package MovieLens

import common.SparkCommon._
import org.apache.spark.sql.functions._

object main {

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

    // calculate average rating for each user
    val user_ratings = ds.select("user", "rating")
      .groupBy("user").avg("rating")
      .sort("user")
    user_ratings.show()

    // calculate average rating for each movie
    val item_ratings = ds.select("item", "rating")
      .groupBy("item").avg("rating")
      .sort("item")
    item_ratings.show()

    // worst rated movies with over 200 reviews
    val worst_movies = ds.select("item","rating").groupBy("item").count()
      .sort(avg("rating"))
      .filter("count>200")
    worst_movies.show()


    spark.stop()
  }
}
