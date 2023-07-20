package common

import java.util.Properties
import org.slf4j.LoggerFactory

object PostgresCommon {

  private val logger = LoggerFactory.getLogger(getClass.getName)

  def connectToDB(): Properties = {

    logger.info("Connecting to PostgreSQL...")
    logger.warn("Creating Dataframe...")

    val pgConnectionProperties = new Properties()
    pgConnectionProperties.put("user", "postgres")
    pgConnectionProperties.put("password", "admin")

    logger.info("Connection successful.")

    pgConnectionProperties
  }
}
