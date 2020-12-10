package UI.Project

import UI.Task.TaskController
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, ChoiceBox, Label, ScrollPane, TextArea, TextField}
import javafx.scene.input.MouseEvent
import javafx.scene.layout.{AnchorPane, HBox, Region, VBox}
import javafx.stage.{Modality, Stage}

class ProjectController {
   @FXML
   private var projectNameLabel: Label = _
   @FXML
   private var projectDescriptionTextArea: TextArea = _
   @FXML
   private var fileListScroll: ScrollPane = _
   @FXML
   private var taskListScroll: ScrollPane = _
   @FXML
   private var creatorLabel: Label = _
   @FXML
   private var createdDateLabel: Label = _
   @FXML
   private var filterChoiceBox: ChoiceBox[String] = _

   private var projectName : String = _
   private var projectDescription : String = _
   private var fileNum: Int = 0
   private var taskNum: Int = 0
   var fileList = new VBox(8) // spacing = 8
   var taskList = new VBox(8) // spacing = 8


   def onSelected(): Unit = {
      println("Hello World")
   }
   def setData(projectName: String, projectDescription: String) {
      this.projectName = projectName
      this.projectDescription = projectDescription
      projectNameLabel.setText(projectName)
      projectDescriptionTextArea.setText(projectDescription)
   }

   def initialize(): Unit ={
      fileListScroll.setContent(fileList)
      taskListScroll.setContent(taskList)
      initInfoTextArea()
      filterChoiceBox.getItems.add("All tasks")
      filterChoiceBox.setValue("All tasks")
      filterChoiceBox.getItems.add("High priority")
      filterChoiceBox.getItems.add("Medium priority")
      filterChoiceBox.getItems.add("Low priority")
   }

   def initInfoTextArea(): Unit ={
      creatorLabel.setText("Created by: " + "")
      createdDateLabel.setText("Created in: " + "")
   }

   def createFile(): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setId(fileNum.toString)
      buttonV.setId(fileNum.toString)
      fileNum += 1
      buttonD.setOnMouseClicked(event => deleteSharedFile(buttonD, event))
      buttonV.setOnMouseClicked(event => openSharedFile(buttonV, event))
      fileList.getChildren.addAll(new HBox(new Label("file " + fileNum), buttonD,buttonV))
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
      modalStage.show
   }

   def createTask(taskName: String, taskDescription: String, taskPriority: String, taskDeadline: String): Unit = {
      val buttonD = new Button("Delete")
      val buttonV = new Button("View")
      buttonD.setId(taskNum.toString)
      buttonV.setId(taskNum.toString)
      taskNum += 1
      buttonD.setOnMouseClicked(event => deleteTask(buttonD, event))
      buttonV.setOnMouseClicked(event => openTask((taskName, taskDescription, taskPriority, taskDeadline)))
      taskList.getChildren.addAll(new HBox(new Label(taskName), buttonD,buttonV))
   }

   def deleteSharedFile(button: Button, event: MouseEvent): Unit = {
      fileList.getChildren.remove(button.getParent)
   }

   def deleteTask(button: Button, event: MouseEvent): Unit = {
      taskList.getChildren.remove(button.getParent)
   }

   def openSharedFile(button: Button, event: MouseEvent): Unit = {

   }

   def openTask(task: (String, String, String, String)): Unit = {
      val fxmlLoader = new FXMLLoader(getClass.getResource("../Task/TaskController.fxml"))
      val root = fxmlLoader.load.asInstanceOf[Region]
      fxmlLoader.getController[TaskController].setData(task)
      projectNameLabel.getScene.setRoot(root)
   }

   def newMember() = {

   }

   def applyFilter(): Unit = {
      filterChoiceBox.getValue match {
         case "All Tasks" => println(filterChoiceBox.getValue)
         case "High priority" =>println(filterChoiceBox.getValue)
         case "Medium priority" =>println(filterChoiceBox.getValue)
         case "Low priority" =>println(filterChoiceBox.getValue)
      }
   }

}