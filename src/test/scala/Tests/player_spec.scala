package Tests

import view.*
import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class player_spec extends AnyWordSpec with Matchers {
  "Player class" should {

    "store colorName and set default infantry and ownedTiles" in {
      val p = new Player("red")

      p.colorName  shouldBe "red"
      p.infantry   shouldBe 20
      p.ownedTiles shouldBe empty
      p.toString   shouldBe "red"
    }
  }

  "Player.apply" should {

    "create a red player for input \"red\"" in {
      val p = Player("red")

      p.colorName shouldBe "red"
    }

    "create a blue player for input \"blue\"" in {
      val p = Player("blue")

      p.colorName shouldBe "blue"
    }

    "create a blue player for input \"pink\"" in {
      val p = Player("pink")

      p.colorName shouldBe "pink"
    }

    "create a blue player for input \"green\"" in {
      val p = Player("green")

      p.colorName shouldBe "green"
    }

    "fallback to grey player for unknown color" in {
      val p = Player("yellow")

      p.colorName shouldBe "grey"
    }
  }
}

