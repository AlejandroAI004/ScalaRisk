package logic
import models.Army
import scalafx.scene.paint.Color

class game {
    def start_game(game: game): Unit = {
      
    }

    def select_color(): Unit = {
      println("Select a color: ")
      var input = scala.io.StdIn.readLine().toLowerCase()
      var color = input match {
        case "red" => Color.red
      }

    }

    def reinforcement_phase(): Unit = {

    }

    def attack_phase(): Unit = {

    }

    def fortify_phase(): Unit = {

    }
}
