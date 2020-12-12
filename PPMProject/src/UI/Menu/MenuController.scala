package UI.Menu

import java.util

import PPMProject.{Database, Project, User}
import UI.FxApp
import UI.Login.LoginController
import UI.Project.ProjectController
import javafx.application.Platform
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Label, ListView, ScrollPane, TextField}
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{HBox, Region, VBox}
import javafx.stage.{Modality, Stage}

import scala.::

class MenuController {
   @FXML
   private var button1: Button = _
   @FXML
   private var projectListView: ListView[HBox] = _
   @FXML
   private var usernameLabel: Label = _
   @FXML
   private var registrationDateLabel: Label = _
   @FXML
   private var titleLabel: Label = _

   private var user: User = _
   private var database: Database = _
   private var parentRoot: Parent = _
   private var parent: LoginController = _


   def newProjectModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.setTitle("Create a new Project")
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(button1.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("CreateProjectController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[CreateProjectController].setParent(this)
      fxmlLoader.getController[CreateProjectController].setData(user, database)
      modalStage.show
   }

   def deleteProject(btn:Button, projectId: Int, event: MouseEvent): Unit = {
      val savedProjects = database.getTableByName("Project")
      val projectsToMaintain = savedProjects.filterTable(projectId)
      val newDatabase = database.swapTable("Project", projectsToMaintain)
      this.database = newDatabase
      projectListView.getItems.remove(btn.getParent)
   }


   def createProjectItem(project: Project): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setOnMouseClicked(event => deleteProject(buttonD, project.getId, event))
      buttonV.setOnMouseClicked(event => openProject(buttonV, project.getId, event))
      val label = new Label(project.getProjectName)
      label.setMaxWidth(589)
      label.setPrefWidth(label.getMaxWidth)
      projectListView.getItems.add(new HBox(label, buttonD, buttonV))
   }

   def openProject(btn:Button, projectId: Int, event: MouseEvent): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Project/ProjectController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      val project = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == projectId).get._2
      fxmlLoader.getController[ProjectController].setData(project, user, database)
      fxmlLoader.getController[ProjectController].setParentRoot(titleLabel.getScene.getRoot)
      fxmlLoader.getController[ProjectController].setParent(this)
      btn.getScene.setRoot(root)
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => titleLabel.getParent.requestFocus)
   }

   def setData(user: User, database: Database): Unit = {
      this.user = user
      setUserInfo()
      this.database = database
      setProjectList()
   }

   def setUserInfo(): Unit = {
      usernameLabel.setText("Username: " + user.getUsername)
      registrationDateLabel.setText("Registration Date: " + user.getCreationDate)
   }

   def setProjectList(): Unit ={
      projectListView.getItems.clear
      user.getParticipatingProjects(database).reverse.map(createProjectItem(_))
   }

   def logoutButtonClicked(): Unit = {
      titleLabel.getScene.setRoot(parentRoot)
      parent.setData(database)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }

   def setParent(parent: LoginController): Unit =
   {
      this.parent = parent
   }

}
