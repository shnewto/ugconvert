package usedgravitrons

// lets start with a couple parts and fill it in incrementally
object UgIssue {
  trait UgPage {
    val text: String
  }

  case class Toc(val text: String) extends UgPage
  case class Bios(val text: String) extends UgPage
  case class Other(val text: String) extends UgPage
}
