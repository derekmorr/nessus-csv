import java.net.{InetAddress, URL}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}
import scalaz.std.string._
import scalaz.syntax.equal._

import com.google.common.net.{InetAddresses, InternetDomainName}
import purecsv.safe.converter.StringConverter


/**
  * Custom [[StringConverter]] instances
  */
object Converters {

  implicit val inetAddressStringConverter = new StringConverter[InetAddress] {
    override def tryFrom(str: String): Try[InetAddress] = Try(InetAddresses.forString(str))
    override def to(inetAddress: InetAddress): String = InetAddresses.toAddrString(inetAddress)
  }

  implicit val yesNoBooleanConverter = new StringConverter[Boolean] {
    override def tryFrom(str: String): Try[Boolean] = {
      str.toLowerCase match {
        case "yes" => Success(true)
        case "no"  => Success(false)
        case _     => Failure(new IllegalArgumentException(s"can't parse '$str' to a Boolean'"))
      }
    }

    override def to(b: Boolean): String = if (b) "Yes" else "No"
  }

  implicit val portNumberConverter = new StringConverter[PortNumber] {
    override def tryFrom(s: String): Try[PortNumber] = Try { PortNumber(s.toInt) }
    override def to(p: PortNumber): String = p.portNumber.toString
  }

  implicit val urlConverter = new StringConverter[URL] {
    override def tryFrom(s: String): Try[URL] = Try { new URL(s) }
    override def to(u: URL): String = u.toExternalForm
  }

  implicit val nessusDateTimeConverter = new StringConverter[Option[LocalDateTime]] {
    private val nessusFormat = DateTimeFormatter.ofPattern("MMM d, uuuu HH:mm:ss zzz")

    override def tryFrom(s: String): Try[Option[LocalDateTime]] = {
      if (s === "N/A")
        Success(None)
      else
        Try { Option(LocalDateTime.parse(s, nessusFormat)) }
    }

    override def to(dtOption: Option[LocalDateTime]): String = dtOption.fold("N/A")(_.format(nessusFormat))
  }

  implicit val urlListConverter = new StringConverter[List[URL]] {
    override def tryFrom(s: String): Try[List[URL]] = Try { s.lines.toList.map { new URL(_) } }
    override def to(urls: List[URL]): String = urls.map { _.toExternalForm }.mkString(System.lineSeparator())
  }

  implicit val internetDomainNameConverter = new StringConverter[InternetDomainName] {
    override def tryFrom(str: String): Try[InternetDomainName] = Try { InternetDomainName.from(str) }

    override def to(idn: InternetDomainName): String = idn.toString
  }

}
