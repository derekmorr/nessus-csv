package scalacheck

import java.net.{Inet4Address, Inet6Address, InetAddress}

import com.google.common.net.InetAddresses
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Gen.{listOfN, oneOf}

/**
  * ScalaCheck generators for IP addresses
  */
object IPGenerators {
  val genIPv4Address: Gen[Inet4Address] =
    arbitrary[Int] map InetAddresses.fromInteger

  val genIPv6Address: Gen[Inet6Address] = for {
    byteList <- listOfN(16, arbitrary[Byte])
  } yield
    InetAddresses
      .fromLittleEndianByteArray(byteList.toArray)
      .asInstanceOf[Inet6Address]

  val genInetAddress: Gen[InetAddress] =
    oneOf(genIPv4Address, genIPv6Address)
}
