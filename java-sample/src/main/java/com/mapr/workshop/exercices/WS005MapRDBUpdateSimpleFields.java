package com.mapr.workshop.exercices;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.Connection;
import org.ojai.store.DocumentMutation;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;

@SuppressWarnings("Duplicates")
public class WS005MapRDBUpdateSimpleFields {

    public static void main(String[] args) {

        System.out.println("==== Start Application ===");


        // Get connection on retrieve all documents

        // Create an OJAI connection to MapR cluster
        // Note: the connection will use the cluster information stored in /opt/mapr/mapr-clusters.conf
        final Connection connection = DriverManager.getConnection("ojai:mapr:");


        // Get an instance of OJAI DocumentStore
        final DocumentStore store = connection.getStore("/apps/workshop");


        // TODO: Using the DocumentMutation class add the field and increment the age by 1 or more
        // TODO:   "type" : "Player"


        // TODO: when Mutation is created use the update method
        // store.update("doc001", mutation);


        // Query the store with the condition
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
