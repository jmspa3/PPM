package UI.Menu

import java.util

import PPMProject.Project
import UI.FxApp
import UI.Project.ProjectController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, Label, ListView, ScrollPane, TextField}
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

import scala.::

class MenuController {
  @FXML
  private var button1: Button = _
  @FXML
  private var textField1: TextField = _
  @FXML
  private var textField2: TextField = _
  @FXML
  private var scroll: ScrollPane= _
  @FXML
  private var listView1: ListView[HBox] = _

  var vbox = new VBox(8) // spacing = 8
  var int = 0

  def onButton1Clicked(): Unit = {
    val buttonD = new Button("Delete")
    val buttonV = new Button("View")

    buttonD.setId(int.toString)
    buttonV.setId(int.toString)
    int = int + 1
    buttonD.setOnMouseClicked(event => deleteProject(buttonD,event))
    buttonV.setOnMouseClicked(event => openProject(buttonV,event))
    vbox.getChildren.addAll(new HBox(new Label("Projeto " + int.toString+ "                "), buttonD,buttonV))
  }

  def deleteProject(btn:Button,event: MouseEvent): Unit = {
    var intTask = btn.getId
    print(intTask.toString)
    vbox.getChildren.remove(btn.getParent)

  }

  def openProject(btn:Button,event: MouseEvent): Unit = {
    var intTask = btn.getId
    print(intTask.toString)
    val fxmlLoader = new FXMLLoader(getClass.getResource("../Project/ProjectController.fxml"))
    fxmlLoader.setController(fxmlLoader.getController[ProjectController].setID(intTask.toInt + 1))
    val mainViewRoot: Parent = fxmlLoader.load()
    btn.getScene.setRoot(mainViewRoot)

  }

  @FXML def initialize(): Unit = {
    scroll.setContent(vbox)
    val button = new Button("Delete")
    button.setId(int.toString)
    int = int + 1
    button.setOnMouseClicked(event => deleteProject(button,event))
    vbox.getChildren.addAll(new HBox(new Label("Projeto " + int.toString+ "                "), button))

  }


}
