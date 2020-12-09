package UI.Menu

import java.util

import PPMProject.Task
import UI.FxApp
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, Label, ListView, ScrollPane, TextField}
import javafx.collections.FXCollections
import javafx.collections.ObservableList
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


  def onButton1Clicked(): Unit = {
    vbox.getChildren.addAll(new HBox(new Label("Task1"), new Button("Delete")))
  }

  @FXML def initialize(): Unit = {

    vbox.getChildren.addAll(new HBox(new Label("Task1"), new Button("Delete")))
    vbox.getChildren.add(new HBox(new Label("Task1"), new Button("Delete")))
    vbox.getChildren.add(new HBox(new Label("Task1"), new Button("Delete")))
    scroll.setContent(vbox)
  }

  FxApp.task
}
