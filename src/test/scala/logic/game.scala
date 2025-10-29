package logic
import scala.io.StdIn

class Player(val name: String, val colorName: String):
  var infantry: Int = 20

object game {
  def main(args: Array[String]): Unit = {
    println("How many players are gonna play? (min 2,limit 4)")
    val TotalPlayers = StdIn.readInt()
    var playersList = Array[Player]()

    for i <- 0 until TotalPlayers do {
      println(s"Enter name for Player ${i + 1}:")
      val name = StdIn.readLine()

      var valid = false
      var colorName = "gray"

      while !valid do {
        println("Select a color (red, blue, yellow, green):")
        val input = scala.io.StdIn.readLine().toLowerCase()
        input match {
          case "red" | "blue" | "yellow" | "green" =>
            colorName = input
            valid = true
          case _ => println("Unknown color, try again!")
        }
        if playersList.exists(p => p.colorName == colorName) then {
          println("that color is taken!")
          valid = false
        }
      }

      playersList = playersList :+ new Player(name, colorName)
      println(s"${colorText(name,colorName)} has selected ${colorText(colorName,colorName)}" +
        s" and has 20 infantry!")
    }

    println("List of players: ")
    for p <- playersList do {
      println(s"${colorText(p.name,p.colorName)} -> ${colorText(p.colorName,p.colorName)} " +
        s"| Infantry: ${p.infantry}")
    }
  }

}

//def reinforcement_phase(): Unit = {
//
//    }
//
//    def attack_phase(): Unit = {
//
//    }
//
//    def fortify_phase(): Unit = {
//
//    }
