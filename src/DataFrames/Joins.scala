package BigDataEssentials.DataFrames

import org.apache.spark.sql.SparkSession

object Joins extends App {

  val spark = SparkSession.builder()
    .appName("Joins")
    .config("spark.master", "local")
    .getOrCreate()

  // can't find guitar datasets, just watch video

  /**
   *
   * Exercise:
   *
   * 1)
   *
   */
}
