package UI.Project

import UI.Menu.MenuController
import javafx.fxml.FXML
import java.time._
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
   var taskDeadline: String = _
   var parentController: ProjectController = _

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
      taskDeadline = deadlineDatePicker.getValue.toString
      parentController.createTask(taskName, taskDescription, taskPriority, taskDeadline)
      createTaskButton.getScene.getWindow.hide
   }

   def setParent(parentController: ProjectController): Unit =
   {
      this.parentController = parentController
   }
}
