package scalacheck

import edu.psu.vmhosting.models.Types.{CVENumber, MacAddress}
import eu.timepit.refined.api.RefType
import org.scalacheck.Gen
import org.scalacheck.Gen.{alphaNumChar, listOf, listOfN, numChar}

/**
  * ScalcCheck Generators for Nessus refined types
  */
object NessusTypeGenerators {

  val genMacAddress: Gen[MacAddress] = for {
    macChars <- listOfN(12, alphaNumChar)
  } yield {
    val str = macChars.grouped(2).map(_.mkString).mkString(":")
    RefType.applyRef[MacAddress](str).right.get
  }

  val genCVE: Gen[CVENumber] = for {
    yearChars <- listOfN(4, numChar)
    vulnChars <- listOf(numChar)
  } yield {
    val str = s"${yearChars.mkString}-${vulnChars.mkString}"
    RefType.applyRef[CVENumber](str).right.get
  }

}
