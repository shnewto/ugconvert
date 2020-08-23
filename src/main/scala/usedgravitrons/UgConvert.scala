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

object UgConvert extends Edict with App {

  case class UgConvertError(info: String)

  def extractor(fpath: String): Either[UgConvertError, String] = {
    if (fpath.split("\\.").last != "pdf") {
      return Left(
        UgConvertError(
          "error: path provided doesn't look like it points to a pdf..."
        )
      )
    }
    try {
      val doc = PDDocument.load(new File(fpath))
      val pdfStripper = new PDFTextStripper();
      val text = pdfStripper.getText(doc);
      doc.close()
      return Right(text)
    } catch {
      case t: Throwable =>
        Left(UgConvertError(t.toString))
    }
  }

  def outfile_name_from_path(path: String): String = {
    // get filename from path
    val pdf_fname = (path.split("/").last)
    // replace the '.pdf' extension with a '.txt' extension
    val txt_fname = pdf_fname.take(1 + pdf_fname.lastIndexOf(".")) + "txt"

    return txt_fname
  }

  def write_text(fname: String, text: String): Unit = {
    val fout = new File(fname)
    val buffer = new BufferedWriter(new FileWriter(fout))
    buffer.write(text)
    buffer.close()
  }

  def debug(): Unit = {

    val pdf_path_env = sys.env.get("PDF_ISSUE_PATH")
    val txt_path_env = sys.env.get("TXT_ISSUE_PATH")

    txt_path_env match {
      case Some(path) =>
        val issue_text = Source.fromFile(path).getLines.mkString
        val toc = UgParse.get_table_of_contents_raw(issue_text)
        println("\n\n\n" + toc + "\n\n\n")
      case None =>
        println("TXT_ISSUE_PATH not present in the environment")
    }

    // path_env match {
    //   case Some(path) =>
    //     val outfile_name = outfile_name_from_path(path)

    //     extractor(path) match {
    //       case Right(txt) =>
    //         println("writing output to: " + outfile_name)
    //         write_text(outfile_name, txt)
    //       case Left(UgConvertError(e)) =>
    //         println(e)
    //     }
    //   case None =>
    //     println("PDF_ISSUE_PATH not present in the environment")
    // }
  }

  debug()
}

trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
