import scala.util.{Failure, Success}

import Converters._
import RefinedConverter._
import circe.CirceEncoders._
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import eu.timepit.refined.auto._
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.refined._
import io.circe.syntax._
import purecsv.safe.CSVReader
import pureconfig.loadConfigOrThrow

object NessusLoader {

  def main(args: Array[String]): Unit = {
    val records = parseRecords(args(0))

    val dbConfig = loadConfigOrThrow[DBConfig]("db.default")
    val dataSource = getDataSource(dbConfig)
    implicit val connection = dataSource.getConnection()

    records.foreach { record => Nessus.insert(record) }
    val smallRecords = Nessus.take(5)
    connection.close()

    smallRecords.foreach { r => println(r.asJson.spaces2) }

  }

  def getDataSource(dbConfig: DBConfig): HikariDataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(dbConfig.url)
    hikariConfig.setUsername(dbConfig.username)
    hikariConfig.setPassword(dbConfig.password)
    hikariConfig.setDriverClassName(dbConfig.driver)
    hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

    new HikariDataSource(hikariConfig)
  }

  def parseRecords(filename: String): List[Nessus] = {
    val reader = CSVReader[Nessus]
    val allRecords = reader.readCSVFromFileName(filename, skipHeader=true)

    val records = allRecords.collect { case Success(s) => s}
    println(s"records: ${records.length}")

    val failures = allRecords.collect { case Failure(e) => e }
    println(s"failures: ${failures.length}")

    failures.map { _.getLocalizedMessage }.distinct.foreach(println)

    records
  }
}
