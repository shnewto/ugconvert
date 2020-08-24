package usedgravitrons

import scala.util.parsing.combinator._

class IssueParser extends RegexParsers {
  private val until_toc = """.*?(?=Contents)""".r
  private val until_bios =
    """.*?(?=Contributors)""".r

  def tableOfContents: Parser[String] =
    until_toc ~> """Contents.+""".r ^^ {
      _.toString
    }

  def contributorBios: Parser[String] =
    until_bios ~> """.+""".r ^^ {
      _.toString
    }
}

object UgParse extends IssueParser {

  def parsePageUnsafe(issueText: String): UgIssue.UgPage = {
    getTableOfContentsRaw(issueText) match {
      case UgParseSucceed(text) =>
        return UgIssue.Toc(text)
      case _ =>
    }

    getContributorBiosRaw(issueText) match {
      case UgParseSucceed(text) =>
        return UgIssue.Bios(text)
      case _ =>
    }

    return UgIssue.Other(issueText)
  }

  def getTableOfContentsRaw(
                             issueText: String
                           ): UgParseResult = {
    parse(tableOfContents, issueText) match {
      case Success(matched, _) => return UgParseSucceed(matched)
      case Failure(msg, _) => return UgParseError(msg)
      case Error(msg, _) => return UgParseError(msg)
    }
  }

  def getContributorBiosRaw(
                             issueText: String
                           ): UgParseResult = {
    parse(contributorBios, issueText) match {
      case Success(matched, _) => return UgParseSucceed(matched)
      case Failure(msg, _) => return UgParseError(msg)
      case Error(msg, _) => return UgParseError(msg)
    }
  }

  def parsePage(issueText: String): UgIssue.UgPage = {
    // Some short cuts here because the parsing functionality above behaves
    // with Beam in a way that makes me believe it's not threadsafe
    if (issueText.contains("Editorial...")) {
      return UgIssue.Toc(issueText)
    }
    if (issueText.trim().startsWith("Contributors")) {
      return UgIssue.Bios(issueText)
    }
    return UgIssue.Other(issueText)
  }

  def debug(): Unit = {
    println("for those times you just _need_ to print something...")
  }

  trait UgParseResult {
    val text: String
  }

  case class UgParseSucceed(val text: String) extends UgParseResult

  case class UgParseError(val text: String) extends UgParseResult
}
