package logic

import scala.collection.mutable
object TestRisk {
  def risk(): Unit =
    val cols = 4
    val rows = 4
    println(s"Creating a $cols x $rows field")
    
    val allPlayers = startgame.playersList.map(p => new player(p.name,p.colorName))
    val players = allPlayers

    val field = mutable.Map[(Int, Int), List[(player, Int)]]()

    for p <- players do
      placeInfantry(p, cols, rows, field)

    println("\nFinal field:") 
    println (generateField(cols, rows, field))


}
