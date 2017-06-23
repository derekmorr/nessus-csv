import java.sql.PreparedStatement

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

import anorm.{Column, ColumnName, MetaDataItem, ParameterMetaData, ToStatement, TypeDoesNotMatch}

object AnormSupport {

  def buildError[B](value: Any, columnType: String, qualified: ColumnName): Left[TypeDoesNotMatch, B] = {
    Left(TypeDoesNotMatch(s"Cannot convert $value: ${value.asInstanceOf[AnyRef].getClass} to $columnType for column $qualified"))
  }

  def toStatementString[T](f: T => String): ToStatement[T] = new ToStatement[T] {
    override def set(s: PreparedStatement, index: Int, t: T): Unit =
      s.setString(index, f(t))
  }

  def metadataString[T]: ParameterMetaData[T] = new ParameterMetaData[T] {
    val sqlType: String = ParameterMetaData.StringParameterMetaData.sqlType
    val jdbcType: Int = ParameterMetaData.StringParameterMetaData.jdbcType
  }

  def columnFromString[T](f: String => T)(implicit ct: ClassTag[T]): Column[T] = Column.nonNull { (value, meta) =>
    val MetaDataItem(qualified, _, clazz) = meta
    value match {
      case str: String => Try { f(str) } match {
        case Success(v) => Right(v)
        case Failure(ex) => buildError(value, clazz, qualified)
      }
      case _ => buildError(value, ct.runtimeClass.getCanonicalName, qualified)
    }
  }
}
