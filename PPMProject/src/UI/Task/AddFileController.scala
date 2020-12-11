package UI.Task

import PPMProject.{Database, SharedFile, Task}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.layout.HBox

class AddFileController {

   @FXML
   private var fileListView: ListView[HBox] = _

   private var task: Task = _
   private var database: Database = _
   private var parent: TaskController = _

   def createFileList(files: List[SharedFile]): Unit = {
      files match {
         case Nil => ;
         case h::t => {
            val buttonA = new Button("Add")
            val label = new Label(h.getName())
            if (task.getFiles(database).contains(h)) {
               label.setDisable(true)
               buttonA.setDisable(true)
            }
            buttonA.setOnMouseClicked(event => addSharedFile(h, label, buttonA))
            label.setMaxWidth(363)
            label.setPrefWidth(label.getMaxWidth)
            fileListView.getItems.add(new HBox(label, buttonA))
            createFileList(t)
         }
      }
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

   def addSharedFile(file: SharedFile, label: Label, button: Button): Unit ={
      val newTask = task.addFile(file)
      val taskEntry = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == task.getId).get
      val newDatabase = database.swapTable("Task", database.getTableByName("Task").updateTable(taskEntry, newTask))
      setData(newTask, newDatabase)
      parent.addFile(newTask, newDatabase)
   }

   def doneButtonClicked(): Unit ={
      fileListView.getScene.getWindow.hide
   }

   def setData(task: Task, database: Database): Unit = {
      this.task = task
      this.database = database
      fileListView.getItems.clear
      createFileList(task.getProject(database).getFiles(database))
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => fileListView.getParent.requestFocus)
   }
}
