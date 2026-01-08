package Tests
import model.*
import model.PlayerConfig.Manager.PlayerConfigManager
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class command_spec extends AnyWordSpec with Matchers {

  "PlayerConfigManager" should {

    "start empty" in {
      val mgr = new PlayerConfigManager
      mgr.size shouldBe 0
      mgr.list.toList shouldBe empty
    }

    "add players and increase size" in {
      val mgr = new PlayerConfigManager

      mgr.addPlayer("red")
      mgr.size shouldBe 1
      mgr.list.usedColors() shouldBe List("red")

      mgr.addPlayer("blue")
      mgr.size shouldBe 2
      mgr.list.usedColors() shouldBe List("red", "blue")
    }

    "undo last addPlayer" in {
      val mgr = new PlayerConfigManager

      mgr.addPlayer("red")
      mgr.addPlayer("blue")
      mgr.size shouldBe 2
      mgr.list.usedColors() shouldBe List("red", "blue")

      mgr.undo()

      mgr.size shouldBe 1
      mgr.list.usedColors() shouldBe List("red")
    }

    "redo after undo restores previous state" in {
      val mgr = new PlayerConfigManager

      mgr.addPlayer("red")
      mgr.addPlayer("blue")
      mgr.undo()

      mgr.size shouldBe 1
      mgr.list.usedColors() shouldBe List("red")

      mgr.redo()

      mgr.size shouldBe 2
      mgr.list.usedColors() shouldBe List("red", "blue")
    }

    "calling undo on empty stack does nothing" in {
      val mgr = new PlayerConfigManager

      mgr.undo()
      mgr.size shouldBe 0
    }

    "calling redo on empty stack does nothing" in {
      val mgr = new PlayerConfigManager

      mgr.redo()
      mgr.size shouldBe 0
    }

    "clears redoStack when adding after undo" in {
      val mgr = new PlayerConfigManager

      mgr.addPlayer("red")
      mgr.addPlayer("blue")
      mgr.undo()

      mgr.addPlayer("green")

      mgr.size shouldBe 2
      mgr.list.usedColors() shouldBe List("red", "green")

      mgr.redo()
      mgr.size shouldBe 2
      mgr.list.usedColors() shouldBe List("red", "green")
    }
  }
}