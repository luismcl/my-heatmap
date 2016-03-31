/* SimpleApp.scala */
import org.apache.spark.SparkContext

import org.apache.spark.SparkConf

object BuildPoints {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val df = sqlContext.read.format("com.databricks.spark.xml").option("rowTag", "trkpt").load("activities/*.gpx")

    val rows = df.select(df("@lat"),df("@lon")).map(row => ((round(row.getDouble(0)) ,round(row.getDouble(1))),1)).reduceByKey((a, b) => a + b)

    val toPrint = rows.map(row => s"{location: new google.maps.LatLng(${row._1._1}, ${row._1._2}), weight: ${row._2}},")
    toPrint.coalesce(1).saveAsTextFile("output")
  }

  def round(value:Double): Double ={
    BigDecimal(value).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
}