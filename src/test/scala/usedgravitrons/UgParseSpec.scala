package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import usedgravitrons.UgParse.UgParseError
import java.io.BufferedReader
import java.io.FileReader
import scala.io.Source
import usedgravitrons.UgIssue

class UgParseSpec extends AnyFlatSpec with Matchers {
  "The UgParse object's getTableOfContentsRaw" should "should produce just the table of contents text when given an entire issue" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val tocPath =
      getClass.getClassLoader
        .getResource("issue-01/table-of-contents.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")
      .map(UgParse.parsePageUnsafe(_))

    // val issueText = Source.fromFile(issuePath).getLines.mkString
    val tocText = Source.fromFile(tocPath).getLines.mkString

    UgParse.getTableOfContentsRaw(issuePages(0).text) shouldEqual UgParse
      .UgParseSucceed(tocText)
  }

  "The UgParse object's getContributorBiosRaw" should "should produce just the table of contents text when given an entire issue" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val biosPath =
      getClass.getClassLoader
        .getResource("issue-01/contributor-bios.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")
      .map(UgParse.parsePageUnsafe(_))

    val biosText = Source.fromFile(biosPath).getLines.mkString

    UgParse.getContributorBiosRaw(issuePages.last.text) shouldEqual UgParse
      .UgParseSucceed(
        biosText
      )
  }

  "The UgParse object's parsePageUnsafe" should "should produce a Toc object when given a Toc page candidate" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val tocPath =
      getClass.getClassLoader
        .getResource("issue-01/table-of-contents.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")

    val tocText = Source.fromFile(tocPath).getLines.mkString

    UgParse.parsePageUnsafe(issuePages(0)) shouldEqual UgIssue.Toc(
      tocText
    )
  }

  "The UgParse object's parsePageUnsafe" should "should produce a Bios object when given a Bios page candidate" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val biosPath =
      getClass.getClassLoader
        .getResource("issue-01/contributor-bios.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")

    val biosText = Source.fromFile(biosPath).getLines.mkString

    UgParse.parsePageUnsafe(issuePages.last) shouldEqual UgIssue.Bios(
      biosText
    )
  }
}
