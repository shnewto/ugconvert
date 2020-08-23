package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import usedgravitrons.UgParse.UgParseError
import java.io.BufferedReader
import java.io.FileReader
import scala.io.Source

class UgParseSpec extends AnyFlatSpec with Matchers {
  "The UgParse object's get_table_of_contents_raw" should "should produce just the table of contents text when given an entire issue" in {
    val issue_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val toc_path =
      getClass.getClassLoader
        .getResource("issue-01/table-of-contents.txt")
        .getPath

    val issue_text = Source.fromFile(issue_path).getLines.mkString
    val toc_text = Source.fromFile(toc_path).getLines.mkString

    UgParse.get_table_of_contents_raw(issue_text) shouldEqual Right(toc_text)
  }

  "The UgParse object's get_contributor_bios_raw" should "should produce just the table of contents text when given an entire issue" in {
    val issue_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath
    val toc_path =
      getClass.getClassLoader
        .getResource("issue-01/contributor-bios.txt")
        .getPath

    val issue_text = Source.fromFile(issue_path).getLines.mkString
    val bios_text = Source.fromFile(toc_path).getLines.mkString

    UgParse.get_contributor_bios_raw(issue_text) shouldEqual Right(bios_text)
  }
}
