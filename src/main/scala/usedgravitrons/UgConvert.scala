package usedgravitrons

import java.io.{
  BufferedWriter,
  ByteArrayInputStream,
  File,
  FileWriter,
  IOException
}
import java.nio.CharBuffer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.encryption.AccessPermission
import org.apache.pdfbox.text.PDFTextStripper;

import scala.io.Source
import usedgravitrons.UgParse
import usedgravitrons.UgExtract
import com.spotify.scio._
import org.apache.beam.sdk.transforms.Create;

object UgConvert extends Edict {

  def main(cmdlineArgs: Array[String]): Unit = {
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    def extractedIssueText =
      UgExtract.extractor(args("input")) match {
        case Right(text) =>
          text
        case Left(UgExtract.UgExtractError(e)) =>
          println(e)
          ""
      }

    debug()
    // sc.wrap(sc.pipeline.apply(Create.of(Array(extractedIssueText))))
    sc.wrap(sc.pipeline.apply(Create.of(extractedIssueText)))
      .transform("intoPages") {
        _.flatMap(spiltPages(_))
      }
      .transform("trimInterestingPages") {
        _.map(p => UgParse.parsePage(p))
      }
      .saveAsTextFile(args("output"))

    sc.run()
    ()
  }

  case class UgConvertError(info: String)

  def outfileNameFromPath(path: String): String = {
    // get filename from path
    val pdfFname = (path.split("/").last)
    // replace the '.pdf' extension with a '.text' extension
    val textFname = pdfFname.take(1 + pdfFname.lastIndexOf(".")) + "txt"

    return textFname
  }

  def writeText(fname: String, text: String): Unit = {
    val fout = new File(fname)
    val buffer = new BufferedWriter(new FileWriter(fout))
    buffer.write(text)
    buffer.close()
  }

  def spiltPages(issueText: String): Array[String] = {
    issueText.split("Used Gravitrons Quarterly Page [0-9]+")
  }

  def debug(): Unit = {
    // val pdfPathEnv = sys.env.get("PDF_ISSUE_PATH")
    val textPathEnv = sys.env.get("TEXT_ISSUE_PATH")

    textPathEnv match {
      case Some(path) =>
        val issueText = Source.fromFile(path).getLines.mkString
        issueText
          .split("Used Gravitrons Quarterly Page [0-9]+")
          .map(UgParse.parsePage(_))
          .foreach(println)
      case None =>
        println("TEXT_ISSUE_PATH not present in the environment")
    }
  }
}

trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
