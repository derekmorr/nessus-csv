import java.net.{InetAddress, URL}

import anorm.{Column, ParameterMetaData, ToStatement}
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

object AnormExtension extends InetAddressAnormSupport
  with InternetDomainNameAnormSupport
  with URLAnormSupport

