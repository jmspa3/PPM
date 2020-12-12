package UI.Project

import UI.Menu.MenuController
import javafx.fxml.FXML
import java.time._

import PPMProject.{Database, HighPriority, LowPriority, MediumPriority, Project, Task, User}
import javafx.application.Platform
import javafx.scene.control.{Button, ChoiceBox, DatePicker, Label, TextArea, TextField}

class CreateTaskController {

   @FXML
   var createTaskButton: Button = _
   @FXML
   var nameTextField: TextField = _
   @FXML
   var descriptionTextArea: TextArea = _
   @FXML
   var priorityChoiceBox: ChoiceBox[String] = _
   @FXML
   var deadlineDatePicker: DatePicker = _
   @FXML
   private var errorLabel: Label = _
   var taskName: String = _
   var taskDescription: String = _
   var taskPriority: String = _
   var taskDeadline: LocalDate = _
   var parent: ProjectController = _
   private var project: Project = _
   private var user: User = _
   private var database: Database = _

   def initialize(): Unit = {
      Platform.runLater(() => nameTextField.getParent.requestFocus)
      deadlineDatePicker.setValue(LocalDate.now())
      priorityChoiceBox.getItems.add("High Priority")
      priorityChoiceBox.setValue("High Priority")
      priorityChoiceBox.getItems.add("Medium Priority")
      priorityChoiceBox.getItems.add("Low Priority")
   }

   def createTaskClicked(): Unit = {
      taskName = nameTextField.getText
      if (!taskName.isEmpty){
         taskDescription = descriptionTextArea.getText
         taskPriority = {
            priorityChoiceBox.getValue match {
               case "High Priority" => priorityChoiceBox.getValue
               case "Medium Priority" => priorityChoiceBox.getValue
               case "Low Priority" => priorityChoiceBox.getValue
            }
         }
         taskDeadline = deadlineDatePicker.getValue
         val savedTasks = database.getTableByName("Task")
         val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
         val t = new Task({
            if (savedTasks.records.values.size > 0) savedTasks.records.values.last.asInstanceOf[Task].getId() + 1 else 0
         }, user.getId, projectEntry._2.getId, deadline = taskDeadline, name = taskName, description = "", priority = {
            taskPriority match {
               case "High Priority" => HighPriority;
               case "Medium Priority" => MediumPriority;
               case "Low Priority" => LowPriority;
            }
         })
         val newProject = projectEntry._2.addTask(t)
         val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
         val newDatabase = tempDatabase.insertInTable(t, "Task")
         parent.setData(newProject, user, newDatabase)
         createTaskButton.getScene.getWindow.hide
      }
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

   def setData(project: Project, user: User, database: Database): Unit = {
      this.project = project
      this.user = user
      this.database = database
   }

   def checkIfEmptyTextField(): Unit = {
      if (nameTextField.getText.isEmpty)
      {
         errorLabel.setVisible(true)
      }
      else
      {
         errorLabel.setVisible(false)
      }
   }
}
