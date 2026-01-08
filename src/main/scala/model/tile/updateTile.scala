package model.tile

import model.*
import model.player.Player

def updateTile(player: Player, n: Int, tile: Tile): Tile = {
    Tile(tile.parent, player, tile.soldiers + n)
}