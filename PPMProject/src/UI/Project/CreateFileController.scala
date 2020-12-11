package UI.Project

import PPMProject.{Database, Project, SharedFile, User}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.stage.{FileChooser, Stage}

class CreateFileController {

   @FXML
   private var filePathTextField: TextField = _

   private var parentController: ProjectController = _
   private var project: Project = _
   private var user: User = _
   private var database: Database = _

   def setParent(parentController: ProjectController): Unit = {
      this.parentController = parentController
   }

   def openFileChooser(): Unit = {
      var fileChooserStage = new Stage()
      var fileChooser = new FileChooser
      fileChooser.setTitle("Upload new file: ")
      val file = fileChooser.showOpenDialog(fileChooserStage)
      val filePath = file.getAbsolutePath
      filePathTextField.setText(filePath)
   }

   def createFileClicked(): Unit = {
      val fileName = filePathTextField.getText.split('\\').last
      val filePath = filePathTextField.getText()
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val savedFiles = database.getTableByName("SharedFile")
      val sh = new SharedFile({
         if (savedFiles.records.values.size > 0) savedFiles.records.values.last.asInstanceOf[SharedFile].getId() + 1 else 0
      }, user.getId, project.getId, fileName = fileName, path = filePath)
      val newProject = projectEntry._2.addFile(sh)
      val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      val newDatabase = tempDatabase.insertInTable(sh, "SharedFile")
      parentController.setData(newProject, user, newDatabase)
      filePathTextField.getScene.getWindow.hide
   }

   def setData(project: Project, user: User, database: Database): Unit =
   {
      this.project = project
      this.user = user
      this.database = database
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => filePathTextField.getParent.requestFocus)
   }
}
