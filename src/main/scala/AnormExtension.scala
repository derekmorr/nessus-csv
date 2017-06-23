import java.net.{InetAddress, URL}

import scala.util.{Success, Try}

import anorm.{Column, MetaDataItem, ParameterMetaData, ToStatement}
import AnormSupport._
import com.google.common.net.{InetAddresses, InternetDomainName}


trait InetAddressAnormSupport {
  implicit val inetAddressMetadata: ParameterMetaData[InetAddress] =
    metadataString[InetAddress]

  implicit val inetAddressToStatement: ToStatement[InetAddress] =
    toStatementString[InetAddress](InetAddresses.toAddrString)

  implicit val inetAddressColumn: Column[InetAddress] =
    columnFromString[InetAddress](InetAddresses.forString)
}

trait InternetDomainNameAnormSupport {
  implicit val idnMetadata: ParameterMetaData[InternetDomainName] =
    metadataString[InternetDomainName]

  implicit val idnToStatement: ToStatement[InternetDomainName] =
    toStatementString[InternetDomainName](_.toString)

  implicit val idnColumn: Column[InternetDomainName] =
    columnFromString[InternetDomainName](InternetDomainName.from)
}

trait URLAnormSupport {
  implicit val urlMetadata: ParameterMetaData[URL] =
    metadataString[URL]

  implicit val urlToStatement: ToStatement[URL] =
    toStatementString[URL](_.toString)

  implicit val urlColumn: Column[URL] =
    columnFromString[URL](new URL(_))
}

trait URLListAnormSupport {
  def stringsToURL(s: String): List[URL] = {
    def stringToURL(st: String): Try[URL] = Try { new URL(st) }

    val strings = s.split("\n")
    val tries = strings map { str => stringToURL(str) }
    tries.toList.collect { case Success(url) => url }
  }

  implicit def rowToSeqURL: Column[List[URL]] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, _, _) = meta
    value match {
      case s: String => Right(stringsToURL(s))
      case _ => buildError(value, "List[URL]", qualified)

    }
  }
}

object AnormExtension
  extends InetAddressAnormSupport
  with InternetDomainNameAnormSupport
  with URLAnormSupport
  with URLListAnormSupport

