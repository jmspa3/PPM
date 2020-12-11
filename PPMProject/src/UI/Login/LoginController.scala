package UI.Login

import java.util

import PPMProject.{Database, StorageManager, User}
import PPMProject.Main.databasePath
import UI.Menu.{CreateProjectController, MenuController}
import UI.Project
import javafx.collections.FXCollections
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, ListView, TextField}
import javafx.scene.layout.Region
import javafx.stage.{Modality, Stage}
class LoginController {
   @FXML
   private var button1: Button = _
   @FXML
   private var textField1: TextField = _

   val databasePath = "savedDatabase.shdb"
   private var database: Database = _

   def onButton1Clicked(): Unit = {
      val username = textField1.getText
      val userTable = database.getTableByName("User")
      val usernamelist = userTable.records.values.toList.asInstanceOf[List[User]]
      if (usernamelist.filter(x => x.getUsername.equals(username)).size ==  0) newUserModal()
      else {
         val fxmlLoader = new FXMLLoader(getClass.getResource("../Menu/MenuController.fxml"))
         val root = fxmlLoader.load.asInstanceOf[Region]
         val user = userTable.records.asInstanceOf[Map[Int, User]].find(x => x._2.getUsername.equals(username)).get._2
         fxmlLoader.getController[MenuController].setData(user, database)
         button1.getScene.setRoot(root)
      }
   }

   def initialize(): Unit ={
      val database = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
      setData(database)
   }

   def newUserModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(button1.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("RegistrationController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[RegistrationController].setParent(this)
      fxmlLoader.getController[RegistrationController].setData(database)
      modalStage.show
   }

   def setData(database: Database): Unit = {
      this.database = database
   }


}