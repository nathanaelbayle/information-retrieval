

## Goal

Rewrite the extraction part of my project to parallel processing.



```shell
docker login
```

```shell
net stop winnat
```

```shell
docker run -it -p 12345:12345 -p 8088:8088 -p 8080:8080 -p 8042:8042 -p 8081:8081 -p 19888:19888 iisas/hadoop-spark-pig-hive:2.9.2 bash
```


```shell
spark-shell

val NUM_SAMPLES=1000000
val count = sc.parallelize(1 to NUM_SAMPLES).filter { _ => 
    val x = math.random 
    val y = math.random 
    x*x + y*y < 1 
}.count()
println(s"Pi is roughly ${4.0 * count / NUM_SAMPLES}")
```


Load the package:

```python

pyspark --master local --packages com.databricks:spark-xml_2.12:0.15.0

data = spark.read.format("com.databricks.spark.xml").load("file:////datasets/frwiki-latest-pages-articles.xml")


pages = data.rdd.flatMap(lambda x :(x[1].split('</page>')))


pages = data.rdd.flatMap(lambda x :(x[1].split('</doc>'))).map(lambda x : (get_title(x), get_content(x)))
```


```scala
spark-shell --master spark://127.0.0.1:6066 --packages com.databricks:spark-xml_2.12:0.15.0 --executor-memory 512m

val textFile = spark.read.textFile("file:////datasets/frwiki-latest-pages-articles.xml")


val df = spark.read.option("rowTag", "page").xml("file:////datasets/frwiki-latest-pages-articles.xml")

val selectedData = df.select("title")


```


## Install Mesos

```bash
wget https://downloads.apache.org/mesos/1.11.0/mesos-1.11.0.tar.gz
tar -zxf mesos-1.11.0.tar.gz
sudo apt-get update
sudo apt-get install -y tar wget git
sudo apt-get install -y openjdk-8-jdk
sudo apt-get install -y autoconf libtool
sudo apt-get -y install build-essential python-dev python-six python-virtualenv libcurl4-nss-dev libsasl2-dev libsasl2-modules maven libapr1-dev libsvn-dev zlib1g-dev iputils-ping

cd mesos
mkdir build
cd build
../configure
make
make check
```


spark-submit --packages com.databricks:spark-xml_2.12:0.15.0 --class project.spark.SparkMain information-retrival-project-1.0-SNAPSHOT.jar --driver-java-options="-Dspark.driver.host=spark://0.0.0.0:6066"

spark-submit --master local --executor-memory 4g --packages com.databricks:spark-xml_2.12:0.15.0 --class project.spark.SparkMain information-retrival-project-1.0-SNAPSHOT.jar 