package logic
import models.Army
import models.Player
import java.awt._
import javax._

class game {
    def start_game(game: game): Unit = {
      println()
    }

    def select_color(): Unit = {
      println("Select a color (red,blue,yellow,green): ")
      var valid = false
      var colorInput: Color = Color.gray
      
      while !valid do {
        val input = scala.io.StdIn.readLine().toLowerCase()
        input match {
          case "red" => colorInput = Color.RED; valid = true
          case "blue" => colorInput = Color.BLUE; valid = true
          case "yellow" => colorInput = Color.YELLOW; valid = true
          case "green" => colorInput = Color.GREEN; valid = true
          case _ => println("Unknown color, try again!")
        }
      }
        
      println(s"You selected $colorInput!")
      Player.color = colorInput
    }

    def reinforcement_phase(): Unit = {

    }

    def attack_phase(): Unit = {

    }

    def fortify_phase(): Unit = {

    }
}
