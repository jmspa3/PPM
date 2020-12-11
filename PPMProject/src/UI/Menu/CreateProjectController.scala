package UI.Menu

import UI.Menu.MenuController
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextArea, TextField}

class CreateProjectController {
   @FXML
   var createProjectButton: Button = _
   @FXML
   var nameTextField: TextField = _
   @FXML
   var descriptionTextArea: TextArea = _
   var projectName: String = _
   var projectDescription: String = _
   var parentController: MenuController = _

   def createProjectClicked(): Unit = {
      projectName = nameTextField.getText
      projectDescription = descriptionTextArea.getText
      parentController.createProject(projectName, projectDescription)
      createProjectButton.getScene.getWindow.hide
   }

   def setParent(parentController: MenuController): Unit =
   {
      this.parentController = parentController
   }
}
