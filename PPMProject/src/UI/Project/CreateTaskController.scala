package UI.Project

import UI.Menu.MenuController
import javafx.fxml.FXML
import java.time._

import PPMProject.{Database, HighPriority, LowPriority, MediumPriority, Project, Task, User}
import javafx.scene.control.{Button, ChoiceBox, DatePicker, TextArea, TextField}

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
   var taskName: String = _
   var taskDescription: String = _
   var taskPriority: String = _
   var taskDeadline: LocalDate = _
   var parent: ProjectController = _
   private var project: Project = _
   private var user: User = _
   private var database: Database = _

   def initialize(): Unit = {
      deadlineDatePicker.setValue(LocalDate.now())
      priorityChoiceBox.getItems.add("High priority")
      priorityChoiceBox.setValue("High priority")
      priorityChoiceBox.getItems.add("Medium priority")
      priorityChoiceBox.getItems.add("Low priority")
   }

   def createTaskClicked(): Unit = {
      taskName = nameTextField.getText
      taskDescription = descriptionTextArea.getText
      taskPriority = { priorityChoiceBox.getValue match {
         case "High priority" => priorityChoiceBox.getValue
         case "Medium priority" => priorityChoiceBox.getValue
         case "Low priority" => priorityChoiceBox.getValue
      }}
      taskDeadline = deadlineDatePicker.getValue
      val savedTasks = database.getTableByName("Task")
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == project.getId).get
      val t = new Task({
         if (savedTasks.records.values.size > 0) savedTasks.records.values.last.asInstanceOf[Task].getId() + 1 else 0
      }, user.getId, projectEntry._2.getId, deadline = taskDeadline, name = taskName, description = "", priority = {
         taskPriority match {
            case "2" => MediumPriority;
            case "3" => HighPriority;
            case _ => LowPriority;
         }
      })
      val newProject = projectEntry._2.addTask(t)
      val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
      val newDatabase = tempDatabase.insertInTable(t, "Task")
      parent.setData(newProject, user, newDatabase)
      createTaskButton.getScene.getWindow.hide
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }

   def setData(project: Project, user: User, database: Database): Unit = {
      this.project = project
      this.user = user
      this.database = database
   }
}
