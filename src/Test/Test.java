package Test;

import Laborator1.ParseHtml;
import Laborator3.Indexes;
import Laborator4.SearchInFiles;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import proiect.MongoConnection;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    public static void searchWordsLab() throws IOException {
        Indexes indexes = new Indexes();

        indexes.populateDirectIndex();
        indexes.writeDirectIndexToMappingFile("./OutFiles/DirectIndexMappingFile.txt");
        indexes.writeDirectIndexToFile("./OutFiles/DirectIndex.txt");

        indexes.populateIndirectIndex();
        indexes.writeIndirectIndexToMappingFile("./OutFiles/IndirectIndexMappingFile.txt");
        indexes.writeIndirectIndexToFile("./OutFiles/IndirectIndex.txt");

        SearchInFiles searchInFiles = new SearchInFiles(indexes.getIndirectIndex());
        searchInFiles.readSearchLine("press philippines states");
        searchInFiles.search();
    }

    public static void insertToMongoDB() throws IOException {
        MongoConnection connection = new MongoConnection();
        Indexes indexes = new Indexes();
        indexes.populateDirectIndex();
        indexes.populateIndirectIndex();
        connection.insertDirectIndexHashMapToMongoDB(indexes.getDirectIndex(), "directIndex");
        connection.insertIndirectIndexHashMapToMongoDB(indexes.getIndirectIndex(), "indirectIndex");

    }

    public static void main(String[] args) throws IOException {
        searchWordsLab();
        //insertToMongoDB();

    }

}
