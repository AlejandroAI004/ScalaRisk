package Tests

import TUI.MapInit.testMap_init
import TUI.Map_Generation.{print_map, print_row}
import model.{Parent_Tile, Tile, direction, player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Map_Genration_spec extends AnyWordSpec with Matchers {
  "A Row " when {
    "initialized with one Tile without connections " should {
      val emptyParent = Parent_Tile()
      val owner = "blue"
      val tile = Tile(emptyParent, new player("blue"))
      "print tile with no connections" in {
        stripAnsi(print_row(List(tile))) should be(
          "              \n" +
          "  +--------+  \n" +
          "  | blue 0 |  \n" +
          "  |        |  \n" +
          "  +--------+  \n" +
          "              \n")
      }
    }
    "initialized with one Tile with all connections " should {
      val Parent = Parent_Tile(List(),List(direction.south, direction.north,
        direction.west, direction.east, direction.southeast, direction.southwest,
        direction.northeast, direction.northwest))
      val owner = "blue"
      val tile = Tile(Parent,  new player("blue"))
      "print tile with all connections" in {
        stripAnsi(print_row(List(tile))) should be(
          "\\     |      /\n" +
          "  +--------+  \n" +
          "__| blue 0 |__\n" +
          "  |        |  \n" +
          "  +--------+  \n" +
          "/     |      \\" + "\n")
      }
    }
  }
  "A Map" when {
    "initiailized with one Tile without connections " should {
      val emptyParent = Parent_Tile()
      val owner = "blue"
      val tile = Tile(emptyParent,  new player("blue"))
      "print a 1*1 map " in {
        stripAnsi(print_map(List(List(tile)))) should be(
          "              \n" +
          "  +--------+  \n" +
          "  | blue 0 |  \n" +
          "  |        |  \n" +
          "  +--------+  \n" +
          "              \n")
      }
    }
  }

  def stripAnsi(str: String): String =
    str.replaceAll("\u001B\\[[;\\d]*m", "")
}

