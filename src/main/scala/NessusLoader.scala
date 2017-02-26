import scala.util.{Failure, Success}

import Converters._
import purecsv.safe.CSVReader
import purecsv.safe.{intc, stringc}

object NessusLoader {

  def main(args: Array[String]): Unit = {
    val reader = CSVReader[Nessus]
    val allRecords = reader.readCSVFromFileName(args(0), skipHeader=true)

    val records = allRecords.collect { case Success(s) => s}
    println(s"records: ${records.length}")

    val failures = allRecords.collect { case Failure(e) => e }
    println(s"failures: ${failures.length}")

    failures.take(10).foreach(println)
  }

}
