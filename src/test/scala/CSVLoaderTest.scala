import edu.psu.vmhosting.models.Nessus
import purecsv.safe.CSVReader
import edu.psu.vmhosting.purecsv.RefinedConverter._
import edu.psu.vmhosting.Converters._
import edu.psu.vmhosting.purecsv.RefinedConverter._
import edu.psu.vmhosting.models.Nessus
import eu.timepit.refined.auto._
import purecsv.safe.CSVReader

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
