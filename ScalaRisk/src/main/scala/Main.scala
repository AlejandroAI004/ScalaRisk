import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    print("Columns:")
    val C = StdIn.readInt()
    print("Rows:")
    val R = StdIn.readInt()
    print {generateField(C,R)}
  }
  def generateField(C: Int, R: Int): String = {
    "+" + "--------+" * C + "\n" +
      ("|" + "        |" * C + "\n" +
        "|" + "        |" * C + "\n" +
        "+" + "--------+" * C + "\n") * R
  }
}
