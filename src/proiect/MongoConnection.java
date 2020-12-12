package proiect;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoConnection {

    public MongoConnection() {
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);
    }

    public static MongoDatabase getDatabase(String dbName) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        return database;
    }

    private static void insertIndexToMongoDB(HashMap<String, HashMap<String, Integer>> index,
                                             String documentName, String term1, String term2, String term3) {
        MongoDatabase database = getDatabase("riw");
        MongoCollection<Document> collection = database.getCollection(documentName);
        for(Map.Entry<String, HashMap<String, Integer>> map : index.entrySet()) {
            String key = map.getKey();
            HashMap<String, Integer> hm = map.getValue();
            Document doc = new Document(term1, key);
            List<Document> docs = new ArrayList<>();
            for(Map.Entry<String, Integer> m : hm.entrySet()) {
                docs.add(new Document(term2, m.getKey()).append(term3, m.getValue()));
            }
            doc.append(term2 + "s", docs);
            collection.insertOne(doc);
        }

    }


    public void insertDirectIndexHashMapToMongoDB(HashMap<String, HashMap<String, Integer>> index,
                                                   String documentName) {
        insertIndexToMongoDB(index, documentName, "doc", "term", "cant");
    }

    public void insertIndirectIndexHashMapToMongoDB(HashMap<String, HashMap<String, Integer>> index,
                                                    String documentName) {
        insertIndexToMongoDB(index, documentName, "term", "doc", "cant");
    }
}
