package Tests

import TUI.*
import model.{Parent_Tile, Tile, player}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Tile_spec extends AnyWordSpec with Matchers {
  "A Tile" when {
    "initialized with parent and owner " should {
      val emptyParent = Parent_Tile()
      val emptyPlayer = new player("empty")
      val tile = Tile(emptyParent, emptyPlayer)
      "have a parent" in {
        tile.parent should be(emptyParent)
      }
      "have an owner" in {
        tile.player.colorName should be("empty")
      }
      "have zero soldiers" in {
        tile.soldiers should be(0)
      }
    }
    "initialized with parent " should {
      val emptyParent = Parent_Tile()
      val tile = Tile(emptyParent)
      "have a parent" in {
        tile.parent should be(emptyParent)
      }
      "have an owner" in {
        tile.player.colorName should be("empty")
      }
      "have no soldier" in {
        tile.soldiers should be(0)
      }
    }
  }
}