name := "my-heatmap"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1",
  "com.databricks" % "spark-xml_2.10" % "0.3.2",
  "org.apache.spark" %% "spark-sql" % "1.6.1"
)
