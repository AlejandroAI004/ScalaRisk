package Tests

import TUI.*
import logic.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class updateTile_spec extends AnyWordSpec with Matchers {
  "A Tile" when {
    "updatet " should {
      val name = "name"
      val color = "red"
      val player = new player(name, color)
      val emptyParent = Parent_Tile()
      val tile = Tile(emptyParent)
      "have an owner" in {
        updateTile(player, 10, tile).owner should be("red")
      }
      "have a parent" in {
        updateTile(player, 10, tile).parent should be(emptyParent)
      }
      "have 10 soldiers" in {
        updateTile(player, 10, tile).soldiers should be(10)
      }
    }
  }
}

