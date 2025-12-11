package view

import controller.*
import model.*
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.{JFXApp3, Platform}
import scalafx.geometry.Pos
import scalafx.scene.{Node, Scene}
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{GridPane, Pane, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text
import scalafx.scene.layout.{GridPane, StackPane}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.util.{Failure, Success}

object GUIView extends JFXApp3 with Observer {
  private var selectedFrom: Option[(Int, Int, StackPane, Rectangle)] = None
  private var controller: GameController = _
  private var boardGrid: GridPane = _
  private var placementPromptOpen = false
  private var offenseMode: Boolean = false



  def init(ctrl: GameController): Unit = {
    controller = ctrl
    controller.add(this)
  }

  override def update(): Unit = {
    // später: GUI aktualisieren, z.B. Map neu zeichnen
    println("GUIView: update called")
    val rest = controller.remainingInfantryPerPlayer
    rest.foreach { case (color, inf) =>
      println(s"$color: $inf Infanterie übrig")
    }

  }

  private def configurePlayers(root: Pane): Unit = {
    var currentStep = 0
    var numPlayers  = 0
    val manager     = new PlayerConfigManager

    val questionLabel = new Label("How many players are gonna play? (min 2, limit 4)") {
      style =
        "-fx-font-size: 16px; -fx-text-fill: red; " +
          "-fx-font-family: 'Comic Sans MS'; -fx-font-weight: bold;" +
          "-fx-effect: dropshadow(gaussian, white, 6, 0.8, 0, 0);"
      layoutX = 300
      layoutY = 310
    }

    val inputField = new TextField {
      promptText = "2-4"
      layoutX = 300
      layoutY = 350
    }

    val confirmButton: Button = new Button("→") {
      layoutX = 450
      layoutY = 350

      onAction = _ => {
        if (currentStep == 0) {
          // Schritt: Spieleranzahl
          val n = inputField.text.value.toIntOption.getOrElse(0)
          if (n >= 2 && n <= 4) {
            numPlayers = n
            currentStep = 1
            inputField.text = ""
            inputField.promptText = "red, blue, pink, green"
            questionLabel.text = s"Player 1: choose color"
          } else {
            questionLabel.text = "Bitte 2–4 eingeben!"
          }

        } else {
          // Schritt: Farben wählen, Manager benutzen
          val color   = inputField.text.value.trim.toLowerCase
          val allowed = List("red", "blue", "pink", "green")

          val usedColors = manager.list.usedColors()

          if (!allowed.contains(color)) {
            questionLabel.text = "Unbekannte Farbe, bitte red/blue/pink/green"
          } else if (usedColors.contains(color)) {
            questionLabel.text = "Farbe schon vergeben, andere wählen"
          } else {
            // Spieler hinzufügen (mit Undo-Unterstützung)
            manager.addPlayer(color)
            val colorsNow = manager.list.usedColors()

            if (colorsNow.size < numPlayers) {
              val nextIdx = colorsNow.size + 1
              inputField.text = ""
              questionLabel.text = s"Player $nextIdx: choose color"
            } else {
              // alle Farben gewählt → Spiel starten
              val playersListObj = manager.list
              val colorsFinal    = playersListObj.usedColors()

              val players = controller.startGame(numPlayers, colorsFinal)
              println(s"GUI gestartet mit Spielern: $players")

              val boardScene = createBoardScene()
              stage.scene = boardScene

              questionLabel.text = "Spiel gestartet!"
            }
          }
        }
      }
    }

    val undoButton: Button = new Button("Undo") {
      layoutX = 480
      layoutY = 350

      onAction = _ => {
        if (currentStep == 1) { // nur in der Farbwahl-Phase sinnvoll
          manager.undo()
          val colorsNow = manager.list.usedColors()
          val nextIdx   = colorsNow.size + 1
          inputField.text = ""
          questionLabel.text = s"Player $nextIdx: choose color"
        }
      }
    }

    root.children ++= Seq(questionLabel, inputField, confirmButton, undoButton)
  }

  def colorForPlayer(p: Player): Color = p.colorName match {
    case "red" => Color.Red
    case "blue" => Color.Blue
    case "pink" => Color.HotPink
    case "green" => Color.Green
    case _ => Color.Gray
  }

  def attachTileHandler(tile: StackPane, xx: Int, yy: Int,
                        rect: Rectangle, label: Text): Unit = {

    tile.onMouseClicked = _ => {
      if (!offenseMode) {
        // Placement-Phase
        if (placementPromptOpen) return
        placementPromptOpen = true

        val input = new TextField { promptText = "Truppenanzahl" }
        val ok    = new Button("OK")

        val overlay = new VBox(5, new Label("Wie viele Truppen?"), input, ok) {
          alignment = Pos.Center
          style = "-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 10;"
        }

        tile.children += overlay

        ok.onAction = _ => {
          val n = input.text.value.toIntOption.getOrElse(0)
          placementPromptOpen = false
          tile.children -= overlay

          if (n > 0) {
            controller.placeInfantry(controller.currentPlayer, xx, yy, n) match {
              case Success(newMap) =>
                val newTile = newMap(yy)(xx)
                label.text = newTile.soldiers.toString
                rect.fill  = colorForPlayer(newTile.player)
                controller.nextPlayerTurn()
                if (controller.allInfantryPlaced) {
                  offenseMode = true
                }
              case Failure(ex) =>
                println(ex.getMessage)
            }
          }
        }
      } else {
        // Offense-Phase
        handleOffenseClick(xx, yy, tile, rect)
      }
    }
  }

  private def handleOffenseClick(x: Int, y: Int, tile: StackPane, rect: Rectangle): Unit = {
    selectedFrom match {
      case None =>
        // Angreifer auswählen
        val fromTile = controller.tiles(y)(x)
        if (fromTile.player != controller.currentPlayer || fromTile.soldiers <= 1) {
          println("Ungültiges Angreiferfeld")
        } else {
          selectedFrom = Some((x, y, tile, rect))
          rect.stroke = Color.White
        }

      case Some((fromX, fromY, fromTileNode, fromRect)) =>
        // Gleiches Feld -> Auswahl aufheben
        if (fromX == x && fromY == y) {
          fromRect.stroke = Color.Black
          selectedFrom = None
        } else {
          fromRect.stroke = Color.Black
          askAttackStrengthAndExecute(fromX, fromY, x, y, tile)
        }
    }
  }

  private def askAttackStrengthAndExecute(fromX: Int, fromY: Int,
                                          toX: Int, toY: Int,
                                          targetTile: StackPane): Unit = {

    val input = new TextField {
      promptText = "Anzahl Truppen"
    }
    val ok = new Button("OK")

    val overlay = new VBox(5, new Label("Mit wie vielen Truppen angreifen?"), input, ok) {
      alignment = Pos.Center
      style = "-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 10;"
    }

    targetTile.children += overlay

    ok.onAction = _ => {
      val n = input.text.value.toIntOption.getOrElse(0)
      targetTile.children -= overlay
      selectedFrom = None

      if (n <= 0) {
        println("Bitte eine positive Zahl eingeben")
      } else {
        controller.offense_phase(controller.currentPlayer, fromX, fromY, toX, toY, n) match {
          case Success(newMap) =>
            controller.nextPlayerTurn()

          case Failure(ex) =>
            println(ex.getMessage)
        }
      }
    }
  }


  private def createBoardScene(): Scene = {
    val grid = new GridPane {
      hgap = 5
      vgap = 5
    }
    boardGrid = grid

    val size = 100.0

    for (yy <- 0 until 2; xx <- 0 until 2) {
      val rect = new Rectangle {
        width = size
        height = size
        fill = Color.DarkOliveGreen
        stroke = Color.Black
        strokeWidth = 2
      }

      val label = new Text {
        text = "0"
        fill = Color.White
      }

      val tile = new StackPane {
        children = Seq(rect, label)
      }

      attachTileHandler(tile, xx, yy, rect, label) // hier ist deine Klicklogik
      grid.add(tile, xx, yy)
    }

    new Scene(300, 300) {
      root = grid
      fill = Color.Black
    }
  }


  override def start(): Unit = {

    val rootPane = new Pane()

    val logo = new ImageView(new Image(getClass.getResourceAsStream("/risiko_logo.png"))) {
      preserveRatio = true
      fitWidth = 1100
    }

    val canonLogo = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
      visible = false
    }

    val startIcon = new ImageView(new Image(getClass.getResourceAsStream("/start_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

    val rulesIcon = new ImageView(new Image(getClass.getResourceAsStream("/rules_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

    val exitIcon = new ImageView(new Image(getClass.getResourceAsStream("/exit_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

    val startCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

    val startButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/start_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        configurePlayers(rootPane)
      }
    }

    val introRoot = new Pane()
    val introText = new Label("*** Welcome to Risk! ***\n" +
      "Bei Risk kämpfst du um die Weltherrschaft! \n" +
      "Du platzierst Armeen, planst Angriffe und würfelst um den Sieg. \n" +
      "Mit geschickter Strategie und etwas Glück eroberst du nach und \n" +
      "nach neue Länder und Kontinente. Jede Runde bringt neue Truppen, \n" +
      "spannende Kämpfe und riskante Entscheidungen. Wer am Ende die \n" +
      "meisten Gebiete kontrolliert – oder seine geheime Mission erfüllt \n" +
      "–, gewinnt das Spiel und herrscht über die Welt!\n") {
      style = "-fx-font-size: 24px; -fx-text-fill: red; " +
        "-fx-font-family: 'Comic Sans MS'; -fx-font-weight: bold;" +
        "-fx-effect: dropshadow(gaussian, white, 6, 0.8, 0, 0);"
      layoutX = 100
      layoutY = 150
    }

    introRoot.children += introText

    val introScene = new Scene(1000, 600) {
      root = introRoot
      stylesheets.add(getClass.getResource("/style.css").toExternalForm)
    }

    val rulesButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/rules_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        println("Start Game (TUI)") // oder controller.startGame()
      }
    }

    val rulesCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

    val exitButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/exit_button.png"))) {
        fitWidth = 85
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        Platform.exit()
      }
    }

    val exitCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

    startButton.layoutX = 160
    startButton.layoutY = 300

    startCanon.layoutX = 118
    startCanon.layoutY = 305

    startButton.onMouseEntered = _ => startCanon.visible = true
    startButton.onMouseExited = _ => startCanon.visible = false

    rulesButton.layoutX = 160
    rulesButton.layoutY = 335

    rulesCanon.layoutX = 118
    rulesCanon.layoutY = 340

    rulesButton.onMouseEntered = _ => rulesCanon.visible = true
    rulesButton.onMouseExited = _ => rulesCanon.visible = false

    exitButton.layoutX = 160
    exitButton.layoutY = 375

    exitCanon.layoutX = 118
    exitCanon.layoutY = 380

    exitButton.onMouseEntered = _ => exitCanon.visible = true
    exitButton.onMouseExited = _ => exitCanon.visible = false

    logo.layoutX = -150
    logo.layoutY = -150

    rootPane.children ++= Seq(startButton, logo, rulesButton, exitButton, startCanon, rulesCanon, exitCanon)


    val menuScene = new Scene(1000, 600) {
      root = rootPane
      stylesheets.add(getClass.getResource("/style.css").toExternalForm)
    }

    introScene.onMouseClicked = _ => {
      stage.scene = menuScene
    }

    stage = new PrimaryStage {
      title = "Risiko – Bodensee Edition"
      scene = introScene

    }
  }
}
