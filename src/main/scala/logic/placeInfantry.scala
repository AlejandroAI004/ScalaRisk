package logic

import TUI.*
import TUI.Map_Generation.print_map

import scala.io.StdIn.readInt

def placeInfantry(players: List[player], cols: Int, rows: Int,
                  mapData: List[List[Tile]]
                 ): List[List[Tile]] =
  var tempMapData = mapData
  var currentPlayer = 0

  while (players.exists(_.infantry > 0)) {
    var player = players(currentPlayer)
    if (player.infantry > 0) {
      var validMove = false
      while (!validMove) {
        println(s"\n${colorText(player.colorName, player.colorName)}, you have ${player.infantry} infantry to place.")
        println(s"Remaining infantry: ${player.infantry}")
        println("Enter X coordinate (0 to " + (cols - 1) + "):")
        val x = readInt()
        println("Enter Y coordinate (0 to " + (rows - 1) + "):")
        val y = readInt()
        println("How many infantry to place here?")
        val n = readInt()

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

          if(!player.ownedTiles.contains(tempMapData(x)(y))) {
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