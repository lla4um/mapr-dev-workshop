package com.mapr.workshop.exercices;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;

@SuppressWarnings("Duplicates")
public class WS002MapRDBInsertDocuments {

    public static void main(String[] args) {

        System.out.println("==== Start Application ===");


        // TODO : Just look at the code that shows how to connect and use a table
        // TODO : feel free to modify the code do add your own documents

        // Get connection on retrieve all documents

        // Create an OJAI connection to MapR cluster
        // Note: the connection will use the cluster information stored in /opt/mapr/mapr-clusters.conf
        final Connection connection = DriverManager.getConnection("ojai:mapr:");


        // Get an instance of OJAI DocumentStore
        final DocumentStore store = connection.getStore("/apps/workshop");


        // Create new documents
        Document doc1 = connection.newDocument("{\"_id\":\"doc001\",\"age\":45,\"name\":{\"first\":\"John\",\"last\":\"Doe\"}}");
        Document doc2 = connection.newDocument("{\"_id\":\"doc002\",\"age\":35,\"name\":{\"first\":\"David\",\"last\":\"Simon\"}}");
        Document doc3 = connection.newDocument("{\"_id\":\"doc003\",\"age\":49,\"name\":{\"first\":\"Steve\",\"last\":\"Allen\"}}");
        Document doc4 = connection.newDocument("{\"_id\":\"doc004\",\"age\":43,\"name\":{\"first\":\"Simon\",\"last\":\"Duran\"}}");

        store.insertOrReplace(doc1);
        store.insertOrReplace(doc2);
        store.insertOrReplace(doc3);
        store.insertOrReplace(doc4);
        System.out.println("\t Documents inserted\n");



        // fetch all OJAI Documents from this store
        final DocumentStream stream = store.find();

        for (final Document docs : stream) {
            // Print the OJAI Document
            System.out.println(docs.asJsonString());
        }

        // Close this instance of OJAI DocumentStore
        store.close();

        // close the OJAI connection and release any resources held by the connection
        connection.close();

        System.out.println("==== Stop Application ===");


    }

}
