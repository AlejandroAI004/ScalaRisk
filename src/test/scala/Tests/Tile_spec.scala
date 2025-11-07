package Tests

import logic.{Parent_Tile, Tile}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Tile_spec extends AnyWordSpec with Matchers {
  "A Tile" when {
    "initialized with parent and owner " should {
      val emptyParent = Parent_Tile()
      val owner = "blue"
      val tile = Tile(emptyParent, owner)
      "have a parent" in {
        tile.parent should be(emptyParent)
      }
      "have an owner" in {
        tile.owner should be(owner)
      }
      "have one soldier" in {
        tile.soldiers should be(1)
      }
    }
    "initialized with parent " should {
      val emptyParent = Parent_Tile()
      val tile = Tile(emptyParent)
      "have a parent" in {
        tile.parent should be(emptyParent)
      }
      "have an owner" in {
        tile.owner should be("user")
      }
      "have one soldier" in {
        tile.soldiers should be(1)
      }
    }
  }
}