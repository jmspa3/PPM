package UI.Task

import PPMProject.{Database, Task}
import UI.Project.{AddMemberController, ProjectController}
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
   private var parent: ProjectController = _
   private var task: Task = _
   private var database: Database = _


   def setData(task: Task, database: Database): Unit = {
      taskNameLabel.setText(task.getName())
      descriptionTextArea.setText(task.getDescription)
      priorityLabel.setText(task.getPriority())
      task.getPriority() match {
         case "Medium Priority" => priorityLabel.setTextFill(Color.DARKRED)
         case "Medium Priority" => priorityLabel.setTextFill(Color.YELLOW)
         case "Low Priority" => priorityLabel.setTextFill(Color.GREEN)
      }
      deadlineLabel.setText(task.getDeadline().toString)
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
      fxmlLoader.getController[EditTaskController].setInitialValues(task)
      modalStage.show
   }

   def editTask(task: Task): Unit = {
      setData(task, database)
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
