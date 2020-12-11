package UI.Menu

import java.util

import PPMProject.Project
import UI.FxApp
import UI.Project.ProjectController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Label, ListView, ScrollPane, TextField}
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{HBox, Region, VBox}
import javafx.stage.{Modality, Stage}

import scala.::

class MenuController {
   @FXML
   private var button1: Button = _
   @FXML
   private var textField1: TextField = _
   @FXML
   private var textField2: TextField = _
   @FXML
   private var projectListView: ListView[HBox] = _
   @FXML
   private var usernameLabel: Label = _
   @FXML
   private var titleLabel: Label = _
   var int = 0
   var user: String = _


   def newProjectModal(): Unit = {
      val modalStage: Stage = new Stage()
      modalStage.centerOnScreen()
      modalStage.initModality(Modality.APPLICATION_MODAL)
      modalStage.initOwner(button1.getScene.getWindow)
      val fxmlLoader = new FXMLLoader(getClass.getResource("CreateProjectController.fxml"))
      val mainViewRoot: Parent = fxmlLoader.load()
      val modalScene = new Scene(mainViewRoot)
      modalStage.setScene(modalScene)
      fxmlLoader.getController[CreateProjectController].setParent(this)
      modalStage.show
   }

   def deleteProject(btn:Button,event: MouseEvent): Unit = {
      var intTask = btn.getId
      print(intTask.toString)
      projectListView.getItems.remove(btn.getParent)
   }

   def createProject(projectName: String, projectDescription: String): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setId(int.toString)
      buttonV.setId(int.toString)
      int = int + 1
      buttonD.setOnMouseClicked(event => deleteProject(buttonD, event))
      buttonV.setOnMouseClicked(event => openProject(buttonV, (projectName, projectDescription), event))
      projectListView.getItems.add(new HBox(new Label(projectName), buttonD, buttonV))
   }

   def setUser(username: String): Unit =
   {
      this.user = username
      usernameLabel.setText("Username: " + user)
   }

   def openProject(btn:Button, projectInfo: (String, String), event: MouseEvent): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Project/ProjectController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      fxmlLoader.getController[ProjectController].setData(projectInfo._1, projectInfo._2)
      fxmlLoader.getController[ProjectController].setParentRoot(titleLabel.getScene.getRoot)
      btn.getScene.setRoot(root)
   }

   @FXML def initialize(): Unit = {

   }


}
