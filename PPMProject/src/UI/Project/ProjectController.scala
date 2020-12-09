package UI.Project

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, TextField}
class ProjectController {
  @FXML
  private var button1: Button = _
  @FXML
  private var textField1: TextField = _
  def onButton1Clicked(): Unit = {
    println("Hello World")
  }
}