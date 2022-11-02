package cbd.ex2_4;

import static com.mongodb.client.model.Filters.*;

import java.time.Instant;
import java.util.Date;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Sorts.descending;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Accumulators;
 
import org.bson.BsonDocument;
import org.bson.BsonInt64; 
import org.bson.types.ObjectId;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.MongoDatabase;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class App 
{

    public static void main( String[] args )
    { 
         
        // Run alinea a)
        a();

        // Run alinea b)
        b();

        // Run alinea c)
        c();
        
        // Run alinea d)
        d();
        
    }
 
    /*
        a) 
        "main" method for alinea a)
    */
    private static void a(){

        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);

        // Set up MongoDB deployment's connection string
        String uri = "mongodb://localhost:27017";
        MongoClient client = MongoClients.create(uri);

        

        //insert 
            insert(client, "mare2"); 
        
        //find 
            find(client, "mare2"); 

        //update 
            update(client, "mare2", "Potuguese cousine"); 
        
        //find 
            find(client, "mare2"); 
        

        client.close();
    
    }

    /*
        b) 
        "main" method for alinea b)
    */
    private static void b(){

        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
 
        String uri = "mongodb://localhost:27017";
        MongoClient client = MongoClients.create(uri);


        /*
        
            Here I test the difference in time with and without indexes

            Obviously this only works the first time(because then we already have the indexes)

            When I tested it cut the time in half
                
                before: 6 ms 
                after: 3 ms

        */
         
        long start = System.currentTimeMillis();
        find(client, "mare2");
        long end = System.currentTimeMillis();
        System.out.println("before Index: " + (end - start));

        createIndexes(client);
        
        start = System.currentTimeMillis();
        find(client, "mare2");
        end = System.currentTimeMillis();
        System.out.println("after Index: " + (end - start));

        client.close();
    
    }


    /*
        c) 
        "main" method for alinea b)
    */
    private static void c(){

        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
 
        String uri = "mongodb://localhost:27017";
        MongoClient client = MongoClients.create(uri);

        query_1(client);
        query_2(client);
        query_3(client);
        query_4(client);
        query_9(client);

        client.close();
    
    }

    /*
        d) 
        "main" method for alinea d)
    */
    private static void d(){

        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
 
        System.out.println("\nNumero de localidades distintas: " + countLocalidades());
        
        System.out.println("\nNumero de restaurantes por localidade:");
        Map<String, Integer> map = countRestByLocalidade();
        for (String key : map.keySet()) {
            System.out.println("-> " + key + " - " + map.get(key));
        } 

        String name_q = "Park";
        System.out.println("\nNome de restaurantes contendo" + name_q + " no nome:");
        
        List<String> list =  getRestWithNameCloserTo(name_q);
        for (String name : list) {
            System.out.println("-> " + name);
        } 
        System.out.println("\n");
    
    }


    // a)
        private static void find(MongoClient client, String name) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            // Choose what to show
            Bson projectionFields = Projections.fields(Projections.include("name", "gastronomia"), Projections.excludeId());


            // Choose what to show
            FindIterable<Document> docs = collection.find(eq("name", name)).projection(projectionFields);
            
            System.out.println("\n Restaurants named " + name + ":  \n");
    

            
            for(Document doc : docs) {
                //access documents e.g. doc.get()
                System.out.println("-> " + doc.toJson()); 
            }
            
        }
        private static void insert(MongoClient client,String name) { 
                
            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            InsertOneResult result = collection.insertOne(new Document()
            .append("restaurant_id", new ObjectId())
            .append("name", name)
            .append("gastronomia", "Bakery")
            .append("localidade", "Bronx")
            .append("address", ""));
             
            System.out.println("Done!");
        }        
        private static void update(MongoClient client, String name, String gast) { 
                
            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            Bson update = Updates.set("gastronomia", gast);
            
            FindIterable<Document> docs = collection.find(eq("name", name));

            for (Document doc : docs) { 
                try { 
                    UpdateResult result = collection.updateOne(doc, update); 
                    System.out.println("succes: " + result);
                } catch (MongoException err) {
                    System.err.println("Unable to update due to an error: " + err);
                }
            }
        }
       
    // b)
        private static void createIndexes(MongoClient client){  

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            String resultCreateIndex = collection.createIndex(Indexes.ascending("localidade"));

            System.out.println("Created index " + resultCreateIndex);
            
            resultCreateIndex = collection.createIndex(Indexes.ascending("gastronomia"));
            System.out.println("Created index " + resultCreateIndex);

            // Text Field indexes

            try {
                resultCreateIndex = collection.createIndex(Indexes.text("nome"));
                System.out.println(String.format("Index created: %s", resultCreateIndex));
            } catch (MongoCommandException e) {
                if (e.getErrorCodeName().equals("IndexOptionsConflict"))
                    System.out.println("there's an existing text index with different options");
                else{
                    System.out.println(e);
                }
            } 
            
        }

    // c)     
        private static void query_1(MongoClient client) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

    
            FindIterable<Document> docs = collection.find();
            
            System.out.println("\n all collection:  \n");

            long count = 0;

            for(Document doc : docs) {
                //access documents e.g. doc.get()
                //System.out.println("-> " + doc.toJson()); 
                count++;
            
            } 
            System.out.println(count + " results");
            
        }
        private static void query_2(MongoClient client) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            
            // Choose what to show
            Bson projectionFields = Projections.fields(Projections.include("restaurant_id", "nome"," localidade", "gastronomia")); 
    
            FindIterable<Document> docs = collection.find();
            
            System.out.println("\n all collection with projection:  \n");


            long count = 0;

            for(Document doc : docs) {
                //access documents e.g. doc.get()
                //System.out.println("-> " + doc.toJson()); 
                count++;
            
            } 
            System.out.println(count + " results");
            
        }
        private static void query_3(MongoClient client) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            
        // Choose what to show
        Bson projectionFields = Projections.fields(Projections.include("restaurant_id", "nome"," localidade", "gastronomia"), Projections.excludeId()); 
    
        FindIterable<Document> docs = collection.find();
        
        System.out.println("\n all collection with projection 2:  \n");


        long count = 0;

            for(Document doc : docs) {
                //access documents e.g. doc.get()
                //System.out.println("-> " + doc.toJson()); 
                count++;
            
            } 
            System.out.println(count + " results");
            
        }
        private static void query_4(MongoClient client) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            
    
            long n = collection.countDocuments(eq("localidade", "Brooklyn"));
            

            System.out.println("\n Total number of restaurants in Brookling: " + n + "\n");


            
            
        }
        private static void query_9(MongoClient client) { 

            MongoDatabase database = client.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            
            // Choose what to show 
    
            Bson projectionFields = Projections.fields(Projections.include("restaurant_id", "nome","gastronomia","address.coord"));
                

            FindIterable<Document> docs = collection.find(and(gt("grades.score", 70),ne("gastronomia", "American"),lt("address.coord.0",-65))).projection(projectionFields);


            
            for(Document doc : docs) {
                //access documents e.g. doc.get()
                System.out.println("-> " + doc.toJson()); 
            }
            
        }

    // d)   
    
        
    public static int countLocalidades(){

        int count = 0; 
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            
            MongoDatabase database = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            AggregateIterable<Document> docs = collection.aggregate(Arrays.asList(Aggregates.group("$localidade", Accumulators.sum("count", 1))));

            for (Document doc : docs) {
                count++;
            } 
        } 
        return count;

    }

    public static Map<String, Integer> countRestByLocalidade(){
       
        Map<String,Integer> map = new HashMap<>();

        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            
            MongoDatabase database = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");

            AggregateIterable<Document> docs = collection.aggregate(
                Arrays.asList(
                    Aggregates.group("$localidade", Accumulators.sum("count", 1))
                )
            );

            for (Document doc : docs) {
                map.put((String) doc.get("_id"), (Integer) doc.get("count"));
            }

        
        }


        return map;
    }



    public static List<String> getRestWithNameCloserTo(String name){

        List <String> list = new ArrayList<>();
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            
            MongoDatabase database = mongoClient.getDatabase("cbd");
            MongoCollection<Document> collection = database.getCollection("restaurants");
            
            Bson filter = Filters.text(name);

            for (Document doc : collection.find(filter)) {
                list.add((String)doc.get("nome"));
            }
 


        
        }

        return list;
    }
}
