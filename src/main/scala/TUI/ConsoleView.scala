package TUI

import model.*
import controller.*

import scala.annotation.tailrec

object ConsoleView {
  def welcome(): String = {
    "*** Welcome to Risk! ***\n" +
      "Bei Risk kämpfst du um die Weltherrschaft! \n" +
      "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
      "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
      "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
      "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
      "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
      "–, gewinnt das Spiel und herrscht über die Welt!\n"
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

  def askForInfantryPlacement(player: player): (Int, Int, Int) = {
    println(s"\n${colorText(player.colorName, player.colorName)}, you have ${player.infantry} infantry to place.")
    println(s"Remaining infantry: ${player.infantry}")
    println(s"Enter X coordinate (0 to 1):")
    var x = scala.io.StdIn.readInt()
    println(s"Enter Y coordinate (0 to 1):")
    var y = scala.io.StdIn.readInt()
    println("How many infantry to place here?")
    var n = readInfatry()
    (x, y, n)
  }
  @tailrec
  private def readInfatry(): Int = {
    val n = scala.io.StdIn.readInt()
    if (n > 0) n
    else {
      println("You cant place 0 infantry!")
      readInfatry()
    }
  }

  @tailrec
  def placeInfantryFunctional(
                               players: List[player],
                               mapData: List[List[Tile]],
                               controller: GameController
                             ): List[List[Tile]] = {
      if (players.forall(_.infantry <= 0))
      mapData
    else {
      val player = players.head
      if (player.infantry > 0) {
        val (x, y, n) = ConsoleView.askForInfantryPlacement(player)
        controller.placeInfantry(player, x, y, n) match {
          case Right(mapData) =>
            print(ConsoleView.showTileMap(mapData))
            placeInfantryFunctional(players.tail :+ player, mapData, controller)
          case Left(msg) =>
            ConsoleView.showStatus(msg)
            print(ConsoleView.showTileMap(mapData))
            placeInfantryFunctional(players, mapData, controller)
        }
      } else {
        placeInfantryFunctional(players.tail :+ player, mapData, controller)
      }
    }
  }

  def showPlayers(playerList: playerList): String = {
    playerList.toString
  }

  def showTileMap(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def showStatus(msg: String): Unit = println(msg)

  def mapString(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }
}