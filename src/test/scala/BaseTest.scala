import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest._

/**
  * Base class for tests.
  */
@DoNotDiscover
class BaseTest extends WordSpec
  with MustMatchers
  with GeneratorDrivenPropertyChecks
  with TypeCheckedTripleEquals
  with OptionValues
  with TryValues {

}
