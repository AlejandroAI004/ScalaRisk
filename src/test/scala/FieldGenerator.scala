object FieldGenerator {
  def generateField(C: Int, R: Int): String = {
    "*" + "--------*" * C + "\n" +
      ("|" + "        |" * C + "\n" +
        "|" + "        |" * C + "\n" +
        "*" + "--------*" * C + "\n") * R
  }
}