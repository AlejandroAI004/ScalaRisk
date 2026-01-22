package Tests

import model.mapInit.impl.MapInit.createInitialMap
import controller.Map_Generation.imp1.Map_Generation.print_row
import model.tile.{Parent_Tile, Tile, add_neighbour, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.List

class MapInit_spec extends AnyWordSpec with Matchers {
  "the Test map " when {
    "initialized " should {
      "return a 2*2 List " in {
        createInitialMap() shouldBe a [List[_]]
      }
    }
  }
}