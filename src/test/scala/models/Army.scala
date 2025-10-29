package models

class Army {
  var soldiers = 10

  def myArmy: Array[Int] = Array(soldiers)

  override def toString: String = {
    s"Your army!!\n" +
      s"Total of $soldiers infantry!"
  }
}

