package Tests

import controller.Map_Generation.imp1.Map_Generation
import model.mapInit.impl.MapInit.createInitialMap
import controller.Map_Generation.*
import model.player.Player
import model.tile.{Parent_Tile, Tile, direction}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class Map_Genration_spec extends AnyWordSpec with Matchers {

  private def tileWith(
                        name: String,
                        conns: List[direction] = Nil,
                        playerColor: String = "red",
                        soldiers: Int = 1
                      ): Tile = {
    val pTile  = Parent_Tile(connections = conns, name = name)
    val player = new Player(playerColor)
    Tile(pTile, player, soldiers)
  }

  "print_upper_conn" should {
    "show no connectors when there are no connections" in {
      val row = List(tileWith("A"), tileWith("B"))
      val out = Map_Generation.print_upper_conn(row)

      out.count(_ == '\\') shouldBe 0
      out.count(_ == '/')  shouldBe 0
      out.count(_ == '|')  shouldBe 0
      out.endsWith("\n")   shouldBe true
    }

    "contain connectors for present directions" in {
      val t = tileWith("A", conns = List(direction.north, direction.northwest, direction.northeast))
      val out = Map_Generation.print_upper_conn(List(t))

      out should include ("\\")
      out should include ("/")
      out.count(_ == '|') should be >= 1
    }
  }

  "print_lower_conn" should {
    "show vertical and diagonal connectors for south directions" in {
      val t = tileWith("A", conns = List(direction.south, direction.southwest, direction.southeast))
      val out = Map_Generation.print_lower_conn(List(t))

      out should include ("/")
      out should include ("\\")
      out.count(_ == '|') should be >= 1
    }
  }

  "print_horizontal" should {
    "repeat the horizontal border for each tile" in {
      val row = List(tileWith("A"), tileWith("B"), tileWith("C"))
      val out = Map_Generation.print_horizontal(row)

      out.split("\n").head should include ("+--------+")
      // 3 Felder → 3 Rahmen
      out.split("\\+--------\\+").length - 1 shouldBe 3
    }
  }

  "print_upper_area" should {
    "contain player color and soldier count" in {
      val p = new Player("pink"); p.infantry = 0
      val t = Tile(Parent_Tile(name = "A"), p, soldiers = 4)

      val out = Map_Generation.print_upper_area(List(t))

      out should include ("pink")
      out should include ("4")
    }

    "use different borders when west/east connections are present" in {
      val left  = tileWith("L", conns = List(direction.west))
      val right = tileWith("R", conns = List(direction.east))

      val out = Map_Generation.print_upper_area(List(left, right))

      out should include ("__|")
      out should include ("|__")
    }
  }

  "print_lower_area" should {
    "print the city name inside the box" in {
      val t = tileWith("Konstanz")
      val out = Map_Generation.print_lower_area(List(t))

      out should include ("Konstan") // wegen take(7)
    }
  }

  "print_row" should {
    "combine all parts into multiple lines" in {
      val row = List(tileWith("A"), tileWith("B"))
      val out = Map_Generation.print_row(row)

      val lines = out.linesIterator.toList
      lines.length should be >= 5   // upper_conn, horizontal, upper_area, lower_area, horizontal, lower_conn

      lines.exists(_.contains("+--------+")) shouldBe true
    }
  }

  "print_map" should {
    "print all rows of the map" in {
      val row1 = List(tileWith("A"), tileWith("B"))
      val row2 = List(tileWith("C"), tileWith("D"))

      val out = Map_Generation.print_map(List(row1, row2))

      // beide Zeilen-Namen sollten vorkommen
      out should include ("A")
      out should include ("D")
      // mindestens zwei horizontale Linien
      out.split("\\+--------\\+").length - 1 should be >= 4
    }
  }
}

