package Tests

import model.direction.{south, west}
import model.{Parent_Tile, add_neighbour, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Parent_Tile_spec extends AnyWordSpec with Matchers {
  "A Parent_Tile" when {
    "not initialized with any value " should {
      val emptyParent = Parent_Tile()
      "have no neighbours" in {
        emptyParent.neighbours.isEmpty should be(true)
      }
      "have no connections" in {
        emptyParent.connections.isEmpty should be(true)
      }
    }
    "initialized with neighbour " should {
      val neighbourTile = Parent_Tile()
      val Parent = Parent_Tile(List(neighbourTile),List(direction.south))
      "have one neighbour" in {
        Parent.neighbours.contains(neighbourTile) should be(true)
      }
      "have one connections" in {
        Parent.connections.contains(direction.south) should be(true)
      }
    }
    "getting a neighbour added to it " should {
      val Parent = Parent_Tile()
      val neighbourTile = Parent_Tile()
      val newParent = Parent_Tile(Parent.add_neighbour_tile(neighbourTile))
      "have one neighbour" in {
        newParent.neighbours.contains(neighbourTile) should be(true)
      }
    }
    "getting a connection added to it " should {
      val Parent = Parent_Tile()
      val newParent = Parent_Tile(List(), Parent.add_connection(direction.west))
      "have one connection" in {
        newParent.connections.contains(direction.west) should be(true)
      }
    }
    "initialized with multiple connections " should {
      val Parent = Parent_Tile(List(), List(direction.south, direction.north,
        direction.west, direction.east, direction.southeast, direction.southwest,
        direction.northeast, direction.northwest))
      "have connection south" in {
        Parent.connections.contains(direction.south) should be(true)
      }
      "have connection north" in {
        Parent.connections.contains(direction.north) should be(true)
      }
      "have connection west" in {
        Parent.connections.contains(direction.west) should be(true)
      }
      "have connection east" in {
        Parent.connections.contains(direction.east) should be(true)
      }
      "have connection southeast" in {
        Parent.connections.contains(direction.southeast) should be(true)
      }
      "have connection southwest" in {
        Parent.connections.contains(direction.southwest) should be(true)
      }
      "have connection northeast" in {
        Parent.connections.contains(direction.northeast) should be(true)
      }
      "have connection northwest" in {
        Parent.connections.contains(direction.northwest) should be(true)
      }
    }
    "getting a neighbour and connection added to it " should {
      val Parent = Parent_Tile()
      val neighbourTile = Parent_Tile()
      val newParent = add_neighbour(Parent, neighbourTile, direction.south)
      "have one neighbour" in {
        newParent.neighbours.contains(neighbourTile) should be(true)
      }
      "have one connections" in {
        newParent.connections.contains(direction.south) should be(true)
      }
    }
  }
}
