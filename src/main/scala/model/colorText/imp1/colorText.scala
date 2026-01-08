package model.colorText.imp1

import model.colorText.colorTextPort

object colorText extends colorTextPort {

  private val Reset = "\u001B[0m" 

  override def colorText(text: String, colorName: String): String = {
    val ansiColor = colorName match
      case "red"   => "\u001B[31m"
      case "blue"  => "\u001B[34m"
      case "pink"  => "\u001B[38;5;206m"
      case "green" => "\u001B[32m"
      case _       => "\u001B[37m"
    ansiColor + text + Reset
  }
}

