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
