package TUI

import model.{Tile, colorText, player}
import controller.*

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

  def showPlacementResult(result: InfantryPlacementResult): String = result match {
    case Success => "Success: Infantry placed!"
    case InvalidInput(msg) => "Error: " + msg
    case TileOccupied(msg) => "Error: " + msg
  }

  def mapString(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def getXCoordinate(player: player): Int = {
    println(s"\n${colorText(player.colorName, player.colorName)}, you have ${player.infantry} infantry to place.")
    println(s"Remaining infantry: ${player.infantry}")
    println("Enter X coordinate (0 to X):")
    scala.io.StdIn.readInt()
  }

  def getYCoordinate(player: player): Int = {
    println("Enter Y coordinate (0 to Y):")
    scala.io.StdIn.readInt()
  }

  def getInfantryCount(player: player): Int = {
    println("How many infantry to place here?")
    scala.io.StdIn.readInt()
  }

}
