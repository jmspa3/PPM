package UI.Task

import java.time.LocalDate

import PPMProject.{Database, HighPriority, LowPriority, MediumPriority, Project, Task}
import PPMProject.Task.getDescription
import UI.Project.ProjectController
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{ChoiceBox, DatePicker, Label, TextArea, TextField}

class EditTaskController {

   @FXML
   private var nameTextField: TextField = _
   @FXML
   private var descriptionTextArea: TextArea = _
   @FXML
   private var priorityChoiceBox: ChoiceBox[String] = _
   @FXML
   private var deadlineDatePicker: DatePicker = _
   @FXML
   private var errorLabel: Label = _

   private var task: Task = _
   private var database: Database = _

   private var parent: TaskController = _

   def setInitialValues(): Unit = {
      nameTextField.setText(task.getName)
      descriptionTextArea.setText(getDescription(task))
      priorityChoiceBox.getItems.add("High Priority")
      priorityChoiceBox.getItems.add("Medium Priority")
      priorityChoiceBox.getItems.add("Low Priority")
      priorityChoiceBox.setValue(task.getPriority())
      deadlineDatePicker.setValue(task.getDeadline())
   }

   def editTaskClicked(): Unit ={
      if (!nameTextField.getText.isEmpty){
         val newPriority = {
            priorityChoiceBox.getValue match {
               case "Low Priority" => LowPriority;
               case "Medium Priority" => MediumPriority;
               case "High Priority" => HighPriority;
            }
         }
         val newTask = task.editName(nameTextField.getText()).editDescription(descriptionTextArea.getText).editPriority(newPriority).editDeadline(deadlineDatePicker.getValue)
         val taskEntry = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == task.getId).get
         val newDatabase = database.swapTable("Task", database.getTableByName("Task").updateTable(taskEntry, newTask))
         parent.setData(newTask, newDatabase)
         nameTextField.getScene.getWindow.hide
      }
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

   def setData(task: Task, database: Database): Unit = {
      this.database = database
      this.task = task
      setInitialValues()
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => nameTextField.getParent.requestFocus)
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
