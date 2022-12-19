# Information Retrieval

This project includes the following features:
- Indexing of Wikipedia data into Apache Lucene
- Ability to search Wikipedia data using Apache Lucene
- Distributed extraction of results using Apache Spark

## Installation

1. Clone this repository
2. Install docker [image iisas/hadoop-spark-pig-hive](https://hub.docker.com/r/iisas/hadoop-spark-pig-hive/tags) and run it using the following command:
```shell
docker run -it -p 12345:12345 -p 8088:8088 -p 8080:8080 -p 8042:8042 -p 8081:8081 -p 19888:19888 iisas/hadoop-spark-pig-hive:2.9.2 bash
```
3. Run the provided jar file with the following command:

```shell
spark-submit --master local --executor-memory 4g --packages com.databricks:spark-xml_2.12:0.15.0 --class project.spark.SparkMain information-retrival-project-1.0-SNAPSHOT.jar "<path-to-wikipedia-dump.xml>"
```

Here an example of the <<path-to-wikipedia-dump.xml>
```shell
file:////datasets/en-wiki-pages-articles.xml
```

## Usage

The Spark-Lucene Information Retrieval system can be accessed via the command line. To search for a term, simply type the term in the command line, and the system will return a list of relevant results.

## Contribute

If you would like to contribute to this project, please submit a pull request.