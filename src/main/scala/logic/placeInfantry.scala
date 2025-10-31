package logic

import scala.io.StdIn.readInt

def placeInfantry(player: player, cols: Int, rows: Int,
                  field: scala.collection.mutable.Map[(Int, Int), List[(player, Int)]]
                 ): Unit =
  println(s"\n${player.name}, you have ${player.infantry} infantry to place.")
  while player.infantry > 0 do {
    println(s"Remaining infantry: ${player.infantry}")
    println("Enter X coordinate (0 to 3):")
    val x = readInt()
    println("Enter Y coordinate (0 to 3):")
    val y = readInt()
    println("How many infantry to place here?")
    val n = readInt()

    if x < 0 || x >= cols || y < 0 || y >= rows then
      println("Invalid coordinates! Try again.")
    else if n > player.infantry then
      println("You don't have that many infantry remaining!")
    else
      val existingUnits = field.getOrElse((x, y), List())
      val updated = existingUnits.find(_._1 == player) match
        case Some((_, oldN)) =>
          existingUnits.map {
            case (p, num) if p == player => (p, num + n)
            case other => other
          }
        case None =>
          (player, n) :: existingUnits

      field((x, y)) = updated
      player.infantry -= n

      println("\nCurrently field:")
      println(generateField(cols, rows, field))
  }