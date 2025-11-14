package Tests

import TUI.ConsoleView
import model.*
import controller.*
import controller.StartGameController.print_playersList
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class startgame_spec extends AnyWordSpec with Matchers{
  "print_playerList" should {
    "format all players with color and infantry correctly" in {
      val p1 = new player("blue")
      val p2 = new player("red")
      val playerList = List(p1,p2)
      val expected = {
        "List of players: \n" +
        "Player 1 -> blue | Infantry: 20\n" +
        "Player 2 -> red | Infantry: 20\n"

      }
      stripAnsi(print_playersList(playerList)) should be(expected)
    }
  }

  "welcome" should {
    "return the correct welcome string" in {
      val expected2 =
        "*** Welcome to Risk! ***\n" +
          "Bei Risk kämpfst du um die Weltherrschaft! \n" +
          "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
          "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
          "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
          "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
          "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
          "–, gewinnt das Spiel und herrscht über die Welt!\n" +
          "Spiel starten?[y]\n"
      ConsoleView.welcome() should be(expected2)
    }
  }

//  "start" should {
//    "return the list with number of players and colors" in {
//      val scriptedInts = Iterator(2)
//      val scriptedLines = Iterator("blu","blue","blue","red")
//
//      val playersTest = StartGameController.start(
//        () => scriptedInts.next(),
//        () => scriptedLines.next()
//      )
//      playersTest.length should be(2)
//      playersTest.map(_.colorName) should be(List("blue", "red"))
//    }
//  }

  def stripAnsi(str: String): String =
    str.replaceAll("\u001B\\[[;\\d]*m", "")
}
