package superheroes

import common.SparkCommon.createSparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{IntegerType, StringType, StructType}

object PopularObscure {
  case class SuperheroNames(id: Int, name: String)
  case class Superhero(value: String)

  def main(args: Array[String]): Unit = {
    val spark = createSparkSession()
    spark.sparkContext.setLogLevel("ERROR")

    val superheroSchema = new StructType()
      .add("id", IntegerType, nullable = true)
      .add("name", StringType, nullable = true)

    import spark.implicits._
    val names = spark.read.schema(superheroSchema)
      .option("sep", " ")
      .csv("src/main/scala/superheroes/Marvel-names.txt")
      .as[SuperheroNames]

    val lines = spark.read
      .text("src/main/scala/superheroes/Marvel-graph.txt")
      .as[Superhero]

    val graphConnections = lines
      .withColumn("id", split(col("value"), " ")(0))
      .withColumn("connections", size(split(col("value"), " ")) - 1)
      .groupBy("id").agg(sum("connections").alias("connections"))

    val popular = graphConnections.sort($"connections".desc).first()
    val popularName = names.filter($"id" === popular(0))
      .select("name")
      .first()

    val minConnections = graphConnections.agg(min("connections")).first().getLong(0)
    val obscure = graphConnections.filter($"connections" === minConnections)
    val obscureNames = obscure.join(names, usingColumn = "id")

    println(s"${popularName(0)} is the most popular superhero with ${popular(1)} connections")
    println("These characters have only " + minConnections + " connections:")

    obscureNames.select("name").show()
  }
}
