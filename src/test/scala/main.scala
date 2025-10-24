import scala.io.StdIn.readInt

object main {
  def main(args: Array[String]): Unit = {

    println("Please enter x: ")
    val x: Int = readInt()
    println("Please enter y: ")
    val y: Int = readInt()
    println(generateField(x,y))
    println("branch 1")
    println("branch 2")

    def generateField(C: Int, R: Int): String = {
      "*" + "--------*" * C + "\n" +
        ("|" + "        |" * C + "\n" +
          "|" + "        |" * C + "\n" +
          "*" + "--------*" * C + "\n") * R
    }
  }
}