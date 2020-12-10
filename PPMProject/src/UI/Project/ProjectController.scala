package UI.Project

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, Label, TextField}

class ProjectController {
  @FXML
  private var button1: Button = _
  @FXML
  private var textField1: TextField = _
  @FXML
  private var label1: Label = _
  private var data : String = _


  def onSelected(): Unit = {
    println("Hello World")
  }
  def setID(id: Int) {
    data = id.toString
    println(data)
  }

  def initialize(): Unit ={
    label1.setText(data + "hshshsas" + data)
    println(data + "ze")
  }

}