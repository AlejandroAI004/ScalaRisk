package controller.Map_Generation

import model.tile.Tile

trait Map_GenerationPort {
  def print_row(tiles: List[Tile]): String
  def print_map(data: List[List[Tile]]): String
}