package TUI

import model.{Tile, colorText, player, playerList, updateTile}
import controller.*
import controller.Map_Generation.print_map

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
    var n = scala.io.StdIn.readInt()
    (x,y,n)
  }

  def showPlayers(playerList: playerList): String = {
    playerList.toString
  }

  def showTileMap(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def showStatus(msg: String): Unit = println(msg)

  def showPlacementResult(result: InfantryPlacementResult, mapData: List[List[Tile]]): String = result match {
    case Success => "Success: Infantry placed!"
    case InvalidInput(msg) => "Warning:" + msg
    case TileOccupied(msg) =>
      "Warning: " + msg + "\n" + Map_Generation.print_map(mapData)
    case controller.allValid(_) => "unknow status"
  }

  def mapString(mapData: List[List[Tile]]): String = {
    Map_Generation.print_map(mapData)
  }

  def placeInfantry(players: List[player],
                    cols: Int,
                    rows: Int,
                    mapData: List[List[Tile]],
                    getX: () => Int,
                    getY: () => Int,
                    getN: () => Int
                   ): List[List[Tile]] =
    var tempMapData = mapData
    var currentPlayer = 0

    while (players.exists(_.infantry > 0)) {
      val player = players(currentPlayer)
      if (player.infantry > 0) {
        var validMove = false
        while (!validMove) {
          println(s"\n${colorText(player.colorName, player.colorName)}, you have ${player.infantry} infantry to place.")
          println(s"Remaining infantry: ${player.infantry}")
          println("Enter X coordinate (0 to " + (cols - 1) + "):")
          val x = getX()
          println("Enter Y coordinate (0 to " + (rows - 1) + "):")
          val y = getY()
          println("How many infantry to place here?")
          val n = getN()

          if (x < 0 || x >= cols || y < 0 || y >= rows) {
            println("Invalid coordinates! Try again.")
          } else if (n > player.infantry) {
            println("You don't have that many infantry remaining!")
          } else if (tempMapData(y)(x).player != player && tempMapData(y)(x).player.colorName != "empty") {
            println("Another Player owns this Tile! Try again.")
            print(print_map(tempMapData))

          } else {
            val oldRow = tempMapData(y)
            val newRow = oldRow.updated(x, updateTile(player, n, oldRow(x)))
            tempMapData = tempMapData.updated(y, newRow)
            player.infantry -= n

            if (!player.ownedTiles.contains(tempMapData(x)(y))) {
              player.ownedTiles = player.ownedTiles :+ tempMapData(x)(y)
            }

            validMove = true
            print(print_map(tempMapData))
          }
        }
      }
      currentPlayer = (currentPlayer + 1) % players.length
    }
     tempMapData
}