package cbd.teles;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.Tuple;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.sql.Timestamp;




public class App 
{
    static Jedis jedis; 

    public static void main( String[] args )
    {   

        // Setting up scanner and the access to the database
        Scanner scan = new Scanner(System.in);
        jedis = new Jedis();

        // Loging system variables
        boolean log_flag = false;
        String Current_User = null;


        while(true){

            // If logged in
            if(log_flag){

                // Interface Prints
                System.out.print("\n Twitter 2 \n ");
                System.out.print("\n 1: See Feed \n 2: Follow User \n 3: Publish message \n 4: See all my subscriptions \n 5: Loggout \n\n\n");
                System.out.print("Insert Command: ");
                String command = scan.nextLine(); 

                switch (command) {

                    case "1":

                        // Print all messages sent to the system in published order 
                        Get_Subscriptions_Msgs(Current_User); 

                    break;

                    case "2":

                        // Print users
                        System.out.print("All users: \n");
                        for(String s : jedis.smembers("Users")){
                            System.out.println(s);
                        }    

                        // Subscribes the current user to the one he choses
                        System.out.print("Chose one to follow");
                        String u2 = scan.nextLine(); 

                        Subscribe_to(Current_User,u2);

                    break;

                    case "3": 

                        // Store a message in the system
                        System.out.print("Msg: ");
                        String msg = scan.nextLine(); 

                        Store_Msg(Current_User, msg);

                    break;

                    case "4":
                        
                        // Print all subscription off the current user
                        Get_Subscriptions(Current_User); 

                    break;

                    case "5":

                        // Logs out 
                        System.out.print("Logging Out... ");
                        log_flag = false;

                    break;
                
                    
                }
            }
            // If not logged in
            else{

                // Interface Prints
                System.out.print("\n Twitter 2 \n ");
                System.out.print("\n 1: Login \n 2: Register \n\n\n");
                System.out.print("Insert Command: ");
                String command = scan.nextLine(); 

                switch (command) {

                    case "1":


                        // Check if the credential given are in the system and logs in 
                        System.out.print("Username: ");
                        String user_name = scan.nextLine(); 

                        System.out.print("Password: ");
                        String pass = scan.nextLine(); 

                        if(Check_User(user_name, pass)){
                            Current_User = user_name;
                            log_flag = true;
                        }
                        else{
                            System.out.print("\nNot in the system... try again or register \n");
                        }

                        

                    break;

                    case "2":
                        
                        // Registers a new user
                        System.out.print("Create Username: ");
                        user_name = scan.nextLine(); 

                        System.out.print("Create Password: ");
                        pass = scan.nextLine(); 

                        Create_User(user_name, pass);

                        Current_User = user_name;
                        log_flag = true;

                    break; 
                    
                
                    
                }
            }
            
             

            jedis.close();
        }
    }


    /*
        Adds user and password to the "Users" hash in the database
    */
    private static void Create_User(String user, String password){ 
      
        //Add User with a password 
        jedis.hset("Users", user, password);  
          
        System.out.println("User Added\n");  
    }

    /*
        Searches for the given credentials in the database and returns true if they exist 
    */
    private static boolean Check_User(String user, String password){
             
        boolean res = false;
         
        
        for(String s : jedis.hkeys("Users")){

            if(s.equals(user) &&jedis.hget("Users", s).equals(password)) 
                return true;
            
        }    

        return false;
    }

    /*
        Adds "u2" to a set with "u1" subscriptions
    */
    private static void Subscribe_to(String u1, String u2){ 
         
        jedis.sadd(u1 +"_subscriptions", u2); 
    }

    /*
        Adds "msg" to a ordered set with "user" messages, ordered by a timestamp 
    */
    private static void Store_Msg(String user, String msg){
               
        jedis.zadd(user +"_messages", System.currentTimeMillis(), user +": " + msg); 
    }

    /*
        Prints all Subscriptions of given "user"
    */
    private static void Get_Subscriptions(String user){ 

        System.out.println("All subscriptios: \n");

        for(String subscription : jedis.smembers(user+"_subscriptions")){ 
            System.out.println(subscription); 
        }    
    }

    /*
        Goes through all subscriptions messages of a given user and prints them, ordered by time of publishement
    */
    private static void Get_Subscriptions_Msgs(String user){
        

        System.out.println("\nFeed: \n");

        //list of all messages
        List<Tuple> feed = new ArrayList<Tuple>();

        // Goes through all subscriptions messages of the given user and adds them to the list
        for(String subscription : jedis.smembers(user+"_subscriptions")){  
            ScanParams scanParams = new ScanParams().count(10);
            String cur = ScanParams.SCAN_POINTER_START;
            do {
                ScanResult<Tuple> scanResult = jedis.zscan(subscription+"_messages", cur, scanParams); 
                for(Tuple t : scanResult.getResult()) 
                    feed.add(t); 
                cur = scanResult.getCursor(); 
            } while (!cur.equals(ScanParams.SCAN_POINTER_START));
        }   

        // Goes through is own messages and adds to the list aswell
        ScanParams scanParams = new ScanParams().count(10);
        String cur = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<Tuple> scanResult = jedis.zscan(user+"_messages", cur, scanParams); 
            for(Tuple t : scanResult.getResult()) 
                feed.add(t); 
            cur = scanResult.getCursor(); 
        } while (!cur.equals(ScanParams.SCAN_POINTER_START));

        // Order the messages in the list by score(timespam)
        Collections.sort(feed,
            new Comparator<Tuple>() {
                @Override
                public int compare(Tuple o1, Tuple o2){ 
                    if(o1.getScore() < o2.getScore())
                        return -1;
                    else if(o2.getScore() <o1.getScore())
                        return 1;
                    return 0;
                }
            }
        );

        // Prints them
        for (Tuple tuple : feed) {
            System.out.println(tuple.getElement());
        }

    }

    

      
     

}
