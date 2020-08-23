package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import usedgravitrons.UgConvert.UgConvertError
import java.io.BufferedReader
import java.io.FileReader
import scala.io.Source

class UgConvertSpec extends AnyFlatSpec with Matchers {
  "The UgConvert object's edict" should "say be self-referential" in {
    UgConvert.edict should include("gravitron")
  }

  "The UgConvert object's edict" should "invoke nostalgia" in {
    UgConvert.edict should include("forgotten")
  }

  "The UgConvert object's edict" should "call to action" in {
    UgConvert.edict should (include("let's") and include("again"))
  }

  "The UgConvert object's extractor functionality" should "return an error if the given an empty string" in {
    UgConvert.extractor("") shouldEqual Left(
      UgConvertError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgConvert object's extract function" should "fail when given an existing but non-pdf file path" in {
    val path =
      getClass.getClassLoader.getResource("yellow-wallpaper.txt").getPath
    UgConvert.extractor(path) shouldEqual Left(
      UgConvertError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )

    UgConvert.extractor("/") shouldEqual Left(
      UgConvertError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )

    UgConvert.extractor("/usr/local") shouldEqual Left(
      UgConvertError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgConvert object's extract function" should "fail when given a non-existant file path" in {
    UgConvert.extractor("/non/existant/path/to/doc.pdf") shouldEqual Left(
      UgConvertError(
        "java.io.FileNotFoundException: /non/existant/path/to/doc.pdf (No such file or directory)"
      )
    )

    UgConvert.extractor("1224232") shouldEqual Left(
      UgConvertError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    )
  }

  "The UgConvert object's extract function" should "succeed in extracting text from a simple pdf" in {
    val path =
      getClass.getClassLoader.getResource("should-be-trivial.pdf").getPath
    UgConvert.extractor(path).isRight.equals("Trivial to extract.\n")
  }

  "The UgConvert object's extract function" should "extract text from issue 1 the same way it does every time" in {
    val pdf_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.pdf").getPath
    val txt_path =
      getClass.getClassLoader.getResource("issue-01/issue-01.txt").getPath

    val issue_text = Source.fromFile(txt_path).getLines.mkString
    UgConvert.extractor(pdf_path).isRight.equals(issue_text)
  }

  "The UgConvert object's outfile_name_from_path function" should "succeed in replacing a .pdf extension with a .txt extension when a path is present" in {
    val path =
      getClass.getClassLoader.getResource("should-be-trivial.pdf").getPath
    UgConvert.outfile_name_from_path(path) shouldEqual "should-be-trivial.txt"
  }

  "The UgConvert object's outfile_name_from_path function" should "succeed in replacing a .pdf extension with a .txt extension when a path not is present" in {
    UgConvert.outfile_name_from_path(
      "should-be-trivial.pdf"
    ) shouldEqual "should-be-trivial.txt"
  }

}
