import edu.psu.vmhosting.DatabaseSupport.withConnection
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalacheck.Shapeless._
import scalacheck.Arbitraries._

import edu.psu.vmhosting.models.Nessus

class DBIntegrationTest extends DBTest {

  implicit val arbPn = arbPortNumber

  val arbNessus: Arbitrary[Nessus] = implicitly[Arbitrary[Nessus]]

  "Nessus" must {
    "round-trip records through the database" in withConnection { implicit connection =>
      forAll { record: Nessus =>
        val id = Nessus.insert(record)
        id mustBe defined
        Nessus.forId(id.get).value must === (record)
      }
    }
  }

}
