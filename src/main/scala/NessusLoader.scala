import scala.util.{Failure, Success}

import edu.psu.vmhosting.Converters._
import edu.psu.vmhosting.DBConfig
import edu.psu.vmhosting.DatabaseSupport.withConnection
import edu.psu.vmhosting.purecsv.RefinedConverter._
import edu.psu.vmhosting.circe.CirceEncoders._
import edu.psu.vmhosting.models.Nessus
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

    implicit val dbConfig = loadConfigOrThrow[DBConfig]("db.default")

    val smallRecords = withConnection { implicit connection =>
      records.foreach { record => Nessus.insert(record) }
      Nessus.take(5)
    }

    smallRecords.foreach { r => println(r.asJson.spaces2) }
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
