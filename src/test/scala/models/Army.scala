package models

class Army {
  var infantry = 10
  var cavalry = 5
  var artillery = 1

  def myArmy: Array[Int] = Array(infantry,cavalry,artillery)

  def calvary_to_infantry(n: Int): Unit = {
    if(cavalry >= n) {
      cavalry -= n
      var total = 5 * n
      infantry += total
      println("You change " + n + " cavalry for " + total + " infantry!!")
    } else {
      println(s"Not enough! You have $cavalry")
    }
  }

  def artillery_to_infantry(n: Int): Unit = {
    if (artillery > n) {
      artillery -= n
      var total = 10 * n
      infantry += total
      println("You change " + n + " artillery for " + total + " infantry!!")
    } else {
      println(s"Not enough! You have $artillery")
    }
  }

  def infantry_to_cavalry(n: Int): Unit = {
    if (infantry >= 5) {
      cavalry += n
      var total = 5 * n
      infantry -= total
      println("You change " + total + " infantry for " + n + " cavalry!!")
    } else {
      println(s"Not enough! You have $cavalry")
    }
  }

  def infantry_to_artillery(n: Int): Unit = {
    if (infantry >= 10) {
      artillery += n
      var total = 10 * n
      infantry -= total
      println("You change " + total + " infantry for " + n + " artillery!!")
    } else {
      println(s"Not enough! You have $cavalry")
    }
  }

  override def toString: String = {
  s"Your army!!\n" +
    s"Artillery = $artillery , Calvary = $cavalry, Infantry = $infantry"
  }
}

