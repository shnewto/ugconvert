package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import usedgravitrons.UgExtract.UgExtractError
import java.io.BufferedReader
import java.io.FileReader
import scala.io.Source

class UgExtractSpec extends AnyFlatSpec with Matchers {

  "The UgExtract object's extractor functionality" should "return an error if the given an empty string" in {
    UgExtract.extractor("") shouldEqual Left(
      UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgExtract object's extractor function" should "fail when given an existing but non-pdf file path" in {
    val path =
      getClass.getClassLoader.getResource("yellow-wallpaper.txt").getPath
    UgExtract.extractor(path) shouldEqual Left(
      UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )

    UgExtract.extractor("/") shouldEqual Left(
      UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )

    UgExtract.extractor("/usr/local") shouldEqual Left(
      UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgExtract object's extractor function" should "fail when given a non-existant file path" in {
    UgExtract.extractor("/non/existant/path/to/doc.pdf") shouldEqual Left(
      UgExtractError(
        "java.io.FileNotFoundException: /non/existant/path/to/doc.pdf (No such file or directory)"
      )
    )

    UgExtract.extractor("1224232") shouldEqual Left(
      UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgExtract object's extractor function" should "succeed in extracting text from a simple pdf" in {
    val path =
      getClass.getClassLoader.getResource("should-be-trivial.pdf").getPath
    UgExtract.extractor(path).isRight.equals("Trivial to extract.\n")
  }

  "The UgExtract object's extractor function" should "extract text from issue 1 the same way it did last time" in {
    val pdf_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.pdf").getPath
    val text_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath

    val issue_text = Source.fromFile(text_path).getLines.mkString
    UgExtract.extractor(pdf_path).isRight.equals(issue_text)
  }
}
