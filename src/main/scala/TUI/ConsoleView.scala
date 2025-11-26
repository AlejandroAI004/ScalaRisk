package TUI

import model.*
import controller.*

import scala.annotation.tailrec

object ConsoleView extends Observer{
  private var controller: GameController = _

  def init(ctrl: GameController): Unit = {
    controller = ctrl
    controller.addObserver(this)
  }

  override def update(): Unit = {
    val map = controller.tiles
    println(showTileMap(map))
  }
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
      println(s"Select a color for Player $playerNum (red, blue, pink, green):")
      val input = scala.io.StdIn.readLine().toLowerCase()
      if (!List("red", "blue", "pink", "green").contains(input)) {
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
    var x = readIntSafe("Enter X coordinate (0 to 1):")
    var y = readIntSafe("Enter Y coordinate (0 to 1):")
    var n = readIntSafe("How many infantry to place here?")
    (x, y, n)
  }

  def askForOffenseMove(player: player): (Int, Int, Int, Int, Int) = {
    println(s"\n${colorText(player.colorName, player.colorName)}, choose your attack:")
    val fromX = readIntSafe("Enter FROM X coordinate:")
    val fromY = readIntSafe("Enter FROM Y coordinate:")
    val toX = readIntSafe("Enter TO   X coordinate:")
    val toY = readIntSafe("Enter TO   Y coordinate:")
    val n = readIntSafe("How many infantry to attack with?")
    (fromX, fromY, toX, toY, n)
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
        val (x, y, n) = askForInfantryPlacement(player)
        controller.placeInfantry(player, x, y, n) match {
          case Right(mapData) =>
            placeInfantryFunctional(players.tail :+ player, mapData, controller)
          case Left(msg) =>
            showStatus(msg)
            print(showTileMap(mapData))
            placeInfantryFunctional(players, mapData, controller)
        }
      } else {
        placeInfantryFunctional(players.tail :+ player, mapData, controller)
      }
    }
  }

  @tailrec
  def offense_phaseFunctional(players: List[player],
                              mapData: List[List[Tile]],
                              controller: GameController
                             ): List[List[Tile]] = {
    val anyCanAttack =
      mapData.exists(row => row.exists(t => t.player.colorName != "empty" && t.soldiers > 1))

    if (!anyCanAttack) {
      mapData
    } else {
      val player = players.head

      // prüfen, ob dieser Spieler überhaupt irgendwo ein angreifbares Feld hat
      val playerCanAttack =
        mapData.exists(row => row.exists(t => t.player == player && t.soldiers > 1))

      if (playerCanAttack) {
        val (fromX, fromY, toX, toY, n) = askForOffenseMove(player)

        controller.offense_phase(player, fromX, fromY, toX, toY, n) match {
          case Right(newMap) =>
            offense_phaseFunctional(players.tail :+ player, newMap, controller)

          case Left(msg) =>
            showStatus(msg)
            print(showTileMap(mapData))
            offense_phaseFunctional(players, mapData, controller)
        }
      } else {
        offense_phaseFunctional(players.tail :+ player, mapData, controller)
      }
    }
  }
  
  def showPlayers(playerList: playerList): String = {
    playerList.toString
  }

  def showTileMap(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def showStatus(msg: String): String =  {
    println(msg)
    msg
  }
  
  @tailrec
  def readIntSafe(prompt: String): Int = {
    println(prompt)
    try {
      scala.io.StdIn.readInt()
    } catch {
      case _: NumberFormatException =>
        println("Bitte gib eine gültige Zahl ein!")
        readIntSafe(prompt)
    }
  }
}