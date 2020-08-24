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

  case class UgExtractError(info: String)

  def extractor(fpath: String): Either[UgExtractError, String] = {
    if (fpath.split("\\.").last != "pdf") {
      return Left(
        UgExtractError(
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
        Left(UgExtractError(t.toString))
    }
  }

}
