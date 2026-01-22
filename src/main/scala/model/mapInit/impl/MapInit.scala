package model.mapInit.impl

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

    // ---- Städte (als vars, damit wir sie "weiterbauen" können)
    var stockach         = Parent_Tile(name = "Stockach")
    var ueberlingen      = Parent_Tile(name = "Überlingen")
    var meersburg        = Parent_Tile(name = "Meersburg")
    var friedrichshafen  = Parent_Tile(name = "Friedrichshafen")
    var lindau           = Parent_Tile(name = "Lindau")

    var radolfzell       = Parent_Tile(name = "Radolfzell")
    var allensbach       = Parent_Tile(name = "Allensbach")
    var konstanz         = Parent_Tile(name = "Konstanz")
    var rheineck         = Parent_Tile(name = "Rheineck")
    var bregenz          = Parent_Tile(name = "Bregenz")

    var singen           = Parent_Tile(name = "Singen")
    var reichenau        = Parent_Tile(name = "Reichenau")
    var kreuzlingen      = Parent_Tile(name = "Kreuzlingen")
    var lustenau         = Parent_Tile(name = "Lustenau")
    var heiden           = Parent_Tile(name = "Heiden")

    var steinAmRhein     = Parent_Tile(name = "Stein am Rhein")
    var steckborn        = Parent_Tile(name = "Steckborn")
    var altnau           = Parent_Tile(name = "Altnau")
    var romanshorn       = Parent_Tile(name = "Romanshorn")
    var arbon            = Parent_Tile(name = "Arbon")

    // ---- Helper, um connect schön kurz zu schreiben
    def link(a: Parent_Tile, b: Parent_Tile, ab: direction, ba: direction): (Parent_Tile, Parent_Tile) =
      connect(a, b, ab, ba)

    // =========================
    //  HORIZONTALE Verbindungen
    // =========================
    { val (a,b) = link(stockach, ueberlingen, direction.east, direction.west); stockach=a; ueberlingen=b }
    { val (a,b) = link(ueberlingen, meersburg, direction.east, direction.west); ueberlingen=a; meersburg=b }
    { val (a,b) = link(meersburg, friedrichshafen, direction.east, direction.west); meersburg=a; friedrichshafen=b }
    { val (a,b) = link(friedrichshafen, lindau, direction.east, direction.west); friedrichshafen=a; lindau=b }

    { val (a,b) = link(radolfzell, allensbach, direction.east, direction.west); radolfzell=a; allensbach=b }
    { val (a,b) = link(allensbach, konstanz, direction.east, direction.west); allensbach=a; konstanz=b }
    { val (a,b) = link(rheineck, bregenz, direction.east, direction.west); rheineck=a; bregenz=b }

    { val (a,b) = link(lustenau, heiden, direction.east, direction.west); lustenau=a; heiden=b }

    { val (a,b) = link(steckborn, altnau, direction.east, direction.west); steckborn=a; altnau=b }
    { val (a,b) = link(altnau, romanshorn, direction.east, direction.west); altnau=a; romanshorn=b }
    { val (a,b) = link(romanshorn, arbon, direction.east, direction.west); romanshorn=a; arbon=b }

    // =======================
    //  VERTIKALE Verbindungen
    // =======================
    // Spalte 0
    { val (a,b) = link(stockach, radolfzell, direction.south, direction.north); stockach=a; radolfzell=b }
    { val (a,b) = link(radolfzell, singen, direction.south, direction.north); radolfzell=a; singen=b }
    { val (a,b) = link(singen, steinAmRhein, direction.south, direction.north); singen=a; steinAmRhein=b }

    // Spalte 1
    { val (a,b) = link(ueberlingen, allensbach, direction.south, direction.north); ueberlingen=a; allensbach=b }

    // Spalte 2
    { val (a,b) = link(meersburg, konstanz, direction.south, direction.north); meersburg=a; konstanz=b }
    { val (a,b) = link(konstanz, kreuzlingen, direction.south, direction.north); konstanz=a; kreuzlingen=b }
    { val (a,b) = link(kreuzlingen, altnau, direction.south, direction.north); kreuzlingen=a; altnau=b }

    // Spalte 3
    { val (a,b) = link(rheineck, lustenau, direction.south, direction.north); rheineck=a; lustenau=b }

    // Spalte 4
    { val (a,b) = link(lindau, bregenz, direction.south, direction.north); lindau=a; bregenz=b }
    { val (a,b) = link(heiden, arbon, direction.south, direction.north); heiden=a; arbon=b }

    // ======================
    //  DIAGONALE Verbindungen
    //  (wie im Sketch)
    // ======================
    // Konstanz (2,1) -> Reichenau (1,2): southwest / northeast
    { val (a,b) = link(konstanz, reichenau, direction.southwest, direction.northeast); konstanz=a; reichenau=b }

    // Kreuzlingen (2,2) -> Steckborn (1,3): southwest / northeast
    { val (a,b) = link(kreuzlingen, steckborn, direction.southwest, direction.northeast); kreuzlingen=a; steckborn=b }

    // ---- Layout exakt wie dein Bild
    val layout: List[List[String]] = List(
      List("Stockach","Überlingen","Meersburg","Friedrichshafen","Lindau"),
      List("Radolfzell","Allensbach","Konstanz","Rheineck","Bregenz"),
      List("Singen","Reichenau","Kreuzlingen","Lustenau","Heiden"),
      List("Stein am Rhein","Steckborn","Altnau","Romanshorn","Arbon")
    )

    // ---- Finales Mapping (WICHTIG: die finalen vars!)
    val parentsByName: Map[String, Parent_Tile] = Map(
      stockach.name        -> stockach,
      ueberlingen.name     -> ueberlingen,
      meersburg.name       -> meersburg,
      friedrichshafen.name -> friedrichshafen,
      lindau.name          -> lindau,

      radolfzell.name      -> radolfzell,
      allensbach.name      -> allensbach,
      konstanz.name        -> konstanz,
      rheineck.name        -> rheineck,
      bregenz.name         -> bregenz,

      singen.name          -> singen,
      reichenau.name       -> reichenau,
      kreuzlingen.name     -> kreuzlingen,
      lustenau.name        -> lustenau,
      heiden.name          -> heiden,

      steinAmRhein.name    -> steinAmRhein,
      steckborn.name       -> steckborn,
      altnau.name          -> altnau,
      romanshorn.name      -> romanshorn,
      arbon.name           -> arbon
    )

    layout.map(row => row.map(n => Tile(parentsByName(n))))
  }
}
