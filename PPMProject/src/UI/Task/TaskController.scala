package UI.Task

import javafx.fxml.FXML
import javafx.scene.control.{Label, TextArea}
import javafx.scene.paint.Color

class TaskController {

   @FXML
   private var taskNameLabel: Label = _
   @FXML
   private var descriptionTextArea: TextArea = _
   @FXML
   private var deadlineLabel: Label = _
   @FXML
   private var priorityLabel: Label = _

   private var taskName: String = _
   private var taskDescription: String = _
   private var taskPriority: String = _
   private var taskDeadline: String = _

   def setData(task: (String, String, String, String)): Unit = {
      taskName = task._1
      taskNameLabel.setText(taskName)
      taskDescription = task._2
      descriptionTextArea.setText(taskDescription)
      taskPriority = task._3
      priorityLabel.setText(taskPriority)
      taskPriority match {
         case "High priority" => priorityLabel.setTextFill(Color.DARKRED)
         case "Medium priority" => priorityLabel.setTextFill(Color.YELLOW)
         case "Low priority" => priorityLabel.setTextFill(Color.GREEN)
      }
      taskDeadline = task._4
      deadlineLabel.setText(taskDeadline)
   }
}
