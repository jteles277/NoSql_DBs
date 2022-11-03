 
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

    private static void main( String[] args){
            
        jedis = new Jedis(); 
        Create_Ord_Set("ord_names", "nomes-pt-2021.csv");
        jedis.close(); 
        
        Scanner scan = new Scanner(System.in);

        while(true){
            jedis = new Jedis(); 

            System.out.print("\n Search for ('Enter' for quit): ");
            String prefix = scan.nextLine(); 

            List<Tuple> matches = Get_Matches_Ord(prefix, "ord_names");
            Collections.sort(matches,
                new Comparator<Tuple>() {
                    @Override
                    public int compare(Tuple o1, Tuple o2){ 
                        if(o1.getScore() < o2.getScore())
                            return 1;
                        else if(o2.getScore() <o1.getScore())
                            return -1;
                        return 0;
                    }
                }
            );

            for(Tuple t: matches){
                System.out.println("\t" + t.getElement());
            }
            if(matches.size() == 0){
                System.out.println("\tNo matches found!");
            }

            jedis.close();
        }  

        
    } 

    private static List<Tuple> Get_Matches_Ord(String prefix, String Set){

        List<Tuple> matches = new ArrayList<Tuple>();
         
        ScanParams scanParams = new ScanParams().count(10).match(prefix+"*");
        String cur = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<Tuple> scanResult = jedis.zscan(Set, cur, scanParams);

            // work with result
            for(Tuple t : scanResult.getResult()){
                matches.add(t);
            }
            cur = scanResult.getCursor();

        } while (!cur.equals(ScanParams.SCAN_POINTER_START));

        return matches;
        
    }
    
    //Generates a Ordered Set
    private static void Create_Ord_Set(String new_list_name, String file_name){

        try {
            File myObj = new File(file_name);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] slpt_data = data.split(";"); 
                jedis.zadd(new_list_name, Double.parseDouble(slpt_data[1]), slpt_data[0]);                
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        
    }

}