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
import usedgravitrons.UgIssue

import scala.io.Source
import scala.util.parsing.combinator._

class IssueParser extends RegexParsers {
  val until_toc = """.*?(?=Contents)""".r

  def tableOfContents: Parser[String] =
    until_toc ~> """Contents.+""".r ^^ {
      _.toString
    }

  val until_bios =
    """.*?(?=Contributors)""".r
  def contributorBios: Parser[String] =
    until_bios ~> """Contributors.+""".r ^^ {
      _.toString
    }
}

object UgParse extends IssueParser {
  case class UgParseError(info: String)
  def getTableOfContentsRaw(
      issueText: String
  ): Either[UgParseError, String] = {
    parse(tableOfContents, issueText) match {
      case Success(matched, _) => return Right(matched)
      case Failure(msg, _)     => return Left(UgParseError(msg))
      case Error(msg, _)       => return Left(UgParseError(msg))
    }
  }

  def getContributorBiosRaw(
      issueText: String
  ): Either[UgParseError, String] = {
    parse(contributorBios, issueText) match {
      case Success(matched, _) => return Right(matched)
      case Failure(msg, _)     => return Left(UgParseError(msg))
      case Error(msg, _)       => return Left(UgParseError(msg))
    }
  }

  def parsePage(issueText: String): UgIssue.UgPage = {

    getTableOfContentsRaw(issueText) match {
      case Right(text) =>
        return UgIssue.Toc(text)
      case _ =>
    }

    getContributorBiosRaw(issueText) match {
      case Right(text) =>
        return UgIssue.Bios(text)
      case _ =>
    }

    return UgIssue.Other(issueText)
  }

  def debug(): Unit = {
    println("for those times you just _need_ to print something...")
  }
}
