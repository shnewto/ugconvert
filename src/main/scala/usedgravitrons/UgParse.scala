package usedgravitrons

object UgParse {

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

  trait UgParseResult {
    val text: String
  }

  case class UgParseSucceed(val text: String) extends UgParseResult

  case class UgParseError(val text: String) extends UgParseResult
}
