package UI.Menu

import PPMProject.{Database, Project, User}
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
   private var database: Database = _
   private var user: User = _

   def createProjectClicked(): Unit = {
      projectName = nameTextField.getText
      projectDescription = descriptionTextArea.getText
      val existingProjects = database.getTableByName("Project")
      val pr = new Project({
         if (existingProjects.records.values.size > 0) existingProjects.records.values.last.asInstanceOf[Project].getId + 1 else 0
      }, user.getId, name = projectName, description = projectDescription)
      val newDatabase = database.insertInTable(pr, "Project")
      parentController.setData(user, newDatabase)
      createProjectButton.getScene.getWindow.hide
   }

   def setParent(parentController: MenuController): Unit =
   {
      this.parentController = parentController
   }

   def setData(user: User, database: Database): Unit = {
      this.user = user
      this.database = database
   }
}
