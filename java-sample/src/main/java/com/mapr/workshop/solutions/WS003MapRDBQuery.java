package com.mapr.workshop.solutions;

import org.ojai.Document;
import org.ojai.DocumentStream;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.ojai.store.DriverManager;
import org.ojai.store.Query;
import org.ojai.store.QueryCondition;

@SuppressWarnings("Duplicates")
public class WS003MapRDBQuery {

    public static void main(String[] args) {

        System.out.println("==== Start Application ===");


        // Get connection on retrieve all documents

        // Create an OJAI connection to MapR cluster
        // Note: the connection will use the cluster information stored in /opt/mapr/mapr-clusters.conf
        final Connection connection = DriverManager.getConnection("ojai:mapr:");


        // Get an instance of OJAI DocumentStore
        final DocumentStore store = connection.getStore("/apps/workshop");


        // Build an OJAI query with QueryCondition
        final Query query = connection.newQuery()
                .where(
                        connection.newCondition()                   //
                                .is("age", QueryCondition.Op.EQUAL, 49)   // Build an OJAI QueryCondition
                                .build())                                 //
                .build();


        // Query the store with the condition
        final DocumentStream stream = store.findQuery(query);

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
