package scalacheck

import edu.psu.vmhosting.models.Types.{CVENumber, MacAddress}
import eu.timepit.refined.api.RefType
import org.scalacheck.Gen
import org.scalacheck.Gen.{choose, listOfN, numChar, oneOf}

/**
  * ScalcCheck Generators for Nessus refined types
  */
object NessusTypeGenerators {

  private val genHexAlphaChar: Gen[Char] = choose('a', 'f')
  private val genHexChar: Gen[Char] = oneOf(genHexAlphaChar, numChar)

  val genMacAddress: Gen[MacAddress] = for {
    macChars <- listOfN(12, genHexChar)
  } yield {
    val str = macChars.grouped(2).map(_.mkString).mkString(":")
    RefType.applyRef[MacAddress](str).right.get
  }

  val genCVE: Gen[CVENumber] = for {
    yearChars <- listOfN(4, numChar)
    vulnChars <- listOfN(4, numChar)
  } yield {
    val str = s"CVE-${yearChars.mkString}-${vulnChars.mkString}"
    RefType.applyRef[CVENumber](str).right.get
  }

}
