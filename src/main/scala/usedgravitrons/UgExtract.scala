package usedgravitrons

import java.io.File

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper

object UgExtract {

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

  trait UgExtractResult {
    val text: String
  }

  case class UgExtractSucceed(val text: String) extends UgExtractResult

  case class UgExtractError(val text: String) extends UgExtractResult

}
