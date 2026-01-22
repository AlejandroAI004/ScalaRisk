package view
import controller.GameController.GameControllerPort
import controller.GameController.impl1.GameState
import model.player.Player
import model.tile.{Tile, direction}
import util.command.{PlayerConfigManager, UndoRedoManager}
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.JFXApp3
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.Node
import scalafx.scene.control.{Alert, Button, Label, TextArea, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{BorderPane, GridPane, HBox, Pane, Priority, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Line, Rectangle}
import scalafx.scene.text.Text
import util.observer.Observer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.Alert.AlertType
import util.gamePhase.GamePhase

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
  private var undoButton: Button = _
  private var redoButton: Button = _
  private var startCanon: ImageView = _
  private var rulesCanon: ImageView = _
  private var exitCanon: ImageView = _
  private var startIcon: ImageView = _
  private var rulesIcon: ImageView = _
  private var exitIcon: ImageView = _
  private var canonLogo: ImageView = _
  private var playersArea: TextArea = _
  private val rows = 2
  private val cols = 2
  private var tilesArray: Array[Array[(StackPane, Rectangle, Text, Text)]] = _
  private var history: UndoRedoManager[GameState] = _
  private var restoring = false
  private var activeOverlay: Option[(StackPane, VBox)] = None
  private var winnerLabel: Label = _
  private var winnerPanel: HBox = _
  private var boardOverlay: Pane = _



  def init(ctrl: GameControllerPort): Unit = {
    controller = ctrl
    controller.add(this)
    history = new UndoRedoManager(controller.snapshot)
  }

  override def update(): Unit = {
    println("GUI called.")
    if (boardGrid == null || tilesArray == null) return

    Platform.runLater {
      val mapData = controller.tiles
      val rows = mapData.length
      val cols = mapData.head.length

      for (y <- 0 until rows; x <- 0 until cols) {
        val (_, rect, soldiersLabel, cityLabel) = tilesArray(x)(y)
        val t = mapData(y)(x)

        soldiersLabel.text = t.soldiers.toString
        cityLabel.text = t.parent.name
        rect.fill = colorForPlayer(t.player)
      }

      playersArea.text = playersText

      controller.currentPhase match {
        case GamePhase.Placement =>
          undoButton.visible = true
          redoButton.visible = true

        case GamePhase.Offense | GamePhase.GameOver =>
          undoButton.visible = false
          redoButton.visible = false
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
      wrapText = true
    }

    val inputField = new TextField { promptText = "2-4" }

    val confirmButton = new Button("→")
    val undoButton    = new Button("Undo") { disable = true }
    val backButton    = new Button("Back")

    val buttonsRow = new VBox(10, confirmButton, undoButton, backButton) {
      alignment = Pos.Center
    }

    val panel = new VBox(12, questionLabel, inputField, buttonsRow) {
      alignment = Pos.Center
      padding = Insets(16)
      maxWidth = 360
      style =
        "-fx-background-color: rgba(20,20,20,0.85);" +
          "-fx-border-color: gold;" +
          "-fx-border-width: 2;" +
          "-fx-border-radius: 12;" +
          "-fx-background-radius: 12;"
    }

    panel.layoutX = 160
    panel.layoutY = 300

    confirmButton.onAction = _ => {
      if (currentStep == 0) {
        val n = inputField.text.value.toIntOption.getOrElse(0)
        if (n >= 2 && n <= 4) {
          numPlayers = n
          currentStep = 1
          undoButton.disable = false
          inputField.text = ""
          inputField.promptText = "red, blue, pink, green"
          questionLabel.text = s"Player 1: choose color"
        } else {
          questionLabel.text = "Bitte 2–4 eingeben!"
        }
      } else {
        val color = inputField.text.value.trim.toLowerCase
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
            val colorsFinal = manager.list.usedColors()
            controller.startGame(numPlayers, colorsFinal) 

            stage.scene = createBoardScene()
          }
        }
      }
    }

    undoButton.onAction = _ => {
      if (currentStep == 1) {
        manager.undo()
        val colorsNow = manager.list.usedColors()
        val nextIdx   = colorsNow.size + 1
        inputField.text = ""
        questionLabel.text = s"Player $nextIdx: choose color"
      }
    }

    backButton.onAction = _ => {
      root.children.remove(panel)
      root.children ++= Seq(startButton, rulesButton, exitButton, startCanon, rulesCanon, exitCanon)
    }

    root.children ++= Seq(panel)
  }




  private def attachTileHandler(tile: StackPane, xx: Int, yy: Int,
                                rect: Rectangle, label: Text): Unit = {

    tile.onMouseClicked = _ => {
      if (!offenseMode) {

        activeOverlay.foreach { case (oldTile, overlay) =>
          oldTile.children -= overlay
        }

        val input = new TextField { promptText = "Truppenanzahl" }
        val ok    = new Button("OK")

        val overlay = new VBox(5, new Label("Wie viele Truppen?"), input, ok) {
          alignment = Pos.Center
          style = "-fx-background-color: rgba(0,0,0,0.7); -fx-padding: 10;"
        }

        tile.children += overlay
        activeOverlay = Some(tile -> overlay)

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
    val rows = mapData.length
    val cols = mapData.head.length

    tilesArray = Array.ofDim[(StackPane, Rectangle, Text, Text)](cols, rows)

    val gap = 5.0
    val size = 80.0

    val grid = new GridPane {
      hgap = gap
      vgap = gap
    }
    boardGrid = grid

    for (y <- 0 until rows; x <- 0 until cols) {
      val t = mapData(y)(x)

      val rect = new Rectangle {
        width = size;
        height = size
        fill = colorForPlayer(t.player)
        stroke = Color.Black;
        strokeWidth = 2
      }

      val soldiersLabel = new Text {
        text = t.soldiers.toString
        fill = Color.White
        style = "-fx-font-size: 12px;"
      }

      val cityLabel = new Text {
        text = t.parent.name
        fill = Color.Yellow
        style = "-fx-font-size: 10px;"
      }

      StackPane.setAlignment(cityLabel, Pos.TopCenter)
      StackPane.setMargin(cityLabel, Insets(6, 0, 0, 0))

      StackPane.setAlignment(soldiersLabel, Pos.BottomCenter)
      StackPane.setMargin(soldiersLabel, Insets(0, 0, 6, 0))

      val tile = new StackPane {
        children = Seq(rect, cityLabel, soldiersLabel)
      }

      tilesArray(x)(y) = (tile, rect, soldiersLabel, cityLabel)
      attachTileHandler(tile, x, y, rect, soldiersLabel)
      grid.add(tile, x, y)
    }

    // ✅ Overlay für Connections
    boardOverlay = new Pane {
      mouseTransparent = true
    }

    val boardLayer = new StackPane {
      children = Seq(boardOverlay, grid)
    }

    // ✅ Players Area
    playersArea = new TextArea {
      editable = false
      wrapText = true
      prefWidth = 220
      text = playersText
      style = "-fx-control-inner-background: rgba(20,20,20,0.85);" +
        "-fx-text-fill: white;"
    }

    val playersAreaPanel = new VBox(playersArea) {
      padding = Insets(10)
      style =
        "-fx-background-color: rgba(20,20,20,0.85);" +
          "-fx-border-color: gold;" +
          "-fx-border-width: 2;" +
          "-fx-border-radius: 12;" +
          "-fx-background-radius: 12;"
    }

    undoButton = new Button("Undo")
    redoButton = new Button("Redo")

    val saveButton = new Button("Save")
    val loadButton = new Button("Load")

    undoButton.onAction = _ => controller.undo()
    redoButton.onAction = _ => controller.redo()

    saveButton.onAction = _ => {
      try {
        controller.saveGame()
        showInfo("Saved", "Spiel wurde gespeichert.")
      } catch {
        case ex: Exception =>
          showError("Save failed", ex.getMessage)
      }
    }

    loadButton.onAction = _ => {
      try {
        controller.loadGame()
        showInfo("Loaded", "Spielstand wurde geladen.")
      } catch {
        case ex: Exception =>
          showError("Load failed", ex.getMessage)
      }
    }

    // ✅ Winner Panel
    winnerLabel = new Label {
      text = ""
      style = "-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;"
    }

    winnerPanel = new HBox(winnerLabel) {
      alignment = Pos.Center
      padding = Insets(10)
      style =
        "-fx-background-color: rgba(0,0,0,0.75);" +
          "-fx-border-color: gold;" +
          "-fx-border-width: 2;"
      visible = false
      managed = false
    }

    val buttonsCol = new VBox(10, undoButton, redoButton, saveButton, loadButton) {
      alignment = Pos.Center
      padding = Insets(10, 0, 0, 0)
    }

    val rightPanel = new VBox(10, playersAreaPanel, buttonsCol) {
      padding = Insets(10)
      prefWidth = 240
    }

    val rootPane = new BorderPane {
      top = winnerPanel
      center = boardLayer // ✅ statt grid
      right = rightPanel
      style = "-fx-background-color: #9ed0ff"
    }

    BorderPane.setMargin(rightPanel, Insets(5))

    // ✅ initial zeichnen
    drawConnectionsOverlay(boardOverlay, mapData, size, gap)
    updateWinnerPanel()

    new Scene(cols * (size + gap) + 220, rows * (size + gap) + 60) { // +60 wegen winnerPanel
      root = rootPane
      fill = Color.Black
    }
  }

  private def dirOffset(d: direction): (Int, Int) = d match {
    case direction.north => (0, -1)
    case direction.south => (0, 1)
    case direction.west => (-1, 0)
    case direction.east => (1, 0)
    case direction.northeast => (1, -1)
    case direction.northwest => (-1, -1)
    case direction.southeast => (1, 1)
    case direction.southwest => (-1, 1)
  }

  private def arrowLine(x1: Double, y1: Double, x2: Double, y2: Double): Seq[Line] = {
    val main = new Line {
      startX = x1;
      startY = y1
      endX = x2;
      endY = y2
      stroke =  Color.Black
      strokeWidth = 2
      opacity = 0.75
    }

    // Pfeilspitze (kleines V)
    val angle = Math.atan2(y2 - y1, x2 - x1)
    val len = 10.0
    val a1 = angle + Math.toRadians(150)
    val a2 = angle - Math.toRadians(150)

    val head1 = new Line {
      startX = x2;
      startY = y2
      endX = x2 + len * Math.cos(a1)
      endY = y2 + len * Math.sin(a1)
      stroke = Color.White
      strokeWidth = 2
      opacity = 0.75
    }

    val head2 = new Line {
      startX = x2;
      startY = y2
      endX = x2 + len * Math.cos(a2)
      endY = y2 + len * Math.sin(a2)
      stroke = Color.White
      strokeWidth = 2
      opacity = 0.75
    }

    Seq(main, head1, head2)
  }

  private def drawConnectionsOverlay(
                                      overlay: Pane,
                                      mapData: List[List[Tile]],
                                      size: Double,
                                      gap: Double
                                    ): Unit = {
    overlay.children.clear()

    val rows = mapData.length
    val cols = mapData.head.length

    def centerX(x: Int): Double = x * (size + gap) + size / 2
    def centerY(y: Int): Double = y * (size + gap) + size / 2

    // damit wir nicht jede Verbindung doppelt zeichnen (weil beidseitig gespeichert)
    val drawDirs = Set(direction.east, direction.south, direction.southeast, direction.southwest)

    for (y <- 0 until rows; x <- 0 until cols) {
      val tile = mapData(y)(x)

      tile.parent.connections
        .filter(drawDirs.contains)
        .foreach { d =>
          val (dx, dy) = dirOffset(d)
          val nx = x + dx
          val ny = y + dy

          if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
            val lines = arrowLine(
              centerX(x), centerY(y),
              centerX(nx), centerY(ny)
            )
            overlay.children.addAll(lines.map(_.delegate))
          }
        }
    }
  }

  private def updateWinnerPanel(): Unit = {
    controller.checkWinner() match {
      case Some(p) =>
        winnerLabel.text = s"🏆 Gewinner: ${p.colorName}"
        winnerPanel.visible = true
        winnerPanel.managed = true
      case None =>
        winnerPanel.visible = false
        winnerPanel.managed = false
    }
  }

  private def showInfo(dialogTitle: String, msg: String): Unit =
    new Alert(AlertType.Information) {
      this.title = dialogTitle
      headerText = None
      contentText = msg
    }.showAndWait()

  private def showError(dialogTitle: String, msg: String): Unit =
    new Alert(AlertType.Error) {
      this.title = dialogTitle
      headerText = None
      contentText = msg
    }.showAndWait()

  private def colorForPlayer(p: Player): Color = {
    p.colorName match
      case "red" => Color.FireBrick
      case "blue" => Color.DodgerBlue
      case "pink" => Color.HotPink
      case "green" => Color.ForestGreen
      case _ => Color.DarkOliveGreen
  }

  private def playersText: String = {
    "List of players:\n" +
      controller.allPlayers.zipWithIndex.map { (p, i) =>
        s"Player ${i + 1} -> ${p.colorName} | Infantry: ${p.infantry}"
      }.mkString("\n")
  }


  override def start(): Unit = {

    val rootPane = new Pane()

    val logo = new ImageView(new Image(this.getClass.getResourceAsStream("/risiko_logo.png"))) {
      preserveRatio = true
      fitWidth = 1100
    }


     canonLogo = new ImageView(new Image(this.getClass.getResourceAsStream("/canon_logo.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
      visible = false
    }

     startIcon = new ImageView(new Image(this.getClass.getResourceAsStream("/start_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     rulesIcon = new ImageView(new Image(this.getClass.getResourceAsStream("/rules_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     exitIcon = new ImageView(new Image(this.getClass.getResourceAsStream("/exit_button.png"))) {
      fitWidth = 40
      fitHeight = 40
      preserveRatio = true
    }

     startCanon = new ImageView(new Image(this.getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     exitCanon = new ImageView(new Image(this.getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     rulesButton = new Button {
      graphic = new ImageView(new Image(this.getClass.getResourceAsStream("/rules_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
      }
    }

     rulesCanon = new ImageView(new Image(this.getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
    }

     exitButton = new Button {
      graphic = new ImageView(new Image(this.getClass.getResourceAsStream("/exit_button.png"))) {
        fitWidth = 85
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        Platform.exit()
      }
    }

    startButton = new Button {
      graphic = new ImageView(new Image(this.getClass.getResourceAsStream("/start_button.png"))) {
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

    exitButton.layoutX = 160
    exitButton.layoutY = 335

    exitCanon.layoutX = 118
    exitCanon.layoutY = 340

    exitButton.onMouseEntered = _ => exitCanon.visible = true
    exitButton.onMouseExited = _ => exitCanon.visible = false

    logo.layoutX = -150
    logo.layoutY = -150

    rootPane.children ++= Seq(startButton, logo, exitButton, startCanon, rulesCanon, exitCanon)


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
      onCloseRequest = _ => {
        println("GUI closed, exiting program...")
        System.exit(0)
      }
    }
  }
}
