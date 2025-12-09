package view

import controller.*
import model.*
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ContentDisplay, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{BorderPane, Pane, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.Includes.*
import scalafx.application.JFXApp3.PrimaryStage

object GUIView extends JFXApp3 {

  override def start(): Unit = {

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

    val startButton = new Button {
      graphic = new ImageView(new Image(getClass.getResourceAsStream("/start_button.png"))) {
        fitWidth = 100
        preserveRatio = true
      }
      style = "-fx-background-color: transparent;"

      onAction = _ => {
        println("Start Game (TUI)")        // oder controller.startGame()
      }
    }

    val startCanon = new ImageView(new Image(getClass.getResourceAsStream("/canon_logo.png"))) {
      visible = false
      preserveRatio = true
      fitWidth = 45
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

    val rootPane = new Pane()

    rootPane.children ++= Seq(startButton,logo,rulesButton,exitButton,startCanon,rulesCanon,exitCanon)

    stage = new PrimaryStage {
      title = "Risiko – Bodensee Edition"
      scene = new Scene(1000, 600) {
        root = rootPane
        stylesheets.add(getClass.getResource("/style.css").toExternalForm)
      }
    }
  }
}
