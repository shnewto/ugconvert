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
import com.spotify.scio._
import com.spotify.scio.values.{SCollection}

object UgExtract {
  trait UgExtractResult {
    val text: String
  }

  case class UgExtractSucceed(val text: String) extends UgExtractResult
  case class UgExtractError(val text: String) extends UgExtractResult

  def extractor(fpath: String): UgExtractResult = {
    if (fpath.split("\\.").last != "pdf") {
      return UgExtractError(
        "error: path provided doesn't look like it points to a pdf..."
      )
    }
    try {
      val doc = PDDocument.load(new File(fpath))
      val pdfStripper = new PDFTextStripper();
      val text = pdfStripper.getText(doc);
      doc.close()
      return UgExtractSucceed(text)
    } catch {
      case t: Throwable =>
        UgExtractError(t.toString)
    }
  }

}
