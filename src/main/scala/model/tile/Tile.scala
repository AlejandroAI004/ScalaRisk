package model.tile

import model.player.Player

case class Tile(parent: Parent_Tile, player: Player = new Player("empty"), soldiers: Int = 0) {}
