package scalacheck

import java.net.URL

import DNSGenerators.genInternetDomainName
import eu.timepit.refined.types.net.PortNumber
import eu.timepit.refined.scalacheck.numeric._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.oneOf

/**
  * Scalacheck generator for URLs
  */
object URLGenerator {

  val genURLProtocol: Gen[String] =
    oneOf("http", "https", "ftp")

  val genURL: Gen[URL] = for {
    proto <- genURLProtocol
    host  <- genInternetDomainName
    port  <- arbitrary[PortNumber]
    file  <- arbitrary[String]
  } yield new URL(proto, host.toString, port.value, file)

}
