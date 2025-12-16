package model

import model.{Parent_Tile, Tile, add_neighbour, direction}

object MapInit {
  def testMap_init(): List[List[Tile]] = {
    // Alle Tiles mit Namen initialisieren
    val konstanz = Parent_Tile(List(), List(), "Konstanz")
    val reichenau = Parent_Tile(List(), List(), "Reichenau")
    val allensbach = Parent_Tile(List(), List(), "Allensbach")
    val radolfzell = Parent_Tile(List(), List(), "Radolfzell")
    val singen = Parent_Tile(List(), List(), "Singen")

    val steckborn = Parent_Tile(List(), List(), "Steckborn")
    val steinRhein = Parent_Tile(List(), List(), "Stein am Rhein")
    val romanshorn = Parent_Tile(List(), List(), "Romanshorn")
    val arbon = Parent_Tile(List(), List(), "Arbon")
    val kreuzlingen = Parent_Tile(List(), List(), "Kreuzlingen")

    val stockach = Parent_Tile(List(), List(), "Stockach")
    val uhldingen = Parent_Tile(List(), List(), "Uhldingen")
    val meersburg = Parent_Tile(List(), List(), "Meersburg")
    val ueberlingen = Parent_Tile(List(), List(), "Überlingen")
    val friedrichshafen = Parent_Tile(List(), List(), "Friedrichshafen")

    val immenstaad = Parent_Tile(List(), List(), "Immenstaad")
    val langennargen = Parent_Tile(List(), List(), "Langenargen")
    val tettnang = Parent_Tile(List(), List(), "Tettnang")
    val ravensburg = Parent_Tile(List(), List(), "Ravensburg")
    val lindau = Parent_Tile(List(), List(), "Lindau")

    // Konstanz-Region (beidseitige Verbindungen)
    val konstanz1 = konstanz.add_neighbour_tile(reichenau).add_connection(direction.west)
    val reichenau1 = reichenau.add_neighbour_tile(konstanz).add_connection(direction.east)
    val konstanz2 = konstanz1.add_neighbour_tile(allensbach).add_connection(direction.southwest)
    val allensbach1 = allensbach.add_neighbour_tile(konstanz).add_connection(direction.northeast)
    val konstanz3 = konstanz2.add_neighbour_tile(kreuzlingen).add_connection(direction.north)
    val kreuzlingen1 = kreuzlingen.add_neighbour_tile(konstanz).add_connection(direction.south)

    // Radolfzell-Region
    val radolfzell1 = radolfzell.add_neighbour_tile(allensbach).add_connection(direction.north)
    val allensbach2 = allensbach1.add_neighbour_tile(radolfzell).add_connection(direction.south)
    val radolfzell2 = radolfzell1.add_neighbour_tile(singen).add_connection(direction.south)
    val singen1 = singen.add_neighbour_tile(radolfzell).add_connection(direction.north)

    // Ostufer (Schweiz)
    val kreuzlingen2 = kreuzlingen1.add_neighbour_tile(steckborn).add_connection(direction.east)
    val steckborn1 = steckborn.add_neighbour_tile(kreuzlingen).add_connection(direction.west)
    val steckborn2 = steckborn1.add_neighbour_tile(steinRhein).add_connection(direction.northeast)
    val steinRhein1 = steinRhein.add_neighbour_tile(steckborn).add_connection(direction.southwest)
    val steinRhein2 = steinRhein1.add_neighbour_tile(romanshorn).add_connection(direction.south)
    val romanshorn1 = romanshorn.add_neighbour_tile(steinRhein).add_connection(direction.north)
    val romanshorn2 = romanshorn1.add_neighbour_tile(arbon).add_connection(direction.east)
    val arbon1 = arbon.add_neighbour_tile(romanshorn).add_connection(direction.west)

    // Westufer (Deutschland)
    val stockach1 = stockach.add_neighbour_tile(uhldingen).add_connection(direction.north)
    val uhldingen1 = uhldingen.add_neighbour_tile(stockach).add_connection(direction.south)
    val uhldingen2 = uhldingen1.add_neighbour_tile(meersburg).add_connection(direction.north)
    val meersburg1 = meersburg.add_neighbour_tile(uhldingen).add_connection(direction.south)
    val meersburg2 = meersburg1.add_neighbour_tile(ueberlingen).add_connection(direction.north)
    val ueberlingen1 = ueberlingen.add_neighbour_tile(meersburg).add_connection(direction.south)
    val ueberlingen2 = ueberlingen1.add_neighbour_tile(friedrichshafen).add_connection(direction.east)
    val friedrichshafen1 = friedrichshafen.add_neighbour_tile(ueberlingen).add_connection(direction.west)

    // Südufer
    val friedrichshafen2 = friedrichshafen1.add_neighbour_tile(immenstaad).add_connection(direction.west)
    val immenstaad1 = immenstaad.add_neighbour_tile(friedrichshafen).add_connection(direction.east)
    val immenstaad2 = immenstaad1.add_neighbour_tile(langennargen).add_connection(direction.west)
    val langennargen1 = langennargen.add_neighbour_tile(immenstaad).add_connection(direction.east)
    val langennargen2 = langennargen1.add_neighbour_tile(tettnang).add_connection(direction.southwest)
    val tettnang1 = tettnang.add_neighbour_tile(langennargen).add_connection(direction.northeast)

    // Oberschwaben
    val tettnang2 = tettnang1.add_neighbour_tile(ravensburg).add_connection(direction.south)
    val ravensburg1 = ravensburg.add_neighbour_tile(tettnang).add_connection(direction.north)
    val ravensburg2 = ravensburg1.add_neighbour_tile(lindau).add_connection(direction.east)
    val lindau1 = lindau.add_neighbour_tile(ravensburg).add_connection(direction.west)

    List(
      List(Tile(konstanz3), Tile(reichenau1), Tile(allensbach2), Tile(radolfzell2), Tile(singen1)),
      List(Tile(kreuzlingen2), Tile(steckborn2), Tile(steinRhein2), Tile(romanshorn2), Tile(arbon1)),
      List(Tile(stockach1), Tile(uhldingen2), Tile(meersburg2), Tile(ueberlingen2), Tile(friedrichshafen2)),
      List(Tile(immenstaad2), Tile(langennargen2), Tile(tettnang2), Tile(ravensburg2), Tile(lindau1))
    )
  }
}
