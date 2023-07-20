package MovieLens

import common.SparkCommon._
import org.apache.spark.sql.functions._

object DataSetPractice {

  case class MovieReview(user: Int, item: Int, rating: Int, time: Long)

  def main(args: Array[String]): Unit = {
    val spark = createSparkSession()
    import spark.implicits._

    // read data to dataframe
    val ds = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/scala/MovieLens/movies.csv")
      .as[MovieReview]

    // verify schema
    ds.printSchema()

    // gather best movies with over 200 reviews
    ds.select( "item", "rating")
      .groupBy("item")
      .agg(avg("rating").as("mean"),
          count("item").as("count"))
      .orderBy(desc("mean"), desc("count"))
      .filter("count>200")
      .show()

    spark.stop()
  }
}
