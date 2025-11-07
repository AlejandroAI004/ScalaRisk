package logic

import TUI.*
import TUI.Map_Generation.print_map

import scala.io.StdIn.readInt

def placeInfantry(player: player, cols: Int, rows: Int,
                  mapData: List[List[Tile]]
                 ): List[List[Tile]] =
  println(s"\n${player.name}, you have ${player.infantry} infantry to place.")
  var tempMapData = mapData
  while player.infantry > 0 do {
    println(s"Remaining infantry: ${player.infantry}")
    println("Enter X coordinate (0 to 1):")
    val x: Int = readInt()
    println("Enter Y coordinate (0 to 1):")
    val y: Int = readInt()
    println("How many infantry to place here?")
    val n = readInt()

    if x < 0 || x >= cols || y < 0 || y >= rows then
      println("Invalid coordinates! Try again.")
      if n > player.infantry then
        println("You don't have that many infantry remaining!")
    else if (mapData(y)(x).owner != player.colorName && mapData(y)(x).owner != "empty")
      println("Another Player owns this Tile!")
    else
      val oldRow = tempMapData(y)
      val newRow = oldRow.updated(x, updateTile(player, n, oldRow(x)))
      val newMapData = tempMapData.updated(y, newRow)
      tempMapData = newMapData
      player.infantry -= n
    
    print(print_map(tempMapData))
  }
  return tempMapData