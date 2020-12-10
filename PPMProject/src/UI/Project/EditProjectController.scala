package UI.Project

import javafx.fxml.FXML
import javafx.scene.control.{TextArea, TextField}

class EditProjectController {

   @FXML
   private var nameTextField: TextField = _
   @FXML
   private var descriptionTextArea: TextArea = _

   private var parent: ProjectController = _

   def editProjectClicked(): Unit ={
      parent.editProject(nameTextField.getText, descriptionTextArea.getText)
      nameTextField.getScene.getWindow.hide
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

}
