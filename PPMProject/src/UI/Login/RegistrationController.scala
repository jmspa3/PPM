package UI.Login

import PPMProject.{Database, StorageManager, User}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.{Label, TextField}

class RegistrationController {

   @FXML
   private var usernameTextField: TextField = _
   @FXML
   private var usernameInUseLabel: Label = _

   private var database: Database = _
   private var parent: LoginController = _

   def registerButtonClicked(){
      val username = usernameTextField.getText()
      val userTable = database.getTableByName("User")
      val usernamelist = userTable.records.values.toList.asInstanceOf[List[User]]
      if (usernamelist.filter(x => x.getUsername.equals(username)).size !=  0) usernameInUseLabel.setVisible(true)
      else {
         usernameInUseLabel.setVisible(false)
         val us = new User({
            if (userTable.records.values.size > 0) userTable.records.values.last.asInstanceOf[User].getId + 1 else 0
         }, username)
         val newDatabase = database.insertInTable(us, "User")
         parent.setData(newDatabase)
         StorageManager.saveDatabase(newDatabase, parent.databasePath)
         usernameTextField.getScene.getWindow.hide
      }
   }

   def setData(database: Database): Unit = {
      this.database = database
   }

   def setParent(parent: LoginController): Unit = {
      this.parent = parent
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => usernameInUseLabel.getParent.requestFocus)
   }
}
