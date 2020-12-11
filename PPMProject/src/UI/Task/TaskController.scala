package UI.Task

import PPMProject.{Database, Project, SharedFile, Task, User}
import UI.Project.{AddMemberController, ProjectController}
import javafx.application.Platform
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Label, ListView, TextArea}
import javafx.scene.layout.HBox
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
   private var memberListView: ListView[HBox] = _
   @FXML
   private var fileListView: ListView[HBox] = _

   private var parentRoot: Parent = _
   private var parent: ProjectController = _
   private var task: Task = _
   private var database: Database = _


   def setData(task: Task, database: Database): Unit = {
      this.task = task
      this.database = database
      setInitialValues
   }

   def setInitialValues(): Unit = {
      taskNameLabel.setText(task.getName())
      descriptionTextArea.setText(task.getDescription)
      priorityLabel.setText(task.getPriority())
      task.getPriority() match {
         case "High Priority" => priorityLabel.setTextFill(Color.DARKRED)
         case "Medium Priority" => priorityLabel.setTextFill(Color.YELLOW)
         case "Low Priority" => priorityLabel.setTextFill(Color.GREEN)
      }
      deadlineLabel.setText(task.getDeadline().toString)
      setFileList()
      setMemberList()
   }

   def setMemberList(): Unit = {
      memberListView.getItems.clear
      task.getMembers(database).map(createMemberItem(_))
   }

   def createMemberItem(user: User): Unit = {
         val buttonD = new Button("Delete")
         buttonD.setOnMouseClicked(event => deleteMember(user.getId))
         val label = new Label(user.getUsername)
         label.setMaxWidth(152)
         label.setPrefWidth(label.getMaxWidth)
         memberListView.getItems.add(new HBox(label, buttonD))
   }

   def deleteFile(fileId: Int): Unit = {
      val taskEntry = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == task.getId).get
      val newTask = taskEntry._2.removeFile(fileId)
      val newDatabase = database.swapTable("Task", database.getTableByName("Task").updateTable(taskEntry, newTask))
      setData(newTask, newDatabase)
   }

   def setFileList(): Unit = {
      fileListView.getItems.clear
      task.getFiles(database).map(createFileItem(_))
   }

   def createFileItem(file: SharedFile): Unit = {
      val buttonD = new Button("Delete")
      buttonD.setOnMouseClicked(event => deleteFile(file.getId))
      val label = new Label(file.getName())
      label.setMaxWidth(152)
      label.setPrefWidth(label.getMaxWidth)
      fileListView.getItems.add(new HBox(label, buttonD))
   }

   def deleteMember(userId: Int): Unit = {
      val taskEntry = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == task.getId).get
      val newTask = taskEntry._2.removeMember(userId)
      val newDatabase = database.swapTable("Task", database.getTableByName("Task").updateTable(taskEntry, newTask))
      setData(newTask, newDatabase)
   }

   def editTaskModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Edit your task")
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("EditTaskController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[EditTaskController].setParent(this)
      fxmlLoader.getController[EditTaskController].setData(task, database)
      modalStage.show
   }

   def newMemberModal() = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Who is responsible for this task?")
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("AddMemberTaskController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddMemberTaskController].setData(task, database)
      fxmlLoader.getController[AddMemberTaskController].setParent(this)
      modalStage.show
   }

   def addMember(task: Task, database: Database): Unit = {
      setData(task, database)
   }

   def newFileModal() = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("What files are involved in this task?")
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(taskNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("AddFileController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddFileController].setData(task, database)
      fxmlLoader.getController[AddFileController].setParent(this)
      modalStage.show
   }

   def addFile(task: Task, database: Database): Unit ={
      setData(task, database)
   }

   def backButtonClicked(): Unit = {
      parent.setData(task.getProject(database), task.getOwner(database), database)
      taskNameLabel.getScene.setRoot(parentRoot)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => taskNameLabel.getParent.requestFocus)
   }
}
