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

    def extracted_issue_text =
      UgExtract.extractor(args("input")) match {
        case Right(text) =>
          text
        case Left(UgExtract.UgExtractError(e)) =>
          println(e)
          ""
      }

    debug()
    sc.wrap(sc.pipeline.apply(Create.of(extracted_issue_text)))
      .transform("intoPages") {
        _.flatMap(_.split("Used Gravitrons Quarterly Page [0-9]+"))
      }
      .transform("trimInterestingPages") {
        _.flatMap(p => UgParse.trim_interesting_pages(p))
      }
      .saveAsTextFile(args("output"))

    sc.run()
    ()
  }

  case class UgConvertError(info: String)

  def outfile_name_from_path(path: String): String = {
    // get filename from path
    val pdf_fname = (path.split("/").last)
    // replace the '.pdf' extension with a '.text' extension
    val text_fname = pdf_fname.take(1 + pdf_fname.lastIndexOf(".")) + "txt"

    return text_fname
  }

  def write_text(fname: String, text: String): Unit = {
    val fout = new File(fname)
    val buffer = new BufferedWriter(new FileWriter(fout))
    buffer.write(text)
    buffer.close()
  }

  def debug(): Unit = {

    // val pdf_path_env = sys.env.get("PDF_ISSUE_PATH")
    val text_path_env = sys.env.get("TEXT_ISSUE_PATH")

    text_path_env match {
      case Some(path) =>
        val issue_text = Source.fromFile(path).getLines.mkString
        issue_text
          .split("Used Gravitrons Quarterly Page [0-9]+")
          .map(UgParse.trim_interesting_pages(_))
          .filter(_.nonEmpty)
          .foreach(println)

      // println(UgParse.get_table_of_contents_raw(issue_text))
      case None =>
        println("text_ISSUE_PATH not present in the environment")
    }

    // path_env match {
    //   case Some(path) =>
    //     val outfile_name = outfile_name_from_path(path)

    //     UgExtract.extractor(path) match {
    //       case Right(text) =>
    //         println("writing output to: " + outfile_name)
    //         write_text(outfile_name, text)
    //       case Left(UgExtractError(e)) =>
    //         println(e)
    //     }
    //   case None =>
    //     println("PDF_ISSUE_PATH not present in the environment")
    // }
  }

  // debug()
}

trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
