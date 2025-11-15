package Tests

import TUI.ConsoleView.welcome
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class consoleView_spec extends AnyWordSpec with Matchers {
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
      welcome() should be(expected2)
    }
  }
}
