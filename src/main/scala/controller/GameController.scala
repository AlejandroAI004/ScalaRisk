package controller
import model.*

class GameController(var mapData: List[List[Tile]], var players: List[player]) {
  def placeInfantry(
                     player: player, x: Int, y: Int, n: Int
                   ): Either[String, List[List[Tile]]] = {
    if (x < 0 || x >= mapData.head.length || y < 0 || y >= mapData.length)
      Left("Invalid coordinates.")
    else if (n > player.infantry)
      Left("You don't have that many infantry remaining!")
    else if (mapData(y)(x).player != player && mapData(y)(x).player.colorName != "empty") {
      Left("Another Player owns this Tile!")
    } else {
      val updated = updateTile(player, n, mapData(y)(x))
      val newRow = mapData(y).updated(x, updated)
      mapData = mapData.updated(y, newRow)
      player.infantry -= n
      Right(mapData)
    }
  }

  def ofense_phase(player: player, x: Int, y: Int, n: Int):
                    Either[String,List[List[Tile]]] = {
    if (x < 0 || x >= mapData.head.length || y < 0 || y >= mapData.length)
      Left("Invalid coordinates.")
    else {
      val targetTile = mapData(y)(x)
      if (targetTile.player == player || targetTile.player.colorName == "empty")
        Left("You can't attack your own or empty tile!")
      else if (n >= player.infantry)
        Left("You must leave at least one infantry on your tile!")
      else {
        val updatedTile = updateTile(player, n, targetTile)
        val newRow = mapData(y).updated(x, updatedTile)
        mapData = mapData.updated(y, newRow)
        player.infantry -= n
        Right(mapData)
      }
    }
  }


  def allPlayers: List[player] = players
  def tiles: List[List[Tile]] = mapData
}