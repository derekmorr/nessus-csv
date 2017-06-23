
import edu.psu.vmhosting.DBConfig
import org.scalatest.DoNotDiscover
import pureconfig.loadConfigOrThrow

/**
  * Base class for database integration tests
  */
@DoNotDiscover
class DBTest extends BaseTest {

  implicit lazy val dbConfig: DBConfig = loadConfigOrThrow[DBConfig]("db.default")

}
