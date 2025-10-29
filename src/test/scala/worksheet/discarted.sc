//  def calvary_to_infantry(n: Int): Unit = {
//    if(cavalry >= n) {
//      cavalry -= n
//      var total = 5 * n
//      infantry += total
//      println("You change " + n + " cavalry for " + total + " infantry!!")
//    } else {
//      println(s"Not enough! You have $cavalry")
//    }
//  }
//
//  def artillery_to_infantry(n: Int): Unit = {
//    if (artillery > n) {
//      artillery -= n
//      var total = 10 * n
//      infantry += total
//      println("You change " + n + " artillery for " + total + " infantry!!")
//    } else {
//      println(s"Not enough! You have $artillery")
//    }
//  }
//
//  def infantry_to_cavalry(n: Int): Unit = {
//    if (infantry >= 5) {
//      cavalry += n
//      var total = 5 * n
//      infantry -= total
//      println("You change " + total + " infantry for " + n + " cavalry!!")
//    } else {
//      println(s"Not enough! You have $cavalry")
//    }
//  }
//
//  def infantry_to_artillery(n: Int): Unit = {
//    if (infantry >= 10) {
//      artillery += n
//      var total = 10 * n
//      infantry -= total
//      println("You change " + total + " infantry for " + n + " artillery!!")
//    } else {
//      println(s"Not enough! You have $cavalry")
//    }
//  }

//    def start_game(game: game): Unit = {
//      println("Welcome to Risk!")
//      println("How many players are gonna play? (min 2,limit 2)")
//      var TotalPlayers = StdIn.readInt()
//      players = TotalPlayers
//      select_color()
//
//    }

//    def select_color(): Unit = {
//      println("Select a color (red,blue,yellow,green): ")
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
//      println(s"You selected $colorInput!")
//      Player.color = colorInput
//    }