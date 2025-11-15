package Tests

import model.MapInit.testMap_init
import controller.Map_Generation.print_row
import controller.{Success, placeInfantry}
import model.{Parent_Tile, Tile, add_neighbour, direction, player}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

import scala.List

class placeInfantry_spec extends AnyWordSpec with Matchers {
  "should put the coordinates x,y and the number of infantry" when {
    "initialized " should {
      "do something" in {
        val scriptedX = Iterator(0, 1, 1, 1, 0, 2, 1)
        val scriptedY = Iterator(1, 1, 1, 0, 1, 2, 1)
        val scriptedN = Iterator(1, 1, 1, 2, 1, 1, 1)

        val p1 = new player("blue")
        val p2 = new player("red")
        p1.infantry = 2
        p2.infantry = 2
        val tiles = List.fill(2)(List.fill(2)(Tile(Parent_Tile())))
        val players = List(p1, p2)

        val result = placeInfantry(
          players,
          2, 2,
          tiles,
          () => scriptedX.next(),
          () => scriptedY.next(),
          () => scriptedN.next()
        )

        result._1.flatten.map(_.soldiers) should be(List(0, 0, 2, 2))
        result._1.flatten.map(_.player.colorName) should contain allOf("blue", "red")
        result._2 should be(Success)
      }


      }
    }
  }


