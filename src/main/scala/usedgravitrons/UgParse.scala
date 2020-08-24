package usedgravitrons

import scala.util.parsing.combinator._

/**
  * Some multi-line regex parsing to identify and produce the pages
  * we know to look for.
  *
  * Due to be phased out in the short term.
  */
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

/**
  * The workhorse that figures out what sort of page the text we have represents.
  */
object UgParse extends IssueParser {

  /**
    * Behaves badly when things get parallel.
    */
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

  /**
    * Try to parse a table of contents from the given input.
    */
  def getTableOfContentsRaw(
      issueText: String
  ): UgParseResult = {
    parse(tableOfContents, issueText) match {
      case Success(matched, _) => return UgParseSucceed(matched)
      case Failure(msg, _)     => return UgParseError(msg)
      case Error(msg, _)       => return UgParseError(msg)
    }
  }

  /**
    * Try to parse contributor bios from the given input.
    */
  def getContributorBiosRaw(
      issueText: String
  ): UgParseResult = {
    parse(contributorBios, issueText) match {
      case Success(matched, _) => return UgParseSucceed(matched)
      case Failure(msg, _)     => return UgParseError(msg)
      case Error(msg, _)       => return UgParseError(msg)
    }
  }

  /**
    * Taking some short cuts here while I sort out what parsing lib I want to
    * change things up to use...
    */
  def parsePage(issueText: String): UgIssue.UgPage = {

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
