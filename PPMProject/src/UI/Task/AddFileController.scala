package UI.Task

import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.layout.HBox

class AddFileController {

   @FXML
   private var fileListView: ListView[HBox] = _

   private var parent: TaskController = _

   def createFileList(files: List[(String, String)]): Unit = {
      files match {
         case Nil => ;
         case h::t => {
            val buttonA = new Button("Add")
            val label = new Label(h._1)
            buttonA.setOnMouseClicked(event => addSharedFile(h, label, buttonA))
            fileListView.getItems.add(new HBox(label, buttonA))
            createFileList(t)
         }
      }
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

   def addSharedFile(file: (String, String), label: Label, button: Button): Unit ={
      parent.addFile(file)
      label.setDisable(true)
      button.setDisable(true)
   }

   def doneButtonClicked(): Unit ={
      fileListView.getScene.getWindow.hide
   }


}
