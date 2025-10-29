import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers


class FieldGeneratorSpec extends AnyWordSpec with Matchers {
  "generateField" should {
    "produce correct size for 2x2" in {
      val result = FieldGenerator.generateField(2,2)
      result.linesIterator.size shouldBe 7  // Beispiel-Test
    }
  }
}