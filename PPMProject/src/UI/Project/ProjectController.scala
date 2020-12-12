package UI.Project

import PPMProject.Main.{databasePath, projectMenu}
import PPMProject.{Comment, Database, Project, SharedFile, StorageManager, Task, User}
import UI.Menu.MenuController
import UI.SharedFile.SharedFileController
import UI.Task.TaskController
import javafx.application.Platform
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
   private var memberListView: ListView[HBox] = _
   @FXML
   private var filterChoiceBox: ChoiceBox[String] = _

   private var project: Project = _
   private var user: User = _
   private var database: Database = _
   private var parentRoot: Parent = _
   private var parent: MenuController = _

   def setData(project: Project, user: User, database: Database) {
      this.project = project
      this.database = database
      this.user = user
      initInfoTextArea()
      setFileList()
      setTaskList()
      setMemberList()
   }

   def initialize(): Unit ={
      Platform.runLater(() => projectNameLabel.getParent.requestFocus)
      filterChoiceBox.getItems.add("All Tasks")
      filterChoiceBox.setValue("All Tasks")
      filterChoiceBox.getItems.add("High Priority")
      filterChoiceBox.getItems.add("Medium Priority")
      filterChoiceBox.getItems.add("Low Priority")
   }

   def initInfoTextArea(): Unit ={
      creatorLabel.setText("Created by: " + project.getOwner(database).getUsername)
      createdDateLabel.setText("Created in: " + project.getCreationDate)
      projectNameLabel.setText(project.getProjectName)
      projectDescriptionTextArea.setText(project.getProjectDescription)
   }

   def newFileModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Upload a new file")
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
      val label = new Label(file.getName)
      label.setMaxWidth(120)
      label.setPrefWidth(label.getMaxWidth)
      fileListView.getItems.add(new HBox(label, buttonD, buttonV))
   }

   def createTaskItem(task: Task): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setOnMouseClicked(event => deleteTask(buttonD, task.getId))
      buttonV.setOnMouseClicked(event => openTask(task.getId))
      val label = new Label(task.getName)
      label.setMaxWidth(120)
      label.setPrefWidth(label.getMaxWidth)
      taskListView.getItems.add(new HBox(label, buttonD, buttonV))
   }

   def createMemberItem(user: User): Unit = {
      if (project.getOwner(database).equals(user)) memberListView.getItems.add(new HBox(new Label(user.getUsername + " (owner)")))
      else {
         val buttonD = new Button("Delete")
         buttonD.setOnMouseClicked(event => deleteMember(buttonD, user.getId))
         val label = new Label(user.getUsername)
         label.setMaxWidth(307)
         label.setPrefWidth(label.getMaxWidth)
         val item = new HBox(label, buttonD)
         memberListView.getItems.add(item)
      }
   }

   def setFileList(): Unit = {
      fileListView.getItems.clear
      project.getFiles(database).reverse.map(createFileItem(_))
   }

   def setTaskList(): Unit = {
      taskListView.getItems.clear
      filterChoiceBox.getValue match {
         case "All Tasks" => project.getTasks(database).reverse.map(createTaskItem(_))
         case "High Priority" => project.getHighPriorityTasks(database).reverse.map(createTaskItem(_))
         case "Medium Priority" => project.getMediumPriorityTasks(database).reverse.map(createTaskItem(_))
         case "Low Priority" => project.getLowPriorityTasks(database).reverse.map(createTaskItem(_))
      }
   }

   def setMemberList(): Unit = {
      memberListView.getItems.clear
      project.getMembers(database).reverse.map(createMemberItem(_))
   }

   def newTaskModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Create a new task")
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
      val savedFiles = database.getTableByName("SharedFile")
      val filesToMaintain = savedFiles.filterTable(fileId)
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val newProject = projectEntry._2.removeFile(fileId)
      val commentsToMaintain = database.getTableByName("Comment").filterTableFromList(savedFiles.records.find(x => x._2.getId.equals(fileId)).get._2.asInstanceOf[SharedFile].getCommentIds)
      val tempDatabase1 = database.swapTable("Comment", commentsToMaintain)
      val tempDatabase2 = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      val newDatabase = tempDatabase2.swapTable("SharedFile", filesToMaintain)
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

   def deleteMember(button: Button, userId: Int): Unit = {
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val newProject = projectEntry._2.removeMember(userId)
      val newDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      setData(newProject, user, newDatabase)
   }

   def openSharedFile(fileId: Int): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../SharedFile/SharedFileController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      val file = database.getTableByName("SharedFile").records.asInstanceOf[Map[Int, SharedFile]].find(x => x._2.id == fileId).get._2
      fxmlLoader.getController[SharedFileController].setData(file, user, database)
      fxmlLoader.getController[SharedFileController].setParent(this)
      fxmlLoader.getController[SharedFileController].setParentRoot(projectNameLabel.getScene.getRoot)
      projectNameLabel.getScene.setRoot(root)
   }

   def openTask(taskId: Int): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Task/TaskController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      val task = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == taskId).get._2
      fxmlLoader.getController[TaskController].setData(task, database)
      fxmlLoader.getController[TaskController].setParent(this)
      fxmlLoader.getController[TaskController].setParentRoot(projectNameLabel.getScene.getRoot)
      projectNameLabel.getScene.setRoot(root)
   }

   def newMemberModal() = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Add a new member")
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(projectNameLabel.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("AddMemberController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[AddMemberController].setParent(this)
      fxmlLoader.getController[AddMemberController].setData(project, database)
      modalStage.show
   }

   def addMember(database: Database): Unit = {
      val newProject= database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get._2
      setData(newProject, user, database)
   }

   def editProjectModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Edit your project")
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
      setData(newProject, user, newDatabase)
   }

   def applyFilter(): Unit = {
      setTaskList()
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