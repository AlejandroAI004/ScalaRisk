package Tests

import TUI.ConsoleView
import model.*
import controller.*
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class startgame_spec extends AnyWordSpec with Matchers{

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

  
}
