package Tests

import TUI.*
import model.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class player_spec extends AnyWordSpec with Matchers {
  "A player" when {
    "initialized with name and color " should {
      val color = "red"
      val player = new player(color)
      "have a color" in {
        player.colorName should be(color)
      }
      "have 20 soldiers" in {
        player.infantry should be(20)
      }
      "return colorName as toString" in {
        val p = new player("blue")
        p.toString should be("blue")
      }
    }
  }
}

