package Tests

import model.tile.{Parent_Tile, add_neighbour, direction}
import model.tile.direction.{south, west}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Parent_Tile_spec extends AnyWordSpec with Matchers {

  "A Parent_Tile" should {

    "have empty neighbours and connections by default" in {
      val t = Parent_Tile()
      t.neighbours shouldBe empty
      t.connections shouldBe empty
      t.name shouldBe ""
    }

    "store the given name" in {
      val t = Parent_Tile(name = "Konstanz")
      t.name shouldBe "Konstanz"
    }

    "add a neighbour without mutating the original" in {
      val a = Parent_Tile(name = "A")
      val b = Parent_Tile(name = "B")

      val a2 = a.add_neighbour_tile(b)

      a.neighbours shouldBe empty          // original unverändert
      a2.neighbours should contain only b  // neuer State
      a2.name shouldBe "A"                 // Name bleibt gleich
    }

    "add multiple neighbours in order" in {
      val a = Parent_Tile(name = "A")
      val b = Parent_Tile(name = "B")
      val c = Parent_Tile(name = "C")

      val a2 = a.add_neighbour_tile(b).add_neighbour_tile(c)

      a2.neighbours shouldBe List(b, c)
    }

    "add a connection without mutating the original" in {
      val a  = Parent_Tile(name = "A")
      val a2 = a.add_connection(direction.north)

      a.connections shouldBe empty
      a2.connections should contain only direction.north
    }

    "add multiple connections in order" in {
      val a  = Parent_Tile(name = "A")
      val a2 = a
        .add_connection(direction.northwest)
        .add_connection(direction.southeast)

      a2.connections shouldBe List(direction.northwest, direction.southeast)
    }
  }

  "add_neighbour function" should {

    "add neighbour and connection in one step" in {
      val a = Parent_Tile(name = "A")
      val b = Parent_Tile(name = "B")

      val result = add_neighbour(a, b, direction.east)

      result.neighbours should contain only b
      result.connections should contain only direction.east
      result.name shouldBe "A"
    }

    "not change the original target" in {
      val a = Parent_Tile(name = "A")
      val b = Parent_Tile(name = "B")

      val _ = add_neighbour(a, b, direction.east)

      a.neighbours shouldBe empty
      a.connections shouldBe empty
    }
  }
}
