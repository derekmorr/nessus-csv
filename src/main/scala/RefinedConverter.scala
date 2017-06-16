import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

import eu.timepit.refined.api.{RefType, Validate}
import purecsv.safe.converter.StringConverter

/**
  * Converter instance for refined types
  */
object RefinedConverter {

  implicit def refinedConverter[R[_, _], T, P](implicit
                                               baseConverter: StringConverter[T],
                                               reftype: RefType[R],
                                               validate: Validate[T, P]) = new StringConverter[R[T, P]] {

    override def tryFrom(str: String): Try[R[T, P]] = {
      baseConverter.tryFrom(str).flatMap { t =>
        reftype.refine[P](t) match {
          case Left(errors) => Failure(new IllegalArgumentException(errors))
          case Right(refinedValue) => Success(refinedValue)
        }
      }
    }

    override def to(value: R[T, P]): String = {
      baseConverter.to(reftype.unwrap(value))
    }

  }

}
