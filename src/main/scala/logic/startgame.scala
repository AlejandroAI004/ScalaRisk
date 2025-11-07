package logic

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

  def start(): Array[player] = {
    var playersList = Array[player]()
    println("How many players are gonna play? (min 2,limit 4)")
    val TotalPlayers = StdIn.readInt()

    for i <- 0 until TotalPlayers do {
      println(s"Enter name for Player ${i + 1}:")
      val name = StdIn.readLine()

      var valid2 = false
      var colorName = "gray"

      while !valid2 do {
        println("Select a color (red, blue, yellow, green):")
        val input = scala.io.StdIn.readLine().toLowerCase()
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

      playersList = playersList :+ new player(name, colorName)
      println(s"${colorText(name, colorName)} has selected ${colorText(colorName, colorName)}" +
        s" and has 20 infantry!")
    }
    
    return playersList
  }
  
  def print_playersList (playersList: Array[player]): String = {
    var output = "List of players: \n"
    for p <- playersList do {
      output += s"${colorText(p.name, p.colorName)} -> ${colorText(p.colorName, p.colorName)} " +
        s"| Infantry: ${p.infantry}\n"
    }
    return output
  }

}
