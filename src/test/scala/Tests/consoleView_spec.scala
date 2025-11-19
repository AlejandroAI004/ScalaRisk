package Tests

import TUI.*
import TUI.ConsoleView.{showPlayers, showStatus, showTileMap, welcome}
import model.*
import controller.*
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class consoleView_spec extends AnyWordSpec with Matchers {
    "welcome" should {
      "print rules" in {
        var expect = "*** Welcome to Risk! ***\n" +
          "Bei Risk kämpfst du um die Weltherrschaft! \n" +
          "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
          "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
          "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
          "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
          "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
          "–, gewinnt das Spiel und herrscht über die Welt!\n"
        welcome() should be(expect)
      }
    }

    "showplayers" should {
      "return the same string as playerList.toString" in {
        val p1 = new player("red")
        val p2 = new player("blue")

        val players = new playerList()
          .addPlayer(p1)
          .addPlayer(p2)

        var result = showPlayers(players)

        result shouldBe players.toString
      }
    }

    "showTileMap" should {
      "print the actual map" in {
        val p1 = new player("red")
        val p2 = new player("blue")

        val parent1 = Parent_Tile()
        val parent2 = Parent_Tile()

        val tile1 = Tile(parent1, p1, 3)
        val tile2 = Tile(parent2, p2, 2)

        val mapData = List(
          List(tile1, tile2)
        )

        val expected = Map_Generation.print_map(mapData)
        val result = showTileMap(mapData)

        result shouldBe expected
      }
    }

    "showStatus" should {
      "should print the actual status of the selected state" in {
        val msg = "Error: Invalid movee"

        val result = showStatus(msg)

        result shouldBe msg
      }
    }

}
