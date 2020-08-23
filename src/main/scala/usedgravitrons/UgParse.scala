package usedgravitrons

import java.io.{
  BufferedWriter,
  ByteArrayInputStream,
  File,
  FileWriter,
  IOException
}
import java.nio.CharBuffer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.encryption.AccessPermission
import org.apache.pdfbox.text.PDFTextStripper;

import scala.io.Source
import scala.util.parsing.combinator._

class IssueParser extends RegexParsers {
  val until_toc = """.+?(?=Contents)+Contents""".r
  val until_next_page = """.+?(?=Used Gravitrons Quarterly)+""".r

  def table_of_contents: Parser[String] =
    until_toc ~> until_next_page ^^ {
      _.toString
    }

  val until_bios =
    """.+?(?=Contributors)+Contributors\..+?(?=Contributors)+Contributors""".r
  def contributor_bios: Parser[String] =
    until_bios ~> """.+""".r ^^ {
      _.toString
    }
}

object UgParse extends IssueParser {

  case class UgParseError(info: String)
  def get_table_of_contents_raw(
      issue_text: String
  ): Either[UgParseError, String] = {
    parse(table_of_contents, issue_text) match {
      case Success(matched, _) => return Right(matched)
      case Failure(msg, _)     => return Left(UgParseError(msg))
      case Error(msg, _)       => return Left(UgParseError(msg))
    }
  }

  def get_contributor_bios_raw(
      issue_text: String
  ): Either[UgParseError, String] = {
    parse(contributor_bios, issue_text) match {
      case Success(matched, _) => return Right(matched)
      case Failure(msg, _)     => return Left(UgParseError(msg))
      case Error(msg, _)       => return Left(UgParseError(msg))
    }
  }

  def debug(): Unit = {
    println("for those times you just _need_ to print something...")
  }
}
