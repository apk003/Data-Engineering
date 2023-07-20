package pipeline

import common.{SparkCommon, PostgresCommon}
import org.apache.spark.sql.SparkSession
import org.slf4j.LoggerFactory


object WriteToPostgreSql extends App {

  System.setProperty("hadoop.home.dir", "C:\\winutils")
  private val logger = LoggerFactory.getLogger(getClass.getName)

  val spark: SparkSession = SparkCommon.createSparkSession()

  logger.info("Reading file...")
  val df = spark.read.format("csv").option("header", "true")
    .load("src/main/scala/pipeline/data/Police_Stop_Data.csv")

  logger.info("Connecting to PostgreSql...")
  val pgTable = "policeschema.trafficstops"
  val pgConnectionProperties = PostgresCommon.connectToDB()
  logger.info("Connection successful.")

  logger.info("Writing to PostgreSQL...")
  df.write.option("driver", "org.postgresql.Driver")
    .jdbc("jdbc:postgresql://localhost:5432/training", pgTable, pgConnectionProperties)
  logger.info("Write successful.")
}
