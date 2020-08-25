package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class UgParseSpec extends AnyFlatSpec with Matchers {

  "The UgParse object's parsePage" should "should produce a Toc object when given a Toc page candidate" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val tocPath =
      getClass.getClassLoader
        .getResource("issue-01/table-of-contents.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines()
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")

    val tocText = Source.fromFile(tocPath).getLines().mkString

    UgParse.parsePage(issuePages(0)) shouldEqual UgIssue.Toc(
      tocText
    )
  }

  "The UgParse object's parsePage" should "should produce a Bios object when given a Bios page candidate" in {
    val issuePath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val biosPath =
      getClass.getClassLoader
        .getResource("issue-01/contributor-bios.txt")
        .getPath

    val issuePages = Source
      .fromFile(issuePath)
      .getLines()
      .mkString
      .split("Used Gravitrons Quarterly Page [0-9]+")

    val biosText = Source.fromFile(biosPath).getLines().mkString

    UgParse.parsePage(issuePages.last) shouldEqual UgIssue.Bios(
      biosText
    )
  }
}
