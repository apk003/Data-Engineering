package pipeline

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object TDSPipeline extends App {
  System.setProperty("hadoop.home.dir", "C:\\winutils")

  val sc = SparkSession
    .builder()
    .appName("TDS Tutorial")
    .config("spark.master", "local")
    .getOrCreate()

  val filepath = "src/main/scala/pipeline/data/Police_Stop_Data.csv"
  var df = sc.read.option("header", "True").csv(filepath).cache()

  val race_searched = df.select("race", "vehicleSearch")
    .where(col("vehicleSearch") === "YES"
      && col("race") != null)

  val race_grouped = race_searched.groupBy("race", "vehicleSearch").count()
  println(race_grouped.show())

  val total_searched = race_searched.count()
  val black_searched = race_searched.where(col("race") === "Black").count()
  val white_searched = race_searched.where(col("race") === "White").count()

  println(s"Black people are recipients of ${100 * (black_searched.toFloat / total_searched.toFloat)}% of vehicle searches")
  println(s"Despite being a racial majority, the same number is ${100 * (white_searched.toFloat / total_searched.toFloat)} for white people")

  val race_unsearched = df.select("race", "vehicleSearch")
    .where(col("vehicleSearch") === "NO"
      && col("race") != null)

  val grouped_unsearched = race_unsearched.groupBy("race", "vehicleSearch")

  val black_unsearched = race_unsearched.where(col("race") === "Black").count()
  val white_unsearched = race_unsearched.where(col("race") === "White").count()
  println(s"The numbers here show a broader story, as it's apparent that traffic stops in general are more common for black people:\n${grouped_unsearched.count.show()}")
  println(s"We additionally see that black people are subject to vehicle searches ${100 * black_searched.toFloat / (black_searched.toFloat+black_unsearched.toFloat)}% of the time, while the same statistic is ${100 * white_searched.toFloat / (white_searched.toFloat+white_unsearched.toFloat)}% for white people")
  println("What is causing this racial bias in policing?")
}
