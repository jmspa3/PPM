package UI.Task

import UI.Project.AddMemberController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Label, ListView, TextArea}
import javafx.scene.paint.Color
import javafx.stage.{Modality, Stage}

class TaskController {

   @FXML
   private var taskNameLabel: Label = _
   @FXML
   private var descriptionTextArea: TextArea = _
   @FXML
   private var deadlineLabel: Label = _
   @FXML
   private var priorityLabel: Label = _
   @FXML
   private var memberListView: ListView[Label] = _
   @FXML
   private var fileListView: ListView[Label] = _

   private var parentRoot: Parent = _
   private var taskName: String = _
   private var taskDescription: String = _
   private var taskPriority: String = _
   private var taskDeadline: String = _


   def setData(task: (String, String, String, String)): Unit = {
      taskName = task._1
      taskNameLabel.setText(taskName)
      taskDescription = task._2
      descriptionTextArea.setText(taskDescription)
      taskPriority = task._3
      priorityLabel.setText(taskPriority)
      taskPriority match {
         case "High priority" => priorityLabel.setTextFill(Color.DARKRED)
         case "Medium priority" => priorityLabel.setTextFill(Color.YELLOW)
         case "Low priority" => priorityLabel.setTextFill(Color.GREEN)
      }
      taskDeadline = task._4
      deadlineLabel.setText(taskDeadline)
   }

   def editTaskModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("EditTaskController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[EditTaskController].setParent(this)
      fxmlLoader.getController[EditTaskController].setInitialValues((taskName, taskDescription, taskPriority, taskDeadline))
      modalStage.show
   }

   def editTask(task: (String, String, String, String)): Unit = {
      setData(task)
   }

   def newMemberModal() = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Project/AddMemberController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddMemberController].setFunc(addMember)
      modalStage.show
   }

   def addMember(username: String): Unit = {
      memberListView.getItems.add(new Label(username))
   }

   def newFileModal() = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("AddFileController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddFileController].setParent(this)
      fxmlLoader.getController[AddFileController].createFileList(List(("file1", "desc1"), ("file2", "desc2"), ("file3", "desc3")))
      modalStage.show
   }

   def addFile(file: (String, String)): Unit ={
      val newFileLabel = new Label(file._1)
      if (fileListView.getItems.filtered(x => x.getText.equals(file._1)).size == 0) fileListView.getItems.add(newFileLabel)
   }

   def backButtonClicked(): Unit = {
      taskNameLabel.getScene.setRoot(parentRoot)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }
}
