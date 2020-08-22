package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import usedgravitrons.UgConvert.UgError

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

  "The UgConvert object's parse_input functionality" should "return an error when given an array that does not have exactly one element" in {
    UgConvert.check_args(Array.empty) shouldEqual Left(
      UgError("error: this app requires exactly 1 arg atm, the path to a pdf document")
    )

    UgConvert.check_args(Array("an arg", "another arg")) shouldEqual Left(
      UgError("error: this app requires exactly 1 arg atm, the path to a pdf document")
    )
  }

  "The UgConvert object's extractor functionality" should "return an error if the given an empty string" in {
    UgConvert.extractor("") shouldEqual Left(
      UgError("error: path provided doesn't look like it points to a pdf...")
    )
  }

  "The UgConvert object's extract function" should "fail when given an existing but non-pdf file path" in {
    val path = getClass.getClassLoader.getResource("yellow-wallpaper.txt").getPath
    UgConvert.extractor(path) shouldEqual Left(
      UgError("error: path provided doesn't look like it points to a pdf...")
    )

    UgConvert.extractor("/") shouldEqual Left(
      UgError("error: path provided doesn't look like it points to a pdf...")
    )

    UgConvert.extractor("/usr/local") shouldEqual Left(
      UgError("error: path provided doesn't look like it points to a pdf...")
    )
  }

  "The UgConvert object's extract function" should "fail when given a non-existant file path" in {
    UgConvert.extractor("/non/existant/path/to/doc.pdf") shouldEqual Left(UgError("java.io.FileNotFoundException: /non/existant/path/to/doc.pdf (No such file or directory)"))

    UgConvert.extractor("1224232") shouldEqual Left(
      UgError("error: path provided doesn't look like it points to a pdf...")
    )
  }

  "The UgConvert object's extract function" should "succeed in extracting text from a simple pdf" in {
    val path = getClass.getClassLoader.getResource("should-be-trivial.pdf").getPath
    UgConvert.extractor(path).isRight.equals("Trivial to extract.\n")
  }
}
