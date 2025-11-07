package Tests

import TUI.*
import logic.*
import logic.startgame.{print_playersList, welcome}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class startgame_spec extends AnyWordSpec with Matchers {
  "print_playersList " when {
    "called with one player " should {
      val name = "name"
      val color = "red"
      val Player = new player(name, color)
      var playersList = Array[player]()
      playersList = playersList :+ Player
      "return a List with one Player " in {
        print_playersList(playersList) should be ("List of players: \n" +
          s"${colorText(Player.name, Player.colorName)} -> ${colorText(Player.colorName, Player.colorName)} " +
          s"| Infantry: ${Player.infantry}\n"
        )
      }
    }
  }
  "welcome" when {
    "called " should {
      "return a welcome message " in {
        welcome() should be("*** Welcome to Risk! ***\n" +
          "Bei Risk kämpfst du um die Weltherrschaft! \n" +
          "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
          "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
          "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
          "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
          "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
          "–, gewinnt das Spiel und herrscht über die Welt!\n" +
          "Spiel starten?[y]\n"
        )
      }
    }
  }
}

