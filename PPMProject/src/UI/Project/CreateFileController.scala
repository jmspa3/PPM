package UI.Project

import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.stage.{FileChooser, Stage}

class CreateFileController {

   @FXML
   private var filePathTextField: TextField = _

   private var parentController: ProjectController = _

   def setParent(parentController: ProjectController): Unit = {
      this.parentController = parentController
   }

   def openFileChooser(): Unit = {
      var fileChooserStage = new Stage()
      var fileChooser = new FileChooser
      fileChooser.setTitle("Upload new file: ")
      val file = fileChooser.showOpenDialog(fileChooserStage)
      val filePath = file.getAbsolutePath
      filePathTextField.setText(filePath)
   }

   def createFileClicked(): Unit = {
      parentController.createFile((filePathTextField.getText.split('\\').last, filePathTextField.getText()))
      filePathTextField.getScene.getWindow.hide
   }
}
