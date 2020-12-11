package UI.Project

import PPMProject.Project
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{TextArea, TextField}

class EditProjectController {

   @FXML
   private var nameTextField: TextField = _
   @FXML
   private var descriptionTextArea: TextArea = _

   private var parent: ProjectController = _
   private var project: Project = _

   def editProjectClicked(): Unit ={
      val newProject = project.editName(nameTextField.getText).editDescription(descriptionTextArea.getText)
      parent.editProject(newProject)
      nameTextField.getScene.getWindow.hide
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

}
