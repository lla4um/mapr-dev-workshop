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




## Using Apache Drill and SQL

### Drill and Files

<details>
<summary>Open the steps:</summary>

A very command use case when working with Data is to do analytics. The best language for analytics is SQL, and MapR Converged Platform provide a powerfull distributed SQL query engine: [Apache Drill](https://drill.apache.org/).

Apache Drill allows you to run queries on many datasource: MapR-DB Tables (JSON and Binary), Apache Hbase, MapR-FS with various format.

For this part of the workshop, you will use the Yelp JSON Dataset available [here](https://www.yelp.com/dataset_challenge). **(note: the dataset has been installed on the workshop VMs)**

### 1- Import the JSON documents into MapR-DB JSON tables

We will import the Yelp JSON documents into MapR-DB JSON tables using the [mapr importJSON](https://maprdocs.mapr.com/home/ReferenceGuide/mapr_importjson.html?hl=importjson) command. Note, the source file path specified in `mapr importJSON` must be a valid path in the MapR filesystem. 

On your cluster:

If not already present, create folders for Yelp dataset (tables and files)
```
hadoop fs -mkdir /yelp
hadoop fs -mkdir /yelp_tables
```

Go to the Drill UI [http://localhost:8047](http://localhost:8047), or Zeppelin to run some SQL Queries:


----
1- View the content of the database:

```
select * from  dfs.`/yelp/business.json` limit 1;
```


----
2- Total reviews in the data set

```
select sum(review_count) as totalreviews from dfs.`/yelp/business.json`;
```

----
3- Top 10 states and cities in total number of reviews

<details>
<summary>Solution</summary>

```
select state, city, count(*) totalreviews 
from dfs.`/yelp/business.json` 
group by state, city order by count(*) desc limit 10;
```
</details>


----
4- Average number of reviews per business star rating

<details>
<summary>Solution</summary>

```
select stars,trunc(avg(review_count)) reviewsavg 
from dfs.`/yelp/business.json`
group by stars order by stars desc;
```
</details>

----
5- Top businesses with high review counts (> 1000)

<details>
<summary>Solution</summary>

```
select stars,trunc(avg(review_count)) reviewsavg 
from dfs.`/yelp/business.json`
group by stars order by stars desc;
```

</details>

----
6- Saturday open and close times for a few businesses

<details>
<summary>Solution</summary>

```
select b.name, b.hours.Saturday.`open`,
b.hours.Saturday.`close`  
from
dfs.`/yelp/business.json`
b limit 10;
```

</details>

----
7- Count the number of "Restaurants"

Note: you have to use the `repeated_contains` operator on the `categories` field.

<details>
<summary>Solution</summary>

```
select count(*) as TotalRestaurants 
from dfs.`/yelp/business.json` 
where true=repeated_contains(categories,'Restaurants');
```

</details>


You can find more queries in the Drill tutorial [here](https://drill.apache.org/docs/analyzing-the-yelp-academic-dataset/)


### Drill and MapR-DB

It is also possible to use MapR-DB JSON with Drill, simply replace the directory or file name by your table name, for example:

```
select * from dfs.`/apps/workshop`
```


It is possible to import the Yelp dataset into Mapr-DB using the following command, that you run on your MapR Cluster:


```
mapr importJSON -idField business_id -src /yelp/business.json -dst /yelp_tables/business -mapreduce false
```

Make the tables public if you want to use it from your Java application runnin on your IDE:

```
maprcli table cf edit -path /yelp_tables/business -cfname default -readperm p -writeperm p
```

#### MapR-DB Secondary Indexes

<details>


Run the following query:

```
select name, stars from dfs.`/yelp_tables/business` where stars = 5;
```

**Simple Index**

You can now improve the performance of this query using an index. In a new terminal window run the following command to create an index on the `stars` field sorted in descending order (`-1`).

```
maprcli table index add -path /yelp_tables/business -index idx_stars -indexedfields 'stars:-1'
```

If you execute the query, multiple times, now it should be faster, since the index is used.

In this case Apache Drill is using the index to find all the stars equal to 5, then retrieve the name of the business from the document itself.

You can use the `EXPLAIN PLAN` command or use the `Profiles` tab to see how drill is executing the query.

The result of the explain plan will be a JSON document explaining the query plan, you can look in the `scanSpec` attribute that use the index if present. You can look at the following attributes in the `scanSpec` attribute:

* `secondaryIndex` equals true when a secondary index is created
* `startRow` and `stopRow` that show the index key used by the query
* `indexName` set to `idx_stars` that is the index used by the query


If you want to delete the index to compare the execution plan run the followind command as `mapr` in a terminal:

```
maprcli table index remove -path /yelp_tables/business -index idx_stars  
```

**Covering Queries**

In the index you have created in the previous step, we have specific the `-indexedfields` parameter to set the index key with the `stars` values. It is also possible to add some non indexed field to the index using the `-includedfields`. 

In this case the index includes some other fields that could be used by applications to allow the query to only access the index without having to retrieve any value from the JSON table itself.

Let's recreate the index on `stars` field and add the `name` to the list of included fields.

```
# if you have not deleted the index yet
$ maprcli table index remove -path /yelp_tables/business -index idx_stars  

$ maprcli table index add -path /yelp_tables/business -index idx_stars -indexedfields 'stars:-1' -includedfields 'name'
```

If you execute the query, multiple times, now it should be even faster than previous queries since Drill is doing a covered queries, only accessing the index.

Look at the profiling of the following query using the new index
```
select name, stars from dfs.`/yelp_tables/business` where stars = 5;
```

The main change in the explain plan compare to previous queries is:

* `includedFields` attribute that show the fields used by the queries.

</details>

</details>




## Using Apache Spark on MapR

<details>
<summary> Open the steps: Apache Spark on MapR<summary>

In this part of the lab you will use the Spark Shell to analyze data.

1- Open Spark Shell

Connect to your cluster and run the following command: (note you can also use Zeppelin)

```
/opt/mapr/spark/spark-2.1.0/bin/spark-shell
```

2- Print some data from MapR-DB JSON

To work with MapR-DB JSON You need some imports

```
scala> import com.mapr.db.spark.sql._
scala> import org.apache.spark.sql.SparkSession

```

Then load the data, and print the schema and count the number of documents

```
scala> val dataFromMapR = spark.loadFromMapRDB("/yelp_tables/business")
scala> dataFromMapR.printSchema
scala> dataFromMapR.count
```

3- Group by function 

Count the number of business by city

<details>
<summary>Solution</summary>

```
scala> dataFromMapR.groupBy("city").count().show()
```

</details>


4- Group by function & filter

Using the group by function find all the city that have more than 10000 businesses

<details>
<summary>Solution</summary>

```
scala> dataFromMapR.groupBy("city").count().filter("count > 10000").show()
```

</details>

5- Save the result into MapR-FS

It is a very common use case to process the data with Apache Spark and save the result into files.

Let's for example save the number of business by city into a parquet file :  `/tmp/parquet_result`

<details>
<summary>Solution</summary>

```
scala> import org.apache.spark.sql.SaveMode
scala>  dataFromMapR.groupBy("city").count().write.mode(SaveMode.Overwrite).parquet("maprfs:///tmp/parquet_result")
```


You can use Drill (or Spark) to check the result, for example using Drill:

```
select * from dfs.`/tmp/parquet_result` limit 100
```

</details>

</details>