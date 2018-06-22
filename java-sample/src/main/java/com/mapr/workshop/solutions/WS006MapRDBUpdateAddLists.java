package com.mapr.workshop.solutions;

import org.apache.commons.collections.iterators.ArrayListIterator;
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


        // Add a list of of tags to the doc002
        List tags = new ArrayList<String>();
        tags.add("html");
        tags.add("javascript");
        tags.add("node");


        // Add an address that contains street, city, country
        Document address = connection.newDocument()
                .set("street", "21 Jump Street")
                .set("city", "New York")
                .set("country","USA");

        // update document doc002 to add a new attribute and increment agettagsags
        DocumentMutation mutation = connection.newMutation()
                .set("tags", tags)
                .set("address", address);

        store.update("doc002", mutation);


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
