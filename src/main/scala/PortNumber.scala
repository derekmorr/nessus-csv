import scala.language.{implicitConversions, postfixOps}
import scalaz.{Validation, ValidationNel}

/**
  * Wrapper for a TCP/UDP port number.
  */
case class PortNumber(portNumber: Int) {
  require(PortNumber.isValidPortNumber(portNumber),
    s"port number must be between ${PortNumber.minPort} and ${PortNumber.maxPort}")

  def isPrivileged: Boolean = portNumber < 1024
}

object PortNumber {

  val minPort = 0
  val maxPort = 65535

  def build(i: Int): ValidationNel[String, PortNumber] = {
    Validation.fromTryCatchNonFatal { PortNumber(i) } leftMap { ex => ex.getMessage } toValidationNel
  }

  def isValidPortNumber(portNumber: Int): Boolean = {
    minPort <= portNumber && portNumber <= maxPort
  }
}
