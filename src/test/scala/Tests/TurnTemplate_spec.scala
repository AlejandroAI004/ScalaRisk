package Tests

import model.*
import controller.*
import controller.GameController.GameControllerPort
import controller.GameController.impl1.GameController
import model.Combat.CombatStrategy.SimpleCombatStrategy
import model.mapInit.imp1.MapInit
import model.player.Player
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import util.pattern.TurnTemplate

class TurnTemplate_spec extends AnyWordSpec with Matchers {

  "TurnTemplate.executeTurn" should {

    "call preTurn, doTurn and postTurn in order" in {
      var trace: List[String] = Nil

      class TestTurn extends TurnTemplate {
        override def preTurn(p: Player, c: GameControllerPort): Unit =
          trace = trace :+ "pre"

        override def doTurn(p: Player, c: GameControllerPort): Unit =
          trace = trace :+ "do"

        override def postTurn(p: Player, c: GameControllerPort): Unit =
          trace = trace :+ "post"
      }

      val dummyPlayer = new Player("red")
      val mapData     = MapInit.createInitialMap()
      val players     = List(dummyPlayer)
      val ctrl        = new GameController(mapData, players, SimpleCombatStrategy)

      new TestTurn().executeTurn(dummyPlayer, ctrl)

      trace shouldBe List("pre", "do", "post")
    }
  }
}