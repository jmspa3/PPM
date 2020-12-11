package UI.Project

import PPMProject.{Database, Project, SavedClass, StorageManager, User}
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}

class AddMemberController {
   @FXML
   private var usernameTextField: TextField = _
   @FXML
   private var errorLabel: Label = _

   private var parent: ProjectController = _
   private var project: Project = _
   private var database: Database = _

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

   def setData(project: Project, database: Database): Unit = {
      this.project = project
      this.database = database
   }

   def addMemberClicked(): Unit = {
      val username = usernameTextField.getText()
      val userTable = database.getTableByName("User")
      val usernamelist = userTable.records.values.toList.asInstanceOf[List[User]]
      if (usernamelist.filter(x => x.getUsername.equals(username)).size ==  0) {
         errorLabel.setText("We could not find a user with that username!")
         errorLabel.setVisible(true)
      }
      else {
         val newUser = database.getTableByName("User").records.asInstanceOf[Map[Int, User]].find(x => x._2.getUsername == username).get._2
         if (project.getMembers(database).contains(newUser)) {
            errorLabel.setText("That user is already part of the project!")
            errorLabel.setVisible(true)
         }
         else {
            errorLabel.setVisible(false)
            val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
            val newProject = project.addMember(newUser)
            val newDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
            parent.addMember(newDatabase)
            usernameTextField.getScene.getWindow.hide
         }
      }
   }
}
