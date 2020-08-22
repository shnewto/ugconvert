package usedgravitrons

import java.io.{BufferedWriter, ByteArrayInputStream, File, FileWriter, IOException}
import java.nio.CharBuffer

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.encryption.AccessPermission
import org.apache.pdfbox.text.PDFTextStripper;

import scala.io.Source

object UgConvert extends Edict with App {

  case class UgError(info: String)

  // From the Scala docs on the Either type:
  // "Convention dictates that Left is used for failure and Right is used for success."
  def check_args(args: Array[String]): Either[UgError, String] = {
    if (args.length != 1) {
      return Left(
        UgError("error: this app requires exactly 1 arg atm, the path to a pdf document")
      )
    }
    return Right(args(0))
  }

  def extractor(fpath: String): Either[UgError, String] = {
    if (fpath.split("\\.").last != "pdf") {
      return Left(
        UgError("error: path provided doesn't look like it points to a pdf...")
      )
    }

    val pdf_fname = (fpath.split("/").last)
    // replace the '.pdf' extension with a '.txt' extension
    val txt_fname = pdf_fname.take(1 + pdf_fname.lastIndexOf(".")) + ".txt"
    try {
      // replace the '.pdf' extension with a '.txt' extension
      val txt_fname = pdf_fname.take(1 + pdf_fname.lastIndexOf(".")) + ".txt"
      val doc = PDDocument.load(new File(fpath))
      val pdfStripper = new PDFTextStripper();
      val text = pdfStripper.getText(doc);
      doc.close()
      return Right(text)
    } catch {
      case t: Throwable =>
        Left(UgError(t.toString))
    }
  }

    def write_text(text: String): Unit = {
      val fout = new File(text)
      val buffer = new BufferedWriter(new FileWriter(fout))
      buffer.write(text)
      buffer.close()
    }

  def debug(): Unit ={
    val path = "/Users/salt/src/ugconvert/src/test/resources/should-be-trivial.pdf"
    extractor(path) match {
      case Right(p) =>
        println(p)
      case Left(UgError(e)) =>
        println(e)
    }

  }

  debug()
}

trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
