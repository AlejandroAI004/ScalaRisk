package model.mapInit.imp1

import model.mapInit.MapInitPort
import model.tile.direction.{east, south}
import model.tile.{Parent_Tile, Tile, direction}

object MapInit extends MapInitPort {
  def connect(
               a: Parent_Tile,
               b: Parent_Tile,
               dirAB: direction,
               dirBA: direction
             ): (Parent_Tile, Parent_Tile) = {
    val a1 = a.addNeighbourName(b.name).add_connection(dirAB)
    val b1 = b.addNeighbourName(a.name).add_connection(dirBA)
    (a1, b1)
  }
  override def createInitialMap(): List[List[Tile]] = {
    val konstanz = Parent_Tile(name = "Konstanz")
    val reichenau = Parent_Tile(name = "Reichenau")
    val allensbach = Parent_Tile(name = "Allensbach")
    val radolfzell = Parent_Tile(name = "Radolfzell")
    val singen = Parent_Tile(name = "Singen")

    val steckborn = Parent_Tile(name = "Steckborn")
    val steinRhein = Parent_Tile(name = "Stein am Rhein")
    val romanshorn = Parent_Tile(name = "Romanshorn")
    val arbon = Parent_Tile(name = "Arbon")
    val kreuzlingen = Parent_Tile(name = "Kreuzlingen")

    val stockach = Parent_Tile(name = "Stockach")
    val uhldingen = Parent_Tile(name = "Uhldingen")
    val meersburg = Parent_Tile(name = "Meersburg")
    val ueberlingen = Parent_Tile(name = "Überlingen")
    val friedrichshafen = Parent_Tile(name = "Friedrichshafen")

    val immenstaad = Parent_Tile(name = "Immenstaad")
    val langennargen = Parent_Tile(name = "Langenargen")
    val tettnang = Parent_Tile(name = "Tettnang")
    val ravensburg = Parent_Tile(name = "Ravensburg")
    val lindau = Parent_Tile(name = "Lindau")

    // Konstanz-Region (beidseitige Verbindungen)
    val (konstanz1, reichenau1) =
      connect(konstanz, reichenau, direction.west, direction.east)

    val (konstanz2, allensbach1) =
      connect(konstanz1, allensbach, direction.southwest, direction.northeast)

    val (konstanz3, kreuzlingen1) =
      connect(konstanz2, kreuzlingen, direction.north, direction.south)


    // Radolfzell-Region
    val (radolfzell1, allensbach2) =
      connect(radolfzell, allensbach1, direction.north, direction.south)

    val (radolfzell2, singen1) =
      connect(radolfzell1, singen, direction.south, direction.north)


    // Ostufer (Schweiz)
    val (kreuzlingen2, steckborn1) =
      connect(kreuzlingen1, steckborn, direction.east, direction.west)

    val (steckborn2, steinRhein1) =
      connect(steckborn1, steinRhein, direction.northeast, direction.southwest)

    val (steinRhein2, romanshorn1) =
      connect(steinRhein1, romanshorn, direction.south, direction.north)

    val (romanshorn2, arbon1) =
      connect(romanshorn1, arbon, direction.east, direction.west)


    // Westufer (Deutschland)
    val (stockach1, uhldingen1) =
      connect(stockach, uhldingen, direction.north, direction.south)

    val (uhldingen2, meersburg1) =
      connect(uhldingen1, meersburg, direction.north, direction.south)

    val (meersburg2, ueberlingen1) =
      connect(meersburg1, ueberlingen, direction.north, direction.south)

    val (ueberlingen2, friedrichshafen1) =
      connect(ueberlingen1, friedrichshafen, direction.east, direction.west)


    // Südufer
    val (friedrichshafen2, immenstaad1) =
      connect(friedrichshafen1, immenstaad, direction.west, direction.east)

    val (immenstaad2, langennargen1) =
      connect(immenstaad1, langennargen, direction.west, direction.east)

    val (langennargen2, tettnang1) =
      connect(langennargen1, tettnang, direction.southwest, direction.northeast)


    // Oberschwaben
    val (tettnang2, ravensburg1) =
      connect(tettnang1, ravensburg, direction.south, direction.north)

    val (ravensburg2, lindau1) =
      connect(ravensburg1, lindau, direction.east, direction.west)


    val layout: List[List[String]] = List(
      List("Überlingen","Reichenau","Allensbach","Radolfzell","Kreuzlingen"),
      List("Stockach","Steckborn","Immenstaad","Romanshorn","Arbon"),
      List("Singen","Uhldingen","Meersburg","Konstanz","Friedrichshafen"),
      List("Stein am Rhein","Langenargen","Tettnang","Ravensburg","Lindau")
    )

    val parentsByName: Map[String, Parent_Tile] = Map(
      "Konstanz" -> konstanz3,
      "Reichenau" -> reichenau1,
      "Allensbach" -> allensbach2,
      "Radolfzell" -> radolfzell2,
      "Singen" -> singen1,

      "Kreuzlingen" -> kreuzlingen2,
      "Steckborn" -> steckborn2,
      "Stein am Rhein" -> steinRhein2,
      "Romanshorn" -> romanshorn2,
      "Arbon" -> arbon1,

      "Stockach" -> stockach1,
      "Uhldingen" -> uhldingen2,
      "Meersburg" -> meersburg2,
      "Überlingen" -> ueberlingen2,
      "Friedrichshafen" -> friedrichshafen2,

      "Immenstaad" -> immenstaad2,
      "Langenargen" -> langennargen2,
      "Tettnang" -> tettnang2,
      "Ravensburg" -> ravensburg2,
      "Lindau" -> lindau1
    )

    layout.map(row => row.map(name => Tile(parentsByName(name))))
  }
}
