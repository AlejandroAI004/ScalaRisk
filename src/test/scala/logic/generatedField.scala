package logic

import scala.collection.mutable

def generateField(C: Int, R: Int, field: mutable.Map[(Int, Int), List[(player, Int)]]): String =
  val cellWidth = 10
  var output = ""

  for y <- 0 until R do
    output += "*" + ("-" * cellWidth + "*") * C + "\n"
    for line <- 0 until 2 do
      for x <- 0 until C do
        val units = field.getOrElse((x, y), List())
        val content = if units.isEmpty then
          " " * cellWidth
        else
          val (player, n) = units.head
          val nameStr = player.name.take(4)
          val numStr = n.toString
          s"${colorText(nameStr, player.colorName)}:${colorText(numStr, player.colorName)}    ".padTo(cellWidth, ' ')
        output += "|" + content
      output += "|\n"
  output += "*" + ("-" * cellWidth + "*") * C + "\n"
  output
