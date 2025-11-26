package Tests

import controller.*
import model.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameController_spec extends AnyWordSpec with Matchers {
  "placeinfantry" should {

    "return Left for invalid coordinates" in {
      val player = new player("red")

      val mapData = List(
        List(Tile(parent = null, player = new player("empty"), soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = -1, y = 0, n = 1)

      result shouldBe Left("Invalid coordinates.")
    }

    "return Left when placing more infantry than the player has" in {
      val player = new player("red")
      player.infantry = 2

      val emptyOwner = new player("empty")
      val mapData = List(
        List(Tile(parent = null, player = emptyOwner, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result shouldBe Left("You don't have that many infantry remaining!")
    }

    "return Left when anoher player own this tile" in {
      val player = new player("red")
      val player2 = new player("blue")

      val mapData = List(
        List(Tile(parent = null, player = player2, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result shouldBe Left("Another Player owns this Tile!")
    }

    "return Right when movement is Sucessful" in {
      val player = new player("red")
      player.infantry = 5

      val emptyOwner = new player("empty")
      val mapData = List(
        List(Tile(parent = null, player = emptyOwner, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result.isRight shouldBe true

      val newMap = result.toOption.get
      val updatedTile = newMap(0)(0)

      updatedTile.player shouldBe player
      updatedTile.soldiers shouldBe 3

      player.infantry shouldBe 2
    }

    "add infantry on own tile" in {
      val player = new player("red")
      player.infantry = 5

      val ownTile = Tile(parent = null, player = player, soldiers = 2)
      val mapData = List(List(ownTile))

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 2)

      result.isRight shouldBe true

      val newMap = result.toOption.get
      val updatedTile = newMap(0)(0)

      updatedTile.player shouldBe player
      updatedTile.soldiers shouldBe 4

      player.infantry shouldBe 3
    }

//    "GameController.offense_phase" should {
//
//      "return Left for invalid coordinates" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//        val mapData = List(
//          List(Tile(parent = null, player = attacker, soldiers = 3),
//            Tile(parent = null, player = defender, soldiers = 2))
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = -1, fromY = 0, toX = 1, toY = 0, n = 2)
//
//        result shouldBe Left("Invalid coordinates.")
//      }
//
//      "return Left if from-tile does not belong to player" in {
//        val attacker = new player("red")
//        val other = new player("blue")
//        val defender = new player("green")
//
//        val mapData = List(
//          List(Tile(parent = null, player = other, soldiers = 3),
//            Tile(parent = null, player = defender, soldiers = 2))
//        )
//
//        val controller = new GameController(mapData, List(attacker, other, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 10)
//
//        result shouldBe Left("You can only attack from your own tiles!")
//      }
//
//      "return Left if attacking tile has <= 1 soldier" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//
//        val mapData = List(
//          List(Tile(parent = null, player = attacker, soldiers = 1),
//            Tile(parent = null, player = defender, soldiers = 2))
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 1)
//
//        result shouldBe Left("You need more than 1 infantry on the attacking tile!")
//      }
//
//      "return Left if n <= 0" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//
//        val mapData = List(
//          List(Tile(parent = null, player = attacker, soldiers = 3),
//            Tile(parent = null, player = defender, soldiers = 2))
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 0)
//
//        result shouldBe Left("You must attack with at least 1 infantry!")
//      }
//
//      "return Left if n >= soldiers on from-tile" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//
//        val mapData = List(
//          List(Tile(parent = null, player = attacker, soldiers = 3),
//            Tile(parent = null, player = defender, soldiers = 1))
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 3)
//
//        result shouldBe Left("You must leave at least one infantry on the attacking tile!")
//      }
//
//      "return Left if attacker does not have more infantry than defender" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//
//        val mapData = List(
//          List(Tile(parent = null, player = attacker, soldiers = 7),
//            Tile(parent = null, player = defender, soldiers = 5))
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 4)
//
//        result shouldBe Left("You dont have more infantry than your opponent!")
//      }
//
//      "return Left if target tile is own or empty" in {
//        val attacker = new player("red")
//        val empty = new player("empty")
//
//        val mapDataOwn = List(
//          List(Tile(parent = null, player = attacker, soldiers = 4),
//            Tile(parent = null, player = attacker, soldiers = 2))
//        )
//
//        val controllerOwn = new GameController(mapDataOwn, List(attacker))
//
//        val resultOwn = controllerOwn.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 2)
//
//        resultOwn shouldBe Left("You can only attack enemy tiles!")
//
//        val mapDataEmpty = List(
//          List(Tile(parent = null, player = attacker, soldiers = 4),
//            Tile(parent = null, player = empty, soldiers = 0))
//        )
//
//        val controllerEmpty = new GameController(mapDataEmpty, List(attacker, empty))
//
//        val resultEmpty = controllerEmpty.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 2)
//
//        resultEmpty shouldBe Left("You can only attack enemy tiles!")
//      }
//
//      "return Right and update tiles correctly on successful attack" in {
//        val attacker = new player("red")
//        val defender = new player("blue")
//
//        val fromTile = Tile(parent = null, player = attacker, soldiers = 5)
//        val toTile = Tile(parent = null, player = defender, soldiers = 2)
//
//        val mapData = List(
//          List(fromTile, toTile)
//        )
//
//        val controller = new GameController(mapData, List(attacker, defender))
//
//        val result = controller.offense_phase(attacker, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 3)
//
//        result.isRight shouldBe true
//
//        val newMap = result.toOption.get
//        val newFromTile = newMap(0)(0)
//        val newToTile = newMap(0)(1)
//
//        newFromTile.player shouldBe attacker
//        newFromTile.soldiers shouldBe 2
//
//        newToTile.player shouldBe attacker
//        newToTile.soldiers shouldBe 3
//      }
//    }

    "GameController.allPlayers" should {
      "return the same list that was passed into the controller" in {
        val p1 = new player("red")
        val p2 = new player("blue")
        val players = List(p1, p2)

        val emptyOwner = new player("empty")
        val mapData = List(
          List(Tile(parent = null, player = emptyOwner, soldiers = 0))
        )

        val controller = new GameController(mapData, players)

        controller.allPlayers shouldBe players
      }

      "GameController.tiles" should {
        "return the same mapData that was passed into the controller (or its current state)" in {
          val p1 = new player("red")
          val players = List(p1)

          val emptyOwner = new player("empty")
          val mapData = List(
            List(Tile(parent = null, player = emptyOwner, soldiers = 0))
          )

          val controller = new GameController(mapData, players)

          controller.tiles shouldBe mapData
        }
      }
    }
  }
}