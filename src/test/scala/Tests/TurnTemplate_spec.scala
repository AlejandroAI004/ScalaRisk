package Tests
import model.*
import controller.*

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class TurnTemplate_spec extends AnyWordSpec with Matchers {

  "TurnTemplate.executeTurn" should {

    "call preTurn, doTurn and postTurn in order" in {
      var trace: List[String] = Nil

      class TestTurn extends TurnTemplate {
        override protected def preTurn(p: Player, c: GameController): Unit =
          trace = trace :+ "pre"

        override protected def doTurn(p: Player, c: GameController): Unit =
          trace = trace :+ "do"

        override protected def postTurn(p: Player, c: GameController): Unit =
          trace = trace :+ "post"
      }

      val dummyPlayer = new Player("red")

      val mapData  = MapInit.testMap_init()
      val players  = List(dummyPlayer)
      val ctrl     = new GameController(mapData, players, SimpleCombatStrategy)

      val turn = new TestTurn
      turn.executeTurn(dummyPlayer, ctrl)

      trace shouldBe List("pre", "do", "post")
    }
  }
}