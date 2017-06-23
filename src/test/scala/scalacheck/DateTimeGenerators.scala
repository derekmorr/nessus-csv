package scalacheck

import java.time._

import scala.collection.JavaConverters._

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

/**
  * ScalaCheck generators for Java 8 DateTime classes
  */
object DateTimeGenerators {
  val genZoneIdString: Gen[String] =
    Gen.oneOf(ZoneId.getAvailableZoneIds.asScala.toSeq)

  val genZoneId: Gen[ZoneId] =
    genZoneIdString map ZoneId.of

  val genLocalDateTimeAnyTimeZone: Gen[LocalDateTime] = for {
    seconds  <- arbitrary[Int]
    timezone <- genZoneId
  } yield
    ZonedDateTime.now(timezone)
      .plusSeconds(seconds.toLong)
      .withEarlierOffsetAtOverlap()
      .toLocalDateTime

}
