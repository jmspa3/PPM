package UI.Project

import javafx.fxml.FXML
import javafx.scene.control.TextField

class AddMemberController {
   @FXML
   private var usernameTextField: TextField = _

   private var func: String => Unit = _

   def setFunc(func: String => Unit): Unit = {
      this.func = func
   }

   def addMemberClicked(): Unit = {
      func(usernameTextField.getText())
      usernameTextField.getScene.getWindow.hide
   }
}
