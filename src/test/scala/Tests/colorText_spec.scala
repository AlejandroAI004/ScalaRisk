package Tests

import model.mapInit.impl.MapInit.createInitialMap
import controller.Map_Generation.imp1.Map_Generation.{print_map, print_row}
import model.colorText.impl.colorText
import model.tile.{Parent_Tile, Tile, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class colorText_spec extends AnyWordSpec with Matchers {
  "A text " when {
    "initialized with color red " should {
      val test = "test"
      "print red text" in {
        colorText.colorText(test, "red") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color blue " should {
      val test = "test"
      "print blue text" in {
        colorText.colorText(test, "blue") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color green " should {
      val test = "test"
      "print green text" in {
        colorText.colorText(test, "green") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
    "initialized with color pink " should {
      val test = "test"
      "print yellow text" in {
        colorText.colorText(test, "pink") should be
        "\u001B[38;5;206m" + test + "\u001B[0m"
      }
    }
    "initialized with no color " should {
      val test = "test"
      "print no text" in {
        colorText.colorText(test, "") should be
        "\u001B[31m" + test + "\u001B[0m"
      }
    }
  }
}
