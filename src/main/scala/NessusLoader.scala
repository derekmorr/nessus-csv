import scala.util.{Failure, Success}

import Converters._
import RefinedConverter._
import com.zaxxer.hikari.{HikariDataSource, HikariConfig}
import purecsv.safe.CSVReader
import purecsv.safe.{intc, stringc}
import pureconfig.loadConfigOrThrow

object NessusLoader {

  def main(args: Array[String]): Unit = {
    val reader = CSVReader[Nessus]
    val allRecords = reader.readCSVFromFileName(args(0), skipHeader=true)

    val records = allRecords.collect { case Success(s) => s}
    println(s"records: ${records.length}")

    val failures = allRecords.collect { case Failure(e) => e }
    println(s"failures: ${failures.length}")

    failures.map { _.getLocalizedMessage }.distinct.foreach(println)

    val dbConfig = loadConfigOrThrow[DBConfig]("db.default")
    val dataSource = getDataSource(dbConfig)
    implicit val connection = dataSource.getConnection()

    records.foreach { record => Nessus.insert(record) }

    connection.close()

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

}
