import java.net.InetAddress

import Converters._

/**
  * Unit tests for [[Converters]]
  */
class ConvertersTest extends BaseTest {

  "Converters" must {
    "support IP addresses" which {
      "parse IPv4 addresses" in {
        inetAddressStringConverter.tryFrom("127.0.0.1").success.value mustBe InetAddress.getByName("127.0.0.1")
      }

      "parse IPv6 addresses" in {
        inetAddressStringConverter.tryFrom("2001:db8::1").success.value mustBe InetAddress.getByName("2001:db8::1")
      }

      "reject invalid IP address strings" in {
        inetAddressStringConverter.tryFrom("this is not an ip") mustBe 'failure
      }
    }

    "support yes/no booleans" which {
      "parse 'yes' as true" in {
        yesNoBooleanConverter.tryFrom("yes").success.value mustBe true
      }

      "parse 'no' as false" in {
        yesNoBooleanConverter.tryFrom("no").success.value mustBe false
      }

      "parse other strings as failure" in {
        yesNoBooleanConverter.tryFrom("nope") mustBe 'failure
      }
    }

    "support nesuss-formatted datetime stamps" which {
      "parse 'N/A' as None" in {
        nessusDateTimeConverter.tryFrom("N/A").success.value mustBe None
      }

      "parse valid dates" in {
        nessusDateTimeConverter.tryFrom("Jun 20, 2016 09:09:53 EDT") mustBe 'success
      }
    }

    "support url lists" which {
      val urlString =
        """http://www.psu.edu/
          |https://foo.bar/this/is/a/path
          |http://proxy.server:8080/""".stripMargin

      "parses new-line separated lists of urls" in {
        urlListConverter.tryFrom(urlString).success.value must have length(3)
      }

      "fails when a single url in the list is mal-formed" in {
        val badUrls = urlString + System.lineSeparator() + "thisisnotaprotocol://thingy.stuff"
        urlListConverter.tryFrom(badUrls) mustBe 'failure
      }
    }

  }
}
