package logic

import TUI.*

import scala.io.StdIn.readInt

def updateTile(player: player, n: Int, tile: Tile): Tile = {
    return new Tile(tile.parent, player.colorName, tile.soldiers + n)
}