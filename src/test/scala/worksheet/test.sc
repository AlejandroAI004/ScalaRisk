
import java.awt.Color
import scala.io.StdIn

//    println("How many players are gonna play? (min 2,limit 2)")
//    val TotalPlayers = StdIn.readInt()
//
//    val availableColors = Array(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN)
//
//    var playersList = Array[Player]()
//
//    for i <- 0 until TotalPlayers do {
//      println(s"Enter name for Player ${i + 1}:")
//      val name = StdIn.readLine()
//
//      var valid = false
//      var colorInput: Color = Color.gray
//
//      while !valid do {
//        val input = scala.io.StdIn.readLine().toLowerCase()
//        input match {
//          case "red" => colorInput = Color.RED; valid = true
//          case "blue" => colorInput = Color.BLUE; valid = true
//          case "yellow" => colorInput = Color.YELLOW; valid = true
//          case "green" => colorInput = Color.GREEN; valid = true
//          case _ => println("Unknown color, try again!")
//        }
//      }
//
//      playersList = playersList :+ new Player(name, colorInput)
//      println(s"$name has selected $colorInput!")
//
//      println("List of players: ")
//      for p <- playersList do {
//        println(s"${p.name} -> ${p.color}")
//      }
//    }
