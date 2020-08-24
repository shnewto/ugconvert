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
import usedgravitrons.UgIssue
import com.spotify.scio._
import com.spotify.scio.values.SCollection
import com.spotify.scio.values.SideOutput
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.util.MimeTypes

object UgConvert extends Edict {

  def main(cmdlineArgs: Array[String]): Unit = {
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    def extractedIssueText =
      UgExtract.extractor(args("input")) match {
        case UgExtract.UgExtractSucceed(text) =>
          text
        case UgExtract.UgExtractError(e) =>
          ""
      }

    val toc = SideOutput[String]()
    val bios = SideOutput[String]()
    val other = SideOutput[String]()

    val ugpages = sc
      .wrap(sc.pipeline.apply(Create.of(extractedIssueText)))
      .transform("intoPages") {
        _.flatMap(spiltPages(_))

      }

    val (complete, sideOutputs) = ugpages
      .withSideOutputs(toc, bios, other)
      .map { (p, ctx) =>
        UgParse.parsePage(p) match {
          case UgIssue.Toc(t)  => ctx.output(toc, t)
          case UgIssue.Bios(t) => ctx.output(bios, t)
          case UgIssue.Other(t) =>
            ctx.output(other, t)
        }
        p
      }

    complete.saveAsTextFile(args("output"))
    sideOutputs(toc).saveAsTextFile(args("toc"))
    sideOutputs(bios).saveAsTextFile(args("bios"))
    sideOutputs(other).saveAsTextFile(args("other"))

    sc.run()
  }

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
    val textPathEnv = sys.env.get("TEXT_ISSUE_PATH")
    textPathEnv match {
      case Some(path) =>
        val issueText = Source.fromFile(path).getLines.mkString
        println(UgParse.parsePage(issueText))
      case None =>
        println("TEXT_ISSUE_PATH not present in the environment")
    }
  }
}

trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
