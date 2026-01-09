package Tests

import controller.*
import controller.GameController.impl1.GameController
import model.*
import model.Combat.CombatStrategy.{DiceCombatStrategy, SimpleCombatStrategy}
import model.Combat.{CombatStrategy, CombatStrategyPort}
import model.GameEventS.PlaceInfantryEvent
import model.mapInit.imp1
import model.mapInit.imp1.MapInit
import model.player.Player
import model.tile.{Parent_Tile, Tile}
import org.scalatest.TryValues.convertTryToSuccessOrFailure
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import util.gamePhase.GamePhase

import scala.util.Failure

class GameController_spec extends AnyWordSpec with Matchers {
  private object TestCombatStrategy extends CombatStrategyPort {
    override def resolveAttack(attacker: Tile, defender: Tile, troops: Int): (Tile, Tile) = {
      val newFrom = attacker.copy(soldiers = attacker.soldiers - troops)
      val newTo   = defender.copy(player = attacker.player, soldiers = troops)
      (newFrom, newTo)
    }
  }


  def newController(players: List[Player] = Nil): GameController = {
    val mapData = MapInit.createInitialMap()
    new GameController(mapData, players, DiceCombatStrategy)
  }

  private def controllerForOffense(fromSoldiers: Int, toSoldiers: Int): GameController = {
    val red = new Player("red")
    val blue = new Player("blue")

    val p1 = Parent_Tile()
    val p2 = Parent_Tile(neighbours = List(p1))
    val from = Tile(p1, red, fromSoldiers)
    val to = Tile(p2, blue, toSoldiers)
    val map = List(List(from, to))

    val c = new GameController(map, List(red, blue), TestCombatStrategy)
    c
  }

  private def forceOffense(c: GameController): Unit = {
    val cls = c.getClass

    val f =
      try cls.getDeclaredField("phase")
      catch {
        case _: NoSuchFieldException =>
          cls.getDeclaredFields.find(_.getName.toLowerCase.contains("phase")).getOrElse {
            throw new NoSuchFieldException("Could not find phase field via reflection")
          }
      }

    f.setAccessible(true)
    f.set(c, GamePhase.Offense)
  }

  private def mkControllerForOffense(fromSoldiers: Int = 10, toSoldiers: Int = 1): GameController = {
    val red  = new Player("red")
    val blue = new Player("blue")

    // Wichtig: contains() braucht dieselbe Parent-Instanz
    val p2 = Parent_Tile(name = "B")
    val p1 = Parent_Tile(name = "A", neighbours = List(p2))

    val from = Tile(p1, red, fromSoldiers)
    val to   = Tile(p2, blue, toSoldiers)

    val map = List(List(from, to))

    val c = new GameController(map, List(red, blue), TestCombatStrategy)

    forceOffense(c)
    c
  }

  "offense_phase" should {

    "fail when not in offense phase" in {
      val red  = new Player("red")
      val blue = new Player("blue")

      val p2 = Parent_Tile(name = "B")
      val p1 = Parent_Tile(name = "A", neighbours = List(p2))

      val from = Tile(p1, red, 10)
      val to   = Tile(p2, blue, 1)

      val c = new GameController(List(List(from, to)), List(red, blue), TestCombatStrategy)
      // phase ist hier absichtlich NICHT Offense

      val res = c.offense_phase(red, 0, 0, 1, 0, 2)

      res.isFailure shouldBe true
      res.failure.exception.getMessage shouldBe "Not in offense phase"
    }

    "fail on invalid coordinates" in {
      val c = mkControllerForOffense()

      val red = c.allPlayers.find(_.colorName == "red").get
      val res = c.offense_phase(red, -1, 0, 1, 0, 2)

      res.isFailure shouldBe true
      res.failure.exception.getMessage shouldBe "Invalid coordinates."
    }

    "update tiles on successful attack" in {
      val c = mkControllerForOffense(fromSoldiers = 10, toSoldiers = 1)
      val red = c.allPlayers.find(_.colorName == "red").get

      val res = c.offense_phase(red, 0, 0, 1, 0, 5)

      val newMap = res.success.value
      newMap(0)(0).soldiers shouldBe 5     // 10 - 5
      newMap(0)(1).soldiers shouldBe 5
      newMap(0)(1).player.colorName shouldBe "red"
    }
  }

  "startReinforcementPhase" should {
    "give at least 3 infantry to each player" in {
      val red = new Player("red")
      val blue = new Player("blue")
      val p1 = Parent_Tile()
      val p2 = Parent_Tile()

      val map = List(
        List(Tile(p1, red, 1), Tile(p2, red, 1)),
        List(Tile(p1, blue, 1), Tile(p2, blue, 1))
      )

      val c = new GameController(map, List(red, blue), TestCombatStrategy)
      red.infantry = 0
      blue.infantry = 0

      c.startReinforcementPhase()

      red.infantry should be >= 3
      blue.infantry should be >= 3
    }

    "set phase back to Placement" in {
      val red = new Player("red")
      val p1 = Parent_Tile()
      val map = List(List(Tile(p1, red, 1)))
      val c = new GameController(map, List(red), TestCombatStrategy)

      c.startReinforcementPhase()

      c.currentPhase shouldBe GamePhase.Placement
    }
  }

  "endOffenseTurn" should {
    "advance to next player" in {
      val p1 = new Player("red")
      val p2 = new Player("blue")
      val t = Tile(Parent_Tile(), p1, 1)
      val c = new GameController(List(List(t)), List(p1, p2), TestCombatStrategy)

      c.endOffenseTurn()

      c.currentPlayer shouldBe p2
    }

    "trigger reinforcement when round completes" in {
      val p1 = new Player("red");
      p1.infantry = 0
      val p2 = new Player("blue");
      p2.infantry = 0
      val t1 = Tile(Parent_Tile(), p1, 1)
      val t2 = Tile(Parent_Tile(), p2, 1)
      val c = new GameController(List(List(t1, t2)), List(p1, p2), TestCombatStrategy)

      c.currentPlayerIndex = 1
      c.endOffenseTurn()

      p1.infantry should be >= 3
      p2.infantry should be >= 3
      c.currentPhase shouldBe GamePhase.Placement
    }
  }

  "startGame" should {

    "succeed and init players and map on valid input" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(3, List("red", "blue", "green"))

      res.isSuccess shouldBe true
      ctrl.players.map(_.colorName).toSet shouldBe Set("red", "blue", "green")
      ctrl.currentPlayerIndex shouldBe 0
      ctrl.tiles.flatten.length shouldBe imp1.MapInit.createInitialMap().flatten.length
      ctrl.tiles.flatten.forall(_.soldiers == 1) shouldBe true
    }

    "fail if numPlayers < 2" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(1, List("red"))

      res.isFailure shouldBe true
      res.failed.get.getMessage shouldBe "Players must be between 2 and 4"
    }

    "fail if numPlayers > 4" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(5, List("red", "blue", "green", "yellow", "pink"))

      res.isFailure shouldBe true
      res.failed.get.getMessage shouldBe "Players must be between 2 and 4"
    }

    "fail if colors contain duplicates" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(3, List("red", "red", "blue"))

      res.isFailure shouldBe true
      res.failed.get.getMessage shouldBe "cannot have same colors"
    }

    "fail if colors size != numPlayers (too few)" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(3, List("red", "blue"))

      res.isFailure shouldBe true
      res.failed.get.getMessage shouldBe "colors size must equal numPlayers"
    }

    "fail if colors size != numPlayers (too many)" in {
      val ctrl = new GameController(imp1.MapInit.createInitialMap(), Nil)

      val res = ctrl.startGame(2, List("red", "blue", "green"))

      res.isFailure shouldBe true
      res.failed.get.getMessage shouldBe "colors size must equal numPlayers"
    }
  }

  "remainingInfantryPerPlayer" should {
    "return list of (colorName, infantry)" in {
      val p1 = new Player("red");
      p1.infantry = 5
      val p2 = new Player("blue");
      p2.infantry = 10
      val c = newController(List(p1, p2))
      c.players = List(p1, p2)

      val res = c.remainingInfantryPerPlayer

      res should contain allOf(("red", 5), ("blue", 10))
    }
  }

  "allInfantryPlaced" should {
    "be true only if all players have infantry <= 0" in {
      val p1 = new Player("red");
      p1.infantry = 0
      val p2 = new Player("blue");
      p2.infantry = 0
      val c = newController(List(p1, p2))
      c.players = List(p1, p2)

      c.allInfantryPlaced shouldBe true

      p2.infantry = 3
      c.allInfantryPlaced shouldBe false
    }
  }

  "placeinfantry" should {

    "return Left for invalid coordinates" in {
      val player = new Player("red")

      val mapData = List(
        List(Tile(parent = null, player = new Player("empty"), soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = -1, y = 0, n = 1)

      result shouldBe a [Failure[_]]
      val ex = result.failed.get
      ex shouldBe a [IllegalArgumentException]
      ex.getMessage shouldBe "Invalid coordinates."
    }

    "return Left when placing more infantry than the player has" in {
      val player = new Player("red")
      player.infantry = 2

      val emptyOwner = new Player("empty")
      val mapData = List(
        List(Tile(parent = null, player = emptyOwner, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result shouldBe a[Failure[_]]
      val ex = result.failed.get
      ex shouldBe a[IllegalArgumentException]
      ex.getMessage shouldBe "You don't have that many infantry remaining!"
    }

    "return Left when anoher player own this tile" in {
      val player = new Player("red")
      val player2 = new Player("blue")

      val mapData = List(
        List(Tile(parent = null, player = player2, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result shouldBe a[Failure[_]]
      val ex = result.failed.get
      ex shouldBe a[IllegalArgumentException]
      ex.getMessage shouldBe "Another Player owns this Tile!"
    }

    "return Right when movement is Sucessful" in {
      val player = new Player("red")
      player.infantry = 5

      val emptyOwner = new Player("empty")
      val mapData = List(
        List(Tile(parent = null, player = emptyOwner, soldiers = 0))
      )

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 3)

      result.isSuccess shouldBe true

      val newMap = result.toOption.get
      val updatedTile = newMap(0)(0)

      updatedTile.player shouldBe player
      updatedTile.soldiers shouldBe 3

      player.infantry shouldBe 2
    }

    "add infantry on own tile" in {
      val player = new Player("red")
      player.infantry = 5

      val ownTile = Tile(parent = null, player = player, soldiers = 2)
      val mapData = List(List(ownTile))

      val controller = new GameController(mapData, List(player))

      val result = controller.placeInfantry(player, x = 0, y = 0, n = 2)

      result.isSuccess shouldBe true

      val newMap = result.toOption.get
      val updatedTile = newMap(0)(0)

      updatedTile.player shouldBe player
      updatedTile.soldiers shouldBe 4

      player.infantry shouldBe 3
    }
  }

    "GameController.allPlayers" should {
      "return the same list that was passed into the controller" in {
        val p1 = new Player("red")
        val p2 = new Player("blue")
        val players = List(p1, p2)

        val emptyOwner = new Player("empty")
        val mapData = List(
          List(Tile(parent = null, player = emptyOwner, soldiers = 0))
        )

        val controller = new GameController(mapData, players)

        controller.allPlayers shouldBe players
      }

      "GameController.tiles" should {
        "return the same mapData that was passed into the controller (or its current state)" in {
          val p1 = new Player("red")
          val players = List(p1)

          val emptyOwner = new Player("empty")
          val mapData = List(
            List(Tile(parent = null, player = emptyOwner, soldiers = 0))
          )

          val controller = new GameController(mapData, players)

          controller.tiles shouldBe mapData
        }
      }

      "GameController.currentStateName" should {

        "return \"Placement\" initially" in {
          val p1      = new Player("red")
          val players = List(p1)
          val mapData = imp1.MapInit.createInitialMap()

          val ctrl = new GameController(mapData, players, SimpleCombatStrategy)

          ctrl.currentStateName shouldBe "Placement"
        }

        "return \"Offense\" after switching to OffenseState" in {
          val p1      = new Player("red"); p1.infantry = 0
          val p2      = new Player("blue"); p2.infantry = 0
          val players = List(p1, p2)
          val mapData = imp1.MapInit.createInitialMap()

          val ctrl = new GameController(mapData, players, SimpleCombatStrategy)

          ctrl.handleEvent(PlaceInfantryEvent)

          ctrl.currentStateName shouldBe "Offense"
        }
      }
    }
  }
