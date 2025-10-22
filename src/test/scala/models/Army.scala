package models

class Army {
  var infantry = 10
  var calvary = 5
  var artillery = 1

  def myArmy: Array[Int] = Array(infantry,calvary,artillery)

  def calvary_to_infantry(n: Int): Unit = {
    if(calvary > n) {
      calvary -= n
      var total = 5 * n
      infantry += total
      println("You change " + n + " calvary for " + total + " infantry!!")
    } else {
      println(s"Not enaught! You have $calvary")
    }
  }

  override def toString: String = {
  s"Your army!!\n" +
    s"Artillery = $artillery , Calvary = $calvary, Infantry = $infantry"
  }
}

