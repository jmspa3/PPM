package UI.Login

import java.util

import UI.Menu.MenuController
import UI.Project
import javafx.collections.FXCollections
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, ListView, TextField}
import javafx.scene.layout.Region
import javafx.stage.Stage
class LoginController {
  @FXML
  private var button1: Button = _
  @FXML
  private var textField1: TextField = _
  def onButton1Clicked(): Unit = {

    println("Hello World")
    val fxmlLoader = new FXMLLoader(getClass.getResource("../Menu/MenuController.fxml"))
    val username = textField1.getText
    val root = fxmlLoader.load.asInstanceOf[Region]
    fxmlLoader.getController[MenuController].setUser(username)
    button1.getScene.setRoot(root)
  }


}