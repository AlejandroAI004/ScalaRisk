package Tests

import logic.{Parent_Tile, Tile, direction}
import logic.Map_Generation.print_row
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Map_Genration_spec extends AnyWordSpec with Matchers {
  "A Row " when {
    "initialized with one Tile without connections " should {
      val emptyParent = Parent_Tile()
      val owner = "blue"
      val tile = Tile(emptyParent, owner)
      "print tile with no connections" in {
        print_row(List(tile)) should be(
          "              \n" +
          "  +--------+  \n" +
          "  | blue 1 |  \n" +
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
      val tile = Tile(Parent, owner)
      "print tile with all connections" in {
        print_row(List(tile)) should be(
          "\\     |      /\n" +
          "  +--------+  \n" +
          "__| blue 1 |__\n" +
          "  |        |  \n" +
          "  +--------+  \n" +
          "/     |      \\" + "\n")
      }
    }
  }
}