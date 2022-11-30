package cbd.cassandra;
 
import com.datastax.oss.driver.api.core.cql.*; 
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
 

import com.datastax.oss.driver.api.core.CqlSession;


public class App 
{
    public static void main( String[] args )
    { 
        
        try (CqlSession session = CqlSession.builder().build()){                                     

            
            query_1(session);
            query_2(session);
            query_3(session);
            query_4(session);
                                    
        }
    }


    // Query 1 ( c) 7. )
    private static void query_1(CqlSession session){  


        session.execute("use video_db_3_2");

        ResultSet resultUpdate = session.execute("SELECT * FROM Video WHERE author = 'user1@ua.pt'");
      
    }
     // Query 1 ( c) 8. )
     private static void query_2(CqlSession session){  


        session.execute("use video_db_3_2");

        ResultSet resultUpdate = session.execute("SELECT * FROM Comment_per_author WHERE author = 'user10@ua.pt'");
    
      
    }
     // Query 1 ( c) 9. )
     private static void query_3(CqlSession session){  


        session.execute("use video_db_3_2");

        ResultSet resultUpdate = session.execute("SELECT * FROM Comment_per_video WHERE video_id = 91b4d180-6e32-11ed-9687-77da2a24bc1e");
    
      
    }
     // Query 1 ( c) 10. )
     private static void query_4(CqlSession session){  


        session.execute("use video_db_3_2");

        ResultSet resultUpdate = session.execute("SELECT avg(value) as Average_rating, count(value) as No_Reviews FROM Rating WHERE video_id = 91b4d180-6e32-11ed-9687-77da2a24bc1e");
    
      
    }




    // Insert 
    private static void create_keyspace(CqlSession session){ 
 
        session.execute("CREATE KEYSPACE video_db_3_2 WITH replication={'class':'SimpleStrategy', 'replication_factor':3}");      

    }
    private static void create_Tables(CqlSession session){ 
        
        session.execute("use video_db_3_2");   
        session.execute("CREATE COLUMNFAMILY User ( email text, username text, name text, reg_timestamp timestamp, PRIMARY KEY(email))");      
        session.execute("CREATE COLUMNFAMILY Video (id uuid, author text, name text, upload_timestamp timestamp, description text, tags list<text>, PRIMARY KEY(id, author, upload_timestamp)) WITH CLUSTERING ORDER BY (author ASC, upload_timestamp DESC)");      
        session.execute("CREATE INDEX ON video (tags)");      
        session.execute("CREATE TABLE Comment_per_author(id uuid,video_id uuid,author text,comment text,upload_timestamp timestamp,PRIMARY KEY (author, upload_timestamp))WITH CLUSTERING ORDER BY (upload_timestamp DESC)");      
        session.execute("CREATE TABLE Comment_per_video(id uuid,video_id uuid,author text,comment text,upload_timestamp timestamp,PRIMARY KEY (video_id, upload_timestamp))WITH CLUSTERING ORDER BY (upload_timestamp DESC)");      
        session.execute("CREATE COLUMNFAMILY Follower(user text,video_id uuid,PRIMARY KEY ((user, video_id)))");      
        session.execute("CREATE COLUMNFAMILY Event(id uuid,user text,video_id uuid,action text,real_timestamp timestamp,video_timestamp int,PRIMARY KEY((user, video_id), real_timestamp, video_timestamp))WITH CLUSTERING ORDER BY (real_timestamp DESC)");    
        session.execute("CREATE COLUMNFAMILY Rating(id uuid,video_id uuid,value int,PRIMARY KEY(video_id, id))");    
        session.execute("create index on rating (value)");      
    }
    private static void inset_data(CqlSession session){ 
        session.execute("use video_db_3_2");   
        try {
            File myObj = new File("Insert_query.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              session.execute(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }   
    }

    // Edit 
    private static void edit_user(CqlSession session, String user, String new_name){ 
        

        session.execute("UPDATE User SET  name = '" + new_name + "' WHERE email='" + user + "';"); 

        ResultSet resultUpdate = session.execute("SELECT json * FROM User;");
    
        System.out.println(resultUpdate.all());
    }

    // Search 
    private static void search_users(CqlSession session){  


        session.execute("use video_db_3_2");

        ResultSet resultUpdate = session.execute("SELECT json * FROM User;");
    
        for(Row r : resultUpdate){
            System.out.println(r.getString(0));
        } 
    }


}
