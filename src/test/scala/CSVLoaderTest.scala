import purecsv.safe.CSVReader

import Converters._
import RefinedConverter._

/**
  * Integration-style test that parses CSV data from a file
  */
class CSVLoaderTest extends BaseTest {

  val testFileName = "nessus_test.csv"

  "NessusLoader" must {
    "be able to load records from a file" in {
      val reader = CSVReader[Nessus]
      val records = reader.readCSVFromFileName(testFileName, skipHeader=true)

      all(records) mustBe 'success
    }
  }

}
