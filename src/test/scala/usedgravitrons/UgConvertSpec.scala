package usedgravitrons

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UgConvertSpec extends AnyFlatSpec with Matchers {
  "The UgConvert object" should "say something self-referential" in {
    UgConvert.edict should include ("gravitron")
  }

  "The UgConvert object" should "say something that invokes nostalgia" in {
    UgConvert.edict should include ("forgotten")
  }

  "The UgConvert object" should "say something that calls to action" in {
    UgConvert.edict should (include ("let's") and include ("again"))
  }
}
