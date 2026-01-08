package view
import controller.GameController.GameControllerPort
import model.*
import model.player.Player
import util.command.PlayerConfigManager
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.Node
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Pane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.layout.{GridPane, StackPane}
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text
import util.observer.Observer

import scala.util.{Failure, Success}

object GUIView extends JFXApp3 with Observer {
  private var selectedFrom: Option[(Int, Int, StackPane, Rectangle)] = None
  private var controller: GameControllerPort = _
  private var boardGrid: GridPane = _
  private var placementPromptOpen = false
  private var offenseMode: Boolean = false
  private var startButton: Button = _
  private var rulesButton: Button = _
  private var exitButton: Button = _
  private var startCanon: ImageView = _
  private var rulesCanon: ImageView = _
  private var exitCanon: ImageView = _
  private var startIcon: ImageView = _
  private var rulesIcon: ImageView = _
  private var exitIcon: ImageView = _
  private var canonLogo: ImageView = _
  val rows = 2
  val cols = 2
  private var tilesArray: Array[Array[(StackPane, Rectangle, Text, Text)]] = _

  
  def init(ctrl: GameControllerPort): Unit = {
    controller = ctrl
    controller.add(this)
  }

  override def update(): Unit = {
    if (boardGrid == null || tilesArray == null) return

    Platform.runLater {
      val mapData = controller.tiles

      for (y <- 0 until rows; x <- 0 until cols) {
        val (tile, rect, soldiersLabel, cityLabel) = tilesArray(x)(y)
        val t = mapData(y)(x)

        soldiersLabel.text = t.soldiers.toString
        cityLabel.text = t.parent.name
        rect.fill = colorForPlayer(t.player)
      }
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
          val color   = inputField.text.value.trim.toLowerCase
          val allowed = List("red", "blue", "pink", "green")
          val usedColors = manager.list.usedColors()

          if (!allowed.contains(color)) {
            questionLabel.text = "Unbekannte Farbe, bitte red/blue/pink/green"
          } else if (usedColors.contains(color)) {
            questionLabel.text = "Farbe schon vergeben, andere wählen"
          } else {
            manager.addPlayer(color)
            val colorsNow = manager.list.usedColors()

            if (colorsNow.size < numPlayers) {
              val nextIdx = colorsNow.size + 1
              inputField.text = ""
              questionLabel.text = s"Player $nextIdx: choose color"
            } else {
              val playersListObj = manager.list
              val colorsFinal = playersListObj.usedColors()

              val players = controller.startGame(numPlayers, colorsFinal).foreach(println)
              println(players)

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
        if (currentStep == 1) {
          manager.undo()
          val colorsNow = manager.list.usedColors()
          val nextIdx   = colorsNow.size + 1
          inputField.text = ""
          questionLabel.text = s"Player $nextIdx: choose color"
        }
      }
    }

    val backButton: Button = new Button("Back") {
      layoutX = 540
      layoutY = 350

      onAction = _ => {
        // alle Konfig-Controls entfernen
        root.children --= Seq(questionLabel, inputField, confirmButton, undoButton, this)
        // Menü-Buttons wieder einblenden
        root.children ++= Seq(startButton, rulesButton, exitButton, startCanon, rulesCanon, exitCanon)
      }
    }

    root.children ++= Seq(questionLabel, inputField, confirmButton, undoButton, backButton)
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
        handleOffenseClick(xx, yy, tile, rect)
      }
    }
  }

  private def handleOffenseClick(x: Int, y: Int, tile: StackPane, rect: Rectangle): Unit = {
    selectedFrom match {
      case None =>
        val fromTile = controller.tiles(y)(x)
        if (fromTile.player != controller.currentPlayer || fromTile.soldiers <= 1) {
          println("Ungültiges Angreiferfeld")
        } else {
          selectedFrom = Some((x, y, tile, rect))
          rect.stroke = Color.White
        }

      case Some((fromX, fromY, fromTileNode, fromRect)) =>
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


  def createBoardScene(): Scene = {
    val mapData = controller.tiles
    val rows    = mapData.length
    val cols    = mapData.head.length

    tilesArray = Array.ofDim[(StackPane, Rectangle, Text, Text)](cols, rows)

    val grid = new GridPane { hgap = 5; vgap = 5 }
    boardGrid = grid
    val size = 80.0

    for (y <- 0 until rows; x <- 0 until cols) {
      val t = mapData(y)(x)

      val rect = new Rectangle {
        width = size; height = size
        fill = Color.DarkOliveGreen
        stroke = Color.Black; strokeWidth = 2
      }

      val soldiersLabel = new Text {
        text = t.soldiers.toString
        fill = Color.White
        style = "-fx-font-size: 12px;"
      }

      val cityLabel = new Text {
        text = t.parent.name  // ✅ funktioniert jetzt!
        fill = Color.Yellow
        style = "-fx-font-size: 10px;"
      }

      // Zentrierung der Labels
      soldiersLabel.layoutXProperty().set(size / 2 - 6)
      soldiersLabel.layoutYProperty().set(size / 2 + 4)

      cityLabel.layoutXProperty().set(size / 2 - 20)
      cityLabel.layoutYProperty().set(size / 2 - 8)

      val tile = new StackPane {
        children = Seq(rect, cityLabel, soldiersLabel)  // Stadtname UNTER Soldaten
      }

      tilesArray(x)(y) = (tile, rect, soldiersLabel, cityLabel)
      attachTileHandler(tile, x, y, rect, soldiersLabel) 
      grid.add(tile, x, y)
    }

    new Scene(cols * (size + 5), rows * (size + 5)) {
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


     canonLogo = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
      visible = false
    }

     startIcon = new ImageView(new Image(getClass.getResourceAsStream("/start_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     rulesIcon = new ImageView(new Image(getClass.getResourceAsStream("/rules_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     exitIcon = new ImageView(new Image(getClass.getResourceAsStream("/exit_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     startCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     exitCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     rulesButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/rules_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
      }
    }

     rulesCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     exitButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/exit_button.png"))) {
        fitWidth = 85
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        Platform.exit()
      }
    }

    startButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/start_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        configurePlayers(rootPane)
        rootPane.children --= Seq(startButton, rulesButton, exitButton, startCanon, rulesCanon, exitCanon)
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
