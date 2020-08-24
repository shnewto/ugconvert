package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class UgExtractSpec extends AnyFlatSpec with Matchers {

  "The UgExtract object's extractor functionality" should "return an error if the given an empty string" in {
    UgExtract.extractor("") shouldEqual UgExtract.UgExtractError(
      "error: path provided doesn't look like it points to a pdf..."
    )
  }

  "The UgExtract object's extractor function" should "fail when given an existing but non-pdf file path" in {
    val path =
      getClass.getClassLoader.getResource("yellow-wallpaper.txt").getPath
    UgExtract.extractor(path) shouldEqual UgExtract.UgExtractError(
      "error: path provided doesn't look like it points to a pdf..."
    )

    UgExtract.extractor("/") shouldEqual UgExtract.UgExtractError(
      "error: path provided doesn't look like it points to a pdf..."
    )

    UgExtract.extractor("/usr/local") shouldEqual UgExtract.UgExtractError(
      "error: path provided doesn't look like it points to a pdf..."
    )
  }

  "The UgExtract object's extractor function" should "fail when given a non-existant file path" in {
    UgExtract.extractor("/non/existant/path/to/doc.pdf") shouldEqual UgExtract
      .UgExtractError(
        "java.io.FileNotFoundException: /non/existant/path/to/doc.pdf (No such file or directory)"
      )

    UgExtract.extractor("1224232") shouldEqual UgExtract.UgExtractError(
      "error: path provided doesn't look like it points to a pdf..."
    )
  }

  "The UgExtract object's extractor function" should "succeed in extracting text from a simple pdf" in {
    val path =
      getClass.getClassLoader.getResource("should-be-trivial.pdf").getPath
    UgExtract.extractor(path) shouldEqual UgExtract.UgExtractSucceed(
      "Trivial to extract. \n"
    )
  }

  "The UgExtract object's extractor function" should "succeed in extracting text from issue 1" in {
    val pdfPath =
      getClass.getClassLoader.getResource("issue-01/issue-01.pdf").getPath
    val textPath =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath

    val issueText = Source.fromFile(textPath).getLines.mkString

    UgExtract.extractor(pdfPath) shouldBe a[UgExtract.UgExtractSucceed]
  }
}
