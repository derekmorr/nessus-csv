package edu.psu.vmhosting.circe

import java.net.{InetAddress, URL}

import com.google.common.net.{InetAddresses, InternetDomainName}
import io.circe.{Encoder, Json}


object CirceEncoders {

  implicit val idnEncoder = new Encoder[InternetDomainName] {
    override def apply(idn: InternetDomainName): Json = Json.fromString(idn.toString)
  }

  implicit val inetAddressEncoder = new Encoder[InetAddress] {
    override def apply(ip: InetAddress): Json = Json.fromString(InetAddresses.toAddrString(ip))
  }

  implicit val urlEncoder = new Encoder[URL] {
    override def apply(url: URL): Json = Json.fromString(url.toExternalForm)
  }

}
