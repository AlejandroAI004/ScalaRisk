package Tests

import model.{player, playerList}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class playerList_spec extends AnyWordSpec with Matchers {
  "playerList.toList" should {

    "return an empty list for a new playerList" in {
      val pl = new playerList

      pl.toList shouldBe empty
    }
  }
  "toString" should {
    "format all players with color and infantry correctly" in {
      val p1 = new player("blue")
      val p2 = new player("red")
      val playerList = new playerList(List(p1,p2))
      val expected = {
        "List of players: \n" +
          "Player 1 -> blue | Infantry: 20\n" +
          "Player 2 -> red | Infantry: 20\n"

      }
      stripAnsi(playerList.toString()) should be(expected)
    }
  }
  "usedColors" should {
    "return all colors correctly" in {
      val p1 = new player("blue")
      val p2 = new player("red")
      val playerList = new playerList(List(p1, p2))
      playerList.usedColors() should contain allOf("blue", "red")
    }
  }
  "addPlayer" when {
    "initialized with player object" should {
      "return a player List with added player" in {
        val p1 = new player("blue")
        var playerList = new playerList()
        playerList = playerList.addPlayer(p1)
        val expected = {
          "List of players: \n" +
            "Player 1 -> blue | Infantry: 20\n"
        }
        stripAnsi(playerList.toString()) should be(expected)
      }
    }
    "initialized with color" should {
      "return a player List with added player" in {
        var playerList = new playerList()
        playerList = playerList.addPlayer("blue")
        val expected = {
          "List of players: \n" +
            "Player 1 -> blue | Infantry: 20\n"
        }
        stripAnsi(playerList.toString()) should be(expected)
      }
    }

  }



  def stripAnsi(str: String): String =
    str.replaceAll("\u001B\\[[;\\d]*m", "")
}
