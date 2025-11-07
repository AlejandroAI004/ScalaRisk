package Tests

import TUI.MapInit.testMap_init
import TUI.Map_Generation.print_row
import TUI.{Parent_Tile, Tile, add_neighbour, direction}
import logic.{placeInfantry, player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.List

class placeInfantry_spec extends AnyWordSpec with Matchers {
  "the Test map " when {
    "initialized " should {
      val name = "name"
      val color = "red"
      val player = new player(name, color)
      "return a 2*2 List " in {
        placeInfantry(player, 2, 2, testMap_init()) shouldBe a[List[_]]
      }
    }
  }
}

