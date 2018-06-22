# MapR Developer Workshop

In this workshop you will learn how to develop with MapR DB, Streams and more.

This workshop is using various sample codes available in MapR GitHub repository: [http://github.com/mapr-demos/]( http://github.com/mapr-demos/). You can find more information about the developer samples and plugin in the [MapR Developer Portal](https://mapr.com/developer-portal/.

# Setting up your environment:

* You have a cluster running, that could be:
    * an existing cluster
    * a VM that you can download from [here](https://mapr.com/products/mapr-sandbox-hadoop/)
    * the Mapr Container for Developers (OSX only), see the instructions [here](https://maprdocs.mapr.com/home/MapRContainerDevelopers/MapRContainerDevelopersOverview.html).

* [Install and configure](https://maprdocs.mapr.com/home/AdvancedInstallation/SettingUptheClient-install-mapr-client.html) the MapR client on your developer environment
    * test the installation using the following command:
        ``` hadoop fs -ls / ``



# Discovering MapR Converged Data Platform

MapR Converged Data Platform is a complete data platform allowing developer to build any type of application, from traditional "big data powered by Apache Hadoop" to more modern streaming based applications.

In the following steps you will discover the basic steps to use:

* MapR-DB the NoSQL (“Not Only SQL”) database built into the MapR Converged Data Platform
* MapR-Streams the big data-scale streaming system built into the MapR Converged Data Platform
* MapR-FS the distrubuted file system built into the MapR Converged Data Platform



## Creating and using MapR-DB JSON Table

<details>
<summary>Open the steps: create a table and use MapR-DB Shell</summary>

Open a terminal window and connect using SSH to one node of your cluster

### 1- Connect to MapR Cluster

If you are using the MapR Container for Developers connect using the following command: *(`root` password is `mapr`)*

```
ssh root@localhost -p 2222 
```

### 2- Navigate into MapR-FS

You can list content of the MapR File System using the following commands:

```
hadoop fs -ls /
```

You can also use the file system directly using simple POSIX comamnds: (on the MapR Container for Developers )

```
cd /mapr/

ls 
```

If you are using another cluster:

```
cd /mapr/trainer.mapr.com/

ls
```
where `trainer.mapr.com` is the name of your cluster.

As you can see, you can list the content of MapR File System, that give you access to all the data store on the cluster, that could be made of hundreds or more nodes.


### 3- Create a MapR-DB JSON Table

MapR provide a command line tool named `maprcli` that allows to manage cluster including resource creation. 

----
3.1- Let's create a newa table in the `/apps` directory.
<details> 

```
maprcli table create -path /apps/workshop -tabletype json
```
</details> 

----
3.2- Let's also make this table "public" to ease the access from your development environment:

<details> 

```
maprcli table cf edit -path /apps/workshop -cfname default -readperm p -writeperm p -traverseperm  p
```

This command set the `default` column family permission in read, write and traverse to public `p`. You can find more informations about table permissions in the [MapR Documentation](https://maprdocs.mapr.com/home/MapR-DB/JSON_DB/granting_or_denying_access_to_fields_with_aces.html).
</details> 




### 4- Use MapR-DB Shell

Now that the MapR-DB Table is created you can use MapR-DB Shell to use it.

Run the following commands in to the terminal

-----
4.1 Insert Data
<details> 

```
mapr dbshell

maprdb root:> find /apps/workshop

maprdb root:> find /apps/workshop

maprdb root:> insert /apps/workshop --value '{"_id":"doc001", "name":{ "first":"John","last":"Doe" }, "age":45   }'

maprdb root:> find /apps/workshop

```

Insert more documents:

```
maprdb root:> insert /apps/workshop --value '{"_id":"doc002", "name":{ "first":"David","last":"Simon" }, "age":35   }' 

maprdb root:> insert /apps/workshop --value '{"_id":"doc003", "name":{ "first":"Steve","last":"Allen" }, "age":49  }' 

maprdb root:> find /apps/workshop

```

You can find more information about MapR DB Shell command using: `help`.
</details> 

-----
4.2 Query Documents
<details> 
Now that we have data into MapR-DB, you can find specific documents using the OJAI Query syntax documented [here](https://maprdocs.mapr.com/home/MapR-DB/JSON_DB/OJAIQueryConditionOperators.html) and [MapR-DB Shell Documentation](https://maprdocs.mapr.com/home/MapR-DB/JSON_DB/QueryWithDBShell.html) 


Find documents, where age equals 35, using the `--where` operator
```
maprdb root:> find /apps/workshop --where '{ "$eq" : { "age" : 35 }  }'
```

Same query but limiting the number of fields returned by the query, using the `--fields` operator:
```
maprdb root:> find /apps/workshop --where '{ "$eq" : { "age" : 35 }  }' --fields _id,name.last
```
</details> 

----
4.3 Update Documents

<details> 
You can also use [MapR-DB Shell to update or delete documents(https://maprdocs.mapr.com/home/ReferenceGuide/dbshell-update.html?hl=mutation%2Cdb%2Cjson)]:
 
Adding a new field:
```
maprdb root:> update /apps/workshop --id doc002 --m ' {"$set" : {"type":"player"}  '

maprdb root:> find /apps/workshop

```

Increment `age`: 

```
maprdb root:> update /apps/workshop --id doc002 --m ' {"$increment" : {"age":1}}'

maprdb root:> find /apps/workshop
```

Modify multiple fields
```
maprdb root:> update /apps/workshop --id doc002 --m ' {"$set" : [{"age":33}, {"city":"Paris"}]}'

maprdb root:> find /apps/workshop
```

Remove a field

```
maprdb root:> update /apps/workshop --id doc002 --m ' {"$delete" : ["city","type"]}'

maprdb root:> find /apps/workshop 
```

4.4 Delete Documents

Let's now delete a document

```
maprdb root:> delete /apps/workshop --id doc002 

maprdb root:> find /apps/workshop 

```
</details>

</details>


## Use MapR-DB from Java

<details>
<summary>Open the steps: use MapR-DB JSON from Java</summary>

The Java project is a complete Maven project located in the `./java-sample ` folder.

Open the project in your favorite IDE, and then look into the sources:

* `src/main/java/com/mapr/workshop/exercices/`: contains simple skeleton that you have to finish to use MapR-DB JSON from Java
* `src/main/java/com/mapr/workshop/solutions/`: contains the solutions of the exercices describe above

</details>




## Using Apache Drill to Query MapR-DB

<details>
<summary>Open the steps: use MapR-DB JSON with Drill</summary>

A very command use case when working with Data is to do analytics. The best language for analytics is SQL, and MapR Converged Platform provide a powerfull distributed SQL query engine: [Apache Drill](https://drill.apache.org/).

Apache Drill allows you to run queries on many datasource: MapR-DB Tables (JSON and Binary), Apache Hbase, MapR-FS with various format.

</details>

