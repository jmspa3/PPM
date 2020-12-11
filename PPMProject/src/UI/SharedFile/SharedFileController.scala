package UI.SharedFile


import java.time.{LocalDate, LocalTime}

import PPMProject.SharedFile
import javafx.fxml.FXML
import javafx.application.Platform
import javafx.animation.PauseTransition
import javafx.scene.Parent
import javafx.util.Duration
import javafx.scene.control.{Label, ListView, ScrollPane, TextArea}
import javafx.scene.layout.VBox

class SharedFileController {

   @FXML
   private var fileNameLabel: Label = _
   @FXML
   private var filePathLabel: Label = _
   @FXML
   private var commentTextArea: TextArea = _
   @FXML
   private var commentListView: ListView[String] = _

   private var parentRoot: Parent = _
   private var file: SharedFile = _

   def initialize(): Unit = {
   }


   def setFile(file: SharedFile): Unit = {
      this.file = file
      fileNameLabel.setText(file.getName())
      filePathLabel.setText(file.getPath())
   }

   def newComment(): Unit = {
      val comment = "User: " + "" + LocalTime.now.getHour + ":" + LocalTime.now.getMinute + "\n" + commentTextArea.getText()
      commentTextArea.setText("")
      commentListView.getItems.add(comment)
      commentListView.scrollTo(comment)
      //commentListScroll.setVvalue(1)
   }

   def backButtonClicked(): Unit = {
      fileNameLabel.getScene.setRoot(parentRoot)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }


}
