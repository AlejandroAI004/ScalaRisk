package Tests

import controller.*
import model.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class GameController_spec extends AnyWordSpec with Matchers {
  object TestCombatStrategy extends CombatStrategy {
    override def resolveAttack(attacker: Tile, defender: Tile, troops: Int): (Tile, Tile) = {
      val newFrom = attacker.copy(soldiers = attacker.soldiers - troops)
      val newTo = Tile(defender.parent, attacker.player, troops)
      (newFrom, newTo)
    }
  }
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

    "GameController.offense_phase" should {

      "return Left(\"Invalid coordinates.\") for out-of-range indices" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 5)
        val to = Tile(p2, blue, 3)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = -1, fromY = 0, toX = 1, toY = 0, n = 2)

        result shouldBe Left("Invalid coordinates.")
      }

      "return Left if from-tile does not belong to player" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, blue, 5) // gehört blue, nicht red
        val to = Tile(p2, red, 3)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 2)

        result shouldBe Left("You can only attack from your own tiles!")
      }

      "return Left if attacking tile has <= 1 soldier" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 1)
        val to = Tile(p2, blue, 3)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 1)

        result shouldBe Left("You need more than 1 infantry on the attacking tile!")
      }

      "return Left if n <= 0" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 5)
        val to = Tile(p2, blue, 3)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 0)

        result shouldBe Left("You must attack with at least 1 infantry!")
      }

      "return Left if n >= soldiers on from-tile" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 4)
        val to = Tile(p2, blue, 2)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 4)

        result shouldBe Left("You must leave at least one infantry on the attacking tile!")
      }

      "return Left if target tile is own or empty" in {
        val red = new player("red")
        val empty = new player("empty")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 5)
        val ownTo = Tile(p2, red, 2)
        val empTo = Tile(p2, empty, 0)

        val mapOwn = List(List(from, ownTo))
        val mapEmpty = List(List(from, empTo))

        val ctrlOwn = new GameController(mapOwn, List(red, empty), TestCombatStrategy)
        val ctrlEmpty = new GameController(mapEmpty, List(red, empty), TestCombatStrategy)

        val resultOwn = ctrlOwn.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 2)
        val resultEmpty = ctrlEmpty.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 2)

        resultOwn shouldBe Left("You can only attack enemy tiles!")
        resultEmpty shouldBe Left("You can only attack enemy tiles!")
      }

      "return Left if attacker does not send more soldiers than defender has" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 10)
        val to = Tile(p2, blue, 8)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 8)

        result shouldBe Left("You dont have more infantry than your opponent!")
      }

      "update tiles and mapData correctly on successful attack" in {
        val red = new player("red")
        val blue = new player("blue")
        val p1 = Parent_Tile()
        val p2 = Parent_Tile()
        val from = Tile(p1, red, 10)
        val to = Tile(p2, blue, 3)
        val map = List(List(from, to))

        val ctrl = new GameController(map, List(red, blue), TestCombatStrategy)

        val result = ctrl.offense_phase(red, fromX = 0, fromY = 0, toX = 1, toY = 0, n = 5)

        result.isRight shouldBe true

        val newMap = result.toOption.get
        val newFromTile = newMap(0)(0)
        val newToTile = newMap(0)(1)

        newFromTile.soldiers shouldBe 10 - 5
        newFromTile.player shouldBe red
        newFromTile.parent shouldBe p1

        newToTile.soldiers shouldBe 5
        newToTile.player shouldBe red // Feld übernommen
        newToTile.parent shouldBe p2

        // mapData im Controller ist aktualisiert
        ctrl.tiles shouldBe newMap
      }
    }
  }

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
