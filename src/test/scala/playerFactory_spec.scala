import model.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class playerFactory_spec extends AnyWordSpec with Matchers {

  "DefaultPlayerFactory" should {

    "create a player with the given color" in {
      val factory = DefaultPlayerFactory

      val p = factory.create("red")

      p.colorName shouldBe "red"
      p.toString  shouldBe "red"
    }

    "initialize infantry with default value (20)" in {
      val factory = DefaultPlayerFactory

      val p = factory.create("blue")

      p.infantry shouldBe 20
    }
  }
}