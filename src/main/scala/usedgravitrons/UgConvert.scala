package usedgravitrons

import java.io.{BufferedWriter, File, FileWriter}

import com.spotify.scio._
import com.spotify.scio.values.SideOutput
import org.apache.beam.sdk.transforms.Create

import scala.io.Source

/**
  * The Scio pipeline entrypoint for the UgConvert application (and some utility/helper methods)
  */
object UgConvert extends Edict {

  /**
    * Usage:
    * sbt "runMain sedgravitrons.UgConvert \
    * --project=[PROJECT] --runner=DataflowRunner --zone=[ZONE] \
    * --input=<PDF_ISSUE_PATH> \
    * --bios=<BIOS_OUTPUT_BUCKET> \
    * --toc=<TOC_OUTPUT_BUCKET> \
    * --other=<OTHER_OUTPUT_BUCKET> \
    * --complete=<COMPLETE_ISSUE_OUTPUT_BUCKET>"
    */
  def main(cmdlineArgs: Array[String]): Unit = {
    val (sc, args) = ContextAndArgs(cmdlineArgs)

    // Next steps, build a custom SCollection from these PDFs and extract as a proper transform
    def extractedIssueText =
      UgExtract.extractor(args("input")) match {
        case UgExtract.UgExtractSucceed(text) =>
          text
        case UgExtract.UgExtractError(e) =>
          ""
      }

    // buckets for our "pages of interest" text
    val toc = SideOutput[String]()
    val bios = SideOutput[String]()
    val other = SideOutput[String]()

    val ugpages = sc
      .wrap(sc.pipeline.apply(Create.of(extractedIssueText)))
      .transform("intoPages") {
        _.flatMap(spiltPages(_))

      }

    // Appreciated the Scio project's 'SideInOutExample'
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

  // "Pages" is sort of arbitrary and maybe not exactly what want down the road, maybe 'splitConcept'
  // will end up being more appropriate
  def spiltPages(issueText: String): Array[String] = {
    issueText.split("Used Gravitrons Quarterly Page [0-9]+")
  }

  // Special case convience function, takes a path to a pdf and makes a text
  // file name, i.e. /home/me/issue.pdf -> ./issue.txt
  def outfileNameFromPath(path: String): String = {
    // get filename from path
    val pdfFname = (path.split("/").last)
    // replace the '.pdf' extension with a '.text' extension
    val textFname = pdfFname.take(1 + pdfFname.lastIndexOf(".")) + "txt"

    return textFname
  }

  // Just file IO for repurposing
  def writeText(fname: String, text: String): Unit = {
    val fout = new File(fname)
    val buffer = new BufferedWriter(new FileWriter(fout))
    buffer.write(text)
    buffer.close()
  }
}

/**
  * Something playful
  */
trait Edict {
  lazy val edict: String =
    "the gravitrons have been forgotten, let's make them used again"
}
