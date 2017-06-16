import java.net.{InetAddress, URL}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}

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

  implicit val internetDomainNameConverter = new StringConverter[InternetDomainName] {
    override def tryFrom(str: String): Try[InternetDomainName] = Try { InternetDomainName.from(str) }

    override def to(idn: InternetDomainName): String = idn.toString
  }

  implicit val nessusDateTimeConverter = new StringConverter[Option[LocalDateTime]] {
    private val format = DateTimeFormatter.ofPattern("MMM d, uuuu HH:mm:ss zzz")

    override def tryFrom(s: String): Try[Option[LocalDateTime]] = {
      if (s == "N/A")
        Success(None)
      else
        Try { Option(LocalDateTime.parse(s, format)) }
    }

    override def to(dtOption: Option[LocalDateTime]): String = {
      dtOption match {
        case None => "N/A"
        case Some(datetime) => datetime.format(format)
      }
    }
  }

  implicit val urlListConverter = new StringConverter[List[URL]] {
    override def tryFrom(s: String): Try[List[URL]] = Try { s.lines.toList.map { new URL(_) } }
    override def to(urls: List[URL]): String = urls.map { _.toExternalForm }.mkString(System.lineSeparator())
  }
}
