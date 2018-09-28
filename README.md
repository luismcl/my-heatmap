#My-Heatmap

This is a simple spark script to merge all your sport activities and use the google maps api to build a Heatmap with your poits. 
If you use Strava, you can download all your activities from your profile page: https://www.strava.com/settings/profile

## Pre-requisites

SBT
spark-1.6.1-bin-hadoop2.6
A set of Files
A google maps api key is require to load the HeatMap

## How to build

```bash 
>sbt package
```

## How to run
```bash 
>$SPARK_HOME/bin/spark-submit --packages com.databricks:spark-xml_2.10:0.3.2 --class "BuildPoints" --master local[4] target/scala-2.10/my-heatmap_2.10-1.0.jar
```
 
 This create a file called points.js in the output directory.  
 
 Edit the heatmap_template.html and replace <YOUR_GOOGLE_API_KEY> with your google api key, then open the file in a browser. 