package scalacheck

import java.net.{InetAddress, URL}
import java.time.LocalDateTime

import DateTimeGenerators.genLocalDateTimeAnyTimeZone
import DNSGenerators.genInternetDomainName
import IPGenerators.genInetAddress
import NessusTypeGenerators.{genCVE, genMacAddress}
import URLGenerator.genURL

import com.google.common.net.InternetDomainName
import edu.psu.vmhosting.models.Types.{CVENumber, MacAddress}
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary

object Arbitraries {
  implicit lazy val arbInetAddress: Arbitrary[InetAddress] =
    Arbitrary(genInetAddress)

  implicit lazy val arbInternetDomainName: Arbitrary[InternetDomainName] =
    Arbitrary(genInternetDomainName)

  implicit lazy val arbLocalDateTime: Arbitrary[LocalDateTime] =
    Arbitrary(genLocalDateTimeAnyTimeZone)

  implicit lazy val arbURL: Arbitrary[URL] =
    Arbitrary(genURL)

  implicit lazy val arbCVE: Arbitrary[CVENumber] =
    Arbitrary(genCVE)

  implicit lazy val arbMacAddress: Arbitrary[MacAddress] =
    Arbitrary(genMacAddress)

  lazy val arbPortNumber: Arbitrary[PortNumber] =
    implicitly[Arbitrary[PortNumber]]

}
