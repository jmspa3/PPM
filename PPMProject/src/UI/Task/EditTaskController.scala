package UI.Task

import java.time.LocalDate

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


   private var parent: TaskController = _

   def setInitialValues(task: (String, String, String, String)): Unit = {
      nameTextField.setText(task._1)
      descriptionTextArea.setText(task._2)
      priorityChoiceBox.getItems.add("High priority")
      priorityChoiceBox.getItems.add("Medium priority")
      priorityChoiceBox.getItems.add("Low priority")
      priorityChoiceBox.setValue(task._3)
      deadlineDatePicker.setValue(LocalDate.now)
   }

   def editTaskClicked(): Unit ={
      val task = (nameTextField.getText, descriptionTextArea.getText, priorityChoiceBox.getValue, deadlineDatePicker.getValue.toString)
      parent.editTask(task)
      nameTextField.getScene.getWindow.hide
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

}
