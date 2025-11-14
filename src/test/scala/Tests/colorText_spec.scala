package Tests

import model.MapInit.testMap_init
import controller.Map_Generation.{print_map, print_row}
import model.{Parent_Tile, Tile, colorText, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class colorText_spec extends AnyWordSpec with Matchers {
  "A text " when {
    "initialized with color red " should {
      val test = "test"
      "print red text" in {
        colorText(test, "red") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color blue " should {
      val test = "test"
      "print blue text" in {
        colorText(test, "blue") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color green " should {
      val test = "test"
      "print green text" in {
        colorText(test, "green") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color yellow " should {
      val test = "test"
      "print yellow text" in {
        colorText(test, "yellow") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with no color " should {
      val test = "test"
      "print no text" in {
        colorText(test, "") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
  }
}
