/* SimpleApp.scala */
import org.apache.spark.SparkContext

import org.apache.spark.SparkConf
import java.io._

import scala.util.Try

object BuildPoints {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val df = sqlContext.read.format("com.databricks.spark.xml").option("rowTag", "trkpt").load("activities/*.gpx")

    val rows = df.select(df("@lat"),df("@lon")).map(row => ((round(row.getDouble(0)) ,round(row.getDouble(1))),1)).reduceByKey((a, b) => a + b)

    val maxIntensity = rows.takeOrdered(1)(Ordering[Int].reverse.on(_._2)).head

    val toPrint = rows.map(row => s"{location: new google.maps.LatLng(${row._1._1}, ${row._1._2}), weight: ${row._2}},")
    toPrint.coalesce(1).saveAsTextFile("output")


    val pointFile = new BufferedWriter(new FileWriter(new File("output/points.js")))
    val partFile = new BufferedReader(new FileReader(new File("output/part-00000")))


    pointFile.write(s"function getCenter() {return new google.maps.LatLng(${maxIntensity._1._1}, ${maxIntensity._1._2}) }")
    pointFile.newLine()
    pointFile.write(s"function getMaxIntensity() {return ${maxIntensity._2}}")
    pointFile.newLine()

    pointFile.write("function getPoints() {return [")
    pointFile.newLine()

    Iterator.continually(partFile.readLine())
      .takeWhile(_ != null)
      .foreach {pointFile.write(_)}
    pointFile.newLine()
    pointFile.write("];}")
    pointFile.close()
    partFile.close()
  }

  def round(value:Double): Double ={
    BigDecimal(value).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
}