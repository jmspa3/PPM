package UI.Project

import PPMProject.Project
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextArea, TextField}

class EditProjectController {

   @FXML
   private var nameTextField: TextField = _
   @FXML
   private var descriptionTextArea: TextArea = _
   @FXML
   private var errorLabel: Label = _

   private var parent: ProjectController = _
   private var project: Project = _

   def editProjectClicked(): Unit ={
      if (!nameTextField.getText.isEmpty) {
         val newProject = project.editName(nameTextField.getText).editDescription(descriptionTextArea.getText)
         parent.editProject(newProject)
         nameTextField.getScene.getWindow.hide
      }
   }

   def setInitialValues(projectName: String, projectDescription: String): Unit =
   {
      nameTextField.setText(projectName)
      descriptionTextArea.setText(projectDescription)
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

   def setData(project: Project): Unit = {
      this.project = project
      setInitialValues(project.getProjectName, project.getProjectDescription)
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => nameTextField.getParent.requestFocus)
   }

   def checkIfEmptyTextField(): Unit = {
      if (nameTextField.getText.isEmpty)
      {
         errorLabel.setVisible(true)
      }
      else
      {
         errorLabel.setVisible(false)
      }
   }

}
