package UI.Task

import java.time.LocalDate

import PPMProject.{HighPriority, LowPriority, MediumPriority, Task}
import PPMProject.Task.getDescription
import UI.Project.ProjectController
import javafx.fxml.FXML
import javafx.scene.control.{ChoiceBox, DatePicker, TextArea, TextField}

class EditTaskController {

   @FXML
   private var nameTextField: TextField = _
   @FXML
   private var descriptionTextArea: TextArea = _
   @FXML
   private var priorityChoiceBox: ChoiceBox[String] = _
   @FXML
   private var deadlineDatePicker: DatePicker = _

   private var task: Task = _


   private var parent: TaskController = _

   def setInitialValues(task: Task): Unit = {
      nameTextField.setText(task.getName)
      descriptionTextArea.setText(getDescription(task))
      priorityChoiceBox.getItems.add("High priority")
      priorityChoiceBox.getItems.add("Medium priority")
      priorityChoiceBox.getItems.add("Low priority")
      priorityChoiceBox.setValue(task.getPriority())
      deadlineDatePicker.setValue(task.getDeadline())
   }

   def editTaskClicked(): Unit ={
      val newPriority =  { priorityChoiceBox.getValue match {
         case "Low priority" => LowPriority;
         case "Medium priority" => MediumPriority;
         case "High priority" => HighPriority;
         }
      }
      val newTask = task.editName(nameTextField.getText()).editDescription(descriptionTextArea.getText).editPriority(newPriority).editDeadline(deadlineDatePicker.getValue)
      parent.editTask(task)
      nameTextField.getScene.getWindow.hide
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

   def setData(task: Task): Unit = {

   }

}
