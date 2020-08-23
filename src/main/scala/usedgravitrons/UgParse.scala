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
  val until_toc = """.*?(?=Contents)""".r

  def table_of_contents: Parser[String] =
    until_toc ~> """Contents.+""".r ^^ {
      _.toString
    }

  val until_bios =
    """.*?(?=Contributors)""".r
  def contributor_bios: Parser[String] =
    until_bios ~> """Contributors.+""".r ^^ {
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

  def trim_interesting_pages(issue_text: String): String = {
    get_table_of_contents_raw(issue_text) match {
      case Right(text) =>
        return text
      case _ =>
    }

    get_contributor_bios_raw(issue_text) match {
      case Right(text) =>
        return text
      case _ =>
    }

    return issue_text
  }

  def debug(): Unit = {
    println("for those times you just _need_ to print something...")
  }
}
