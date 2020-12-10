package UI.Login

import java.util

import UI.Project
import javafx.collections.FXCollections
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, ListView, TextField}
class LoginController {
  @FXML
  private var button1: Button = _
  @FXML
  private var textField1: TextField = _
  def onButton1Clicked(): Unit = {
    println("Hello World")
    val fxmlLoader = new FXMLLoader(getClass.getResource("../Menu/MenuController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    button1.getScene().setRoot(mainViewRoot)

  }


}