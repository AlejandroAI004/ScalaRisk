package TUI

import model.player
import controller.*
import model.playerList

object ConsoleView {
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

  def start(): playerList = {
    val numPlayers = askPlayerCount()
    var playersList = new playerList()
    for (i <- 1 to numPlayers) {
      val color = askPlayerColor(i, playersList.usedColors())
      playersList = playersList.addPlayer(color)
    }
    println(playersList.toString())
    playersList
  }
  
  def askPlayerCount(): Int = {
    println("How many players are gonna play? (min 2,limit 4)")
    scala.io.StdIn.readInt()
  }
  
  def askPlayerColor(playerNum: Int, usedColors: List[String]): String = {
    var color = "grey"
    var valid = false
    while (!valid) {
      println(s"Select a color for Player $playerNum (red, blue, yellow, green):")
      val input = scala.io.StdIn.readLine().toLowerCase()
      if (!List("red", "blue", "yellow", "green").contains(input)) {
        println("Unknown color, try again!")
      } else if (usedColors.contains(input)) {
        println("That color is taken!")
      } else {
        color = input
        valid = true
      }
    }
    color
  }

  def printPlayersList(playersList: List[player]): Unit = {
    println("List of players: ")
    for ((p, idx) <- playersList.zipWithIndex) {
      println(s"Player ${idx + 1} -> ${p.colorName} | Infantry: ${p.infantry}")
    }
  }

  def showPlacementResult(result: InfantryPlacementResult): Unit = result match {
    case allValid => println("Success: Infantry placed!")
    case Success => println("Success: Infantry placed!")
    case InvalidInput(msg) => println("Error: " + msg)
    case TileOccupied(msg) => println("Error: " + msg)
  }
}
