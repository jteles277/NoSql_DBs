 
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;




public class ex_1_4_a
{
    static Jedis jedis; 

    public static void main( String[] args )
    {   

        jedis = new Jedis(); 
        Create_Set("ord_names", "nomes-pt-2021.csv");
        jedis.close();

        Scanner scan = new Scanner(System.in);

        while(true){
            jedis = new Jedis();  

            System.out.print("\n Search for ('Enter' for quit): ");
            String prefix = scan.nextLine(); 

            List<String> matches = Get_Matches(prefix, "names");
            Collections.sort(matches);
            for(String s: matches){
                System.out.println("\t" + s);
            }
            if(matches.size() == 0){
                System.out.println("\tNo matches found!");
            }

            jedis.close();
        }  
 
         
    }

    private static List<String> Get_Matches(String prefix, String Set){

        List<String> matches = new ArrayList<String>();
            
        ScanParams scanParams = new ScanParams().count(10).match(prefix+"*");
        String cur = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> scanResult = jedis.sscan(Set, cur, scanParams);

            // work with result
            for(String s : scanResult.getResult()){
                matches.add(s);
            }
            cur = scanResult.getCursor();

        } while (!cur.equals(ScanParams.SCAN_POINTER_START));

        return matches;
        
    }

    //Generates a Set 
    private static void Create_Set(String new_list_name, String file_name){

        try {
            File myObj = new File(file_name);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                jedis.sadd(new_list_name, data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}