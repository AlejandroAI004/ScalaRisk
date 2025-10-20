import scala.io.StdIn.readInt
import scala.io.StdIn

object main {
  def main(args: Array[String]): Unit = {

    println("Please enter x: ")
    val x: Int = readInt()
    println("Please enter y: ")
    val y: Int = readInt()
    println(generateField(x,y))

    def generateField(C: Int, R: Int): String = {
      "*" + "--------*" * C + "\n" +
        ("|" + "        |" * C + "\n" +
          "|" + "        |" * C + "\n" +
          "*" + "--------*" * C + "\n") * R
    }
  }
}