package model

import TUI.colorText

import scala.io.StdIn

object startgame {
  
  def welcome(): String = {
    "*** Welcome to Risk! ***\n" +
    "Bei Risk kämpfst du um die Weltherrschaft! \n" +
    "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
    "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
    "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
    "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
    "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
    "–, gewinnt das Spiel und herrscht über die Welt!\n" +
    "Spiel starten?[y]\n"
  }

  def start(getInt: () => Int, getLine: () => String): List[player] = {
    var playersList = List[player]()
    println("How many players are gonna play? (min 2,limit 4)")
    val TotalPlayers = getInt()

    for i <- 0 until TotalPlayers do {
      var valid2 = false
      var colorName = "gray"
      while !valid2 do {
        println(s"Select a color for Player ${i + 1} (red, blue, yellow, green):")
        val input = getLine().toLowerCase()
        input match {
          case "red" | "blue" | "yellow" | "green" =>
            colorName = input
            valid2 = true
          case _ => println("Unknown color, try again!")
        }
        if playersList.exists(p => p.colorName == colorName) then {
          println("that color is taken!")
          valid2 = false
        }
      }
        val no = playersList.length + 1
        playersList = playersList :+ new player(colorName)
        println(s"${colorText(s"Player $no:",colorName)} has selected ${colorText(colorName, colorName)}")
    }
     playersList
  }

  def print_playersList (playersList: List[player]): String = {
    var output = "List of players: \n"
    for i <- playersList.indices do {
      var p = playersList(i)
      output += s"${colorText(s"Player ${i + 1}" , p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}\n"
    }
     output
  }

}
