package com.mapr.workshop.exercices;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.Connection;
import org.ojai.store.DocumentMutation;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class WS006MapRDBUpdateAddLists {

    public static void main(String[] args) {

        System.out.println("==== Start Application ===");


        // Get connection on retrieve all documents

        // Create an OJAI connection to MapR cluster
        // Note: the connection will use the cluster information stored in /opt/mapr/mapr-clusters.conf
        final Connection connection = DriverManager.getConnection("ojai:mapr:");


        // Get an instance of OJAI DocumentStore
        final DocumentStore store = connection.getStore("/apps/workshop");



        // TODO: Write the code to update the document doc002 to get the following JSON:
        // TODO : {
        // TODO :    "_id":"doc002",
        // TODO :    "address": {                               =>> NEW FIELD "address"
        // TODO :                "city":"New York",
        // TODO :                "country":"USA",
        // TODO :                "street":"21 Jump Street"
        // TODO :               },
        // TODO :    "age":35,
        // TODO :    "name":{"first":"David","last":"Simon"},
        // TODO :    "tags":["html","javascript","node"]        =>> NEW FIELD  "tags:
        // TODO :   }

        // TODO : store.update("doc002", mutation);


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
