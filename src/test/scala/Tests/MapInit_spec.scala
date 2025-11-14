package Tests

import TUI.MapInit.testMap_init
import TUI.Map_Generation.print_row
import model.{Parent_Tile, Tile, add_neighbour, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.List

class MapInit_spec extends AnyWordSpec with Matchers {
  "the Test map " when {
    "initialized " should {
      "return a 2*2 List " in {
        testMap_init() shouldBe a [List[_]]
      }
    }
  }
}