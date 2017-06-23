package scalacheck

import com.google.common.net.InternetDomainName
import org.scalacheck.Gen
import org.scalacheck.Gen.{alphaNumChar, choose, listOfN, oneOf}

/**
  * ScalaCheck generators for DNS types
  */
object DNSGenerators {
  private val MAX_LABEL_LENGTH = 63

  private def genString(min: Int, max: Int) = for {
    len   <- choose(min, max)
    chars <- listOfN(len, alphaNumChar)
  } yield chars.map { _.toString }.mkString

  val genValidLabel: Gen[String] = for {
    base  <- Gen.const("host-")
    label <- genString(1, MAX_LABEL_LENGTH - base.length)
  } yield base + label

  val genInvalidLabel: Gen[String] = for {
    extraCount <- choose(10, 100)
    invalidLabel <- genString(MAX_LABEL_LENGTH + 1, MAX_LABEL_LENGTH + extraCount)
  } yield invalidLabel

  val genInvalidHostname: Gen[String] = for {
    count <- choose(1, 50)
    labels <- listOfN(count, oneOf(genValidLabel, genInvalidLabel))
    invalidLabel <- genInvalidLabel // ensure we get at least one invalid label
  } yield (invalidLabel +: labels).mkString(".")

  val genInternetDomainName: Gen[InternetDomainName] =
    genValidLabel map InternetDomainName.from
}
