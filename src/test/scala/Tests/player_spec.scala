package Tests

import TUI.*
import logic.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class player_spec extends AnyWordSpec with Matchers {
  "A player" when {
    "initialized with name and color " should {
      val name = "name"
      val color = "red"
      val player = new player(name, color)
      "have a name" in {
        player.name should be ("name")
      }
      "have a color" in {
        player.colorName should be(color)
      }
      "have 20 soldiers" in {
        player.infantry should be(20)
      }
    }
  }
}

