package TUI

def colorText(text: String, colorName: String): String = {
  val reset = "\u001B[0m"
  val ansiColor = colorName match
    case "red" => "\u001B[31m"
    case "blue" => "\u001B[34m"
    case "yellow" => "\u001B[33m"
    case "green" => "\u001B[32m"
    case _ => "\u001B[37m"
  ansiColor + text + reset
}

