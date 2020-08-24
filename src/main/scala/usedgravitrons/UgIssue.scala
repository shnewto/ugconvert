package usedgravitrons

object UgIssue {
  trait UgPage {
    val text: String
  }

  case class Toc(val text: String) extends UgPage
  case class Bios(val text: String) extends UgPage
  case class Other(val text: String) extends UgPage
}
