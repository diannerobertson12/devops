package org.example;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class App {
    public static void main(String[] args) {
        try (MongoClient mongoClient = new MongoClient("localhost", 27000)) {
            MongoDatabase database = mongoClient.getDatabase("mydb");
            MongoCollection<Document> collection =
                    database.getCollection("test");

            Document doc = new Document("name", "Dianne Robertson")
                    .append("class", "DevOps")
                    .append("year", "2026")
                    .append("result",
                            new Document("CW", 95).append("EX", 85));

            collection.insertOne(doc);

            Document myDoc = collection.find().first();
            if (myDoc != null) {
                System.out.println(myDoc.toJson());
            }
        }
    }
}

