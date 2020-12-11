package UI.Project

import PPMProject.Main.{databasePath, projectMenu}
import PPMProject.{Database, Project, SharedFile, StorageManager, Task, User}
import UI.Menu.MenuController
import UI.SharedFile.SharedFileController
import UI.Task.TaskController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, ChoiceBox, Label, ListView, ScrollPane, TextArea, TextField}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{AnchorPane, HBox, Region, VBox}
import javafx.stage.{Modality, Stage}

class ProjectController {
   @FXML
   private var projectNameLabel: Label = _
   @FXML
   private var projectDescriptionTextArea: TextArea = _
   @FXML
   private var creatorLabel: Label = _
   @FXML
   private var createdDateLabel: Label = _
   @FXML
   private var fileListView: ListView[HBox] = _
   @FXML
   private var taskListView: ListView[HBox] = _
   @FXML
   private var memberListView: ListView[Label] = _
   @FXML
   private var filterChoiceBox: ChoiceBox[String] = _

   private var project: Project = _
   private var user: User = _
   private var database: Database = _
   private var parentRoot: Parent = _
   private var parent: MenuController = _

   def onSelected(): Unit = {
      println("Hello World")
   }
   def setData(project: Project, user: User, database: Database) {
      this.project = project
      this.database = database
      this.user = user
      projectNameLabel.setText(project.getProjectName)
      projectDescriptionTextArea.setText(project.getProjectDescription)
      initInfoTextArea()
      setFileList()
      setTaskList()
   }

   def initialize(): Unit ={
      filterChoiceBox.getItems.add("All tasks")
      filterChoiceBox.setValue("All tasks")
      filterChoiceBox.getItems.add("High priority")
      filterChoiceBox.getItems.add("Medium priority")
      filterChoiceBox.getItems.add("Low priority")
   }

   def initInfoTextArea(): Unit ={
      creatorLabel.setText("Created by: " + project.getOwner(database).getUsername)
      createdDateLabel.setText("Created in: " + project.getCreationDate)
      projectDescriptionTextArea.setText(project.getProjectDescription)
   }

   def newFileModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(projectNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("CreateFileController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[CreateFileController].setParent(this)
      fxmlLoader.getController[CreateFileController].setData(project, user, database)
      modalStage.show
   }

   def createFileItem(file: SharedFile): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setOnMouseClicked(event => deleteSharedFile(buttonD, file.getId))
      buttonV.setOnMouseClicked(event => openSharedFile(file.getId))
      fileListView.getItems.add(new HBox(new Label(file.getName), buttonD, buttonV))
   }

   def createTaskItem(task: Task): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setOnMouseClicked(event => deleteTask(buttonD, task.getId))
      buttonV.setOnMouseClicked(event => openTask(task.getId))
      taskListView.getItems.add(new HBox(new Label(task.getName), buttonD, buttonV))
   }

   def setFileList(): Unit = {
      fileListView.getItems.clear
      project.getFiles(database).map(createFileItem(_))
   }

   def setTaskList(): Unit = {
      taskListView.getItems.clear
      project.getTasks(database).map(createTaskItem(_))
   }

   def newTaskModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(projectNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("CreateTaskController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[CreateTaskController].setParent(this)
      fxmlLoader.getController[CreateTaskController].setData(project, user, database)
      modalStage.show
   }

   def deleteSharedFile(button: Button, fileId: Int): Unit = {
      val savedFiles = database.getTableByName("SharedFiles")
      val filesToMaintain = savedFiles.filterTable(fileId)
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val newProject = projectEntry._2.removeFile(fileId)
      val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      val newDatabase = tempDatabase.swapTable("SharedFile", filesToMaintain)
      this.database = newDatabase
      fileListView.getItems.remove(button.getParent)
   }

   def deleteTask(button: Button, taskId: Int): Unit = {
      val savedTasks = database.getTableByName("Task")
      val tasksToMaintain = savedTasks.filterTable(taskId)
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val newProject = projectEntry._2.removeTask(taskId)
      val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      val newDatabase = tempDatabase.swapTable("Task", tasksToMaintain)
      this.database = newDatabase
      taskListView.getItems.remove(button.getParent)
   }

   def openSharedFile(fileId: Int): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../SharedFile/SharedFileController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      val file = database.getTableByName("SharedFile").records.asInstanceOf[Map[Int, SharedFile]].find(x => x._2.id == fileId).get._2
      fxmlLoader.getController[SharedFileController].setFile(file)
      fxmlLoader.getController[SharedFileController].setParentRoot(projectNameLabel.getScene.getRoot)
      projectNameLabel.getScene.setRoot(root)
   }

   def openTask(taskId: Int): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Task/TaskController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      val task = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == taskId).get._2
      fxmlLoader.getController[TaskController].setData(task, database)
      fxmlLoader.getController[TaskController].setParentRoot(projectNameLabel.getScene.getRoot)
      projectNameLabel.getScene.setRoot(root)
   }

   def newMemberModal() = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(projectNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("AddMemberController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddMemberController].setFunc(addMember)
      modalStage.show
   }

   def addMember(username: String): Unit = {
      memberListView.getItems.add(new Label(username))
   }

   def editProjectModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(projectNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("EditProjectController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[EditProjectController].setParent(this)
      fxmlLoader.getController[EditProjectController].setData(project)
      modalStage.show
   }

   def editProject(newProject: Project): Unit = {
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val newDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      setData(project, user, newDatabase)
   }

   def applyFilter(): Unit = {
      filterChoiceBox.getValue match {
         case "All Tasks" => println(filterChoiceBox.getValue)
         case "High priority" =>println(filterChoiceBox.getValue)
         case "Medium priority" =>println(filterChoiceBox.getValue)
         case "Low priority" =>println(filterChoiceBox.getValue)
      }
   }

   def saveButtonClicked(): Unit = {
      StorageManager.saveDatabase(database, "savedDatabase.shdb")
   }

   def backButtonClicked(): Unit = {
      projectNameLabel.getScene.setRoot(parentRoot)
      parent.setData(user, database)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }

   def setParent(parent: MenuController): Unit =
   {
      this.parent = parent
   }

}