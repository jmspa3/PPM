package UI.SharedFile


import java.time.{LocalDate, LocalTime}

import PPMProject.{Comment, Database, SharedFile, User}
import UI.Project.ProjectController
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
   private var creatorLabel: Label = _
   @FXML
   private var createdDateLabel: Label = _
   @FXML
   private var commentTextArea: TextArea = _
   @FXML
   private var commentListView: ListView[String] = _

   private var parentRoot: Parent = _
   private var parent: ProjectController = _
   private var user: User = _
   private var file: SharedFile = _
   private var database: Database = _

   def initialize(): Unit = {
   }


   def setData(file: SharedFile, user: User, database: Database): Unit = {
      this.file = file
      this.user = user
      this.database = database
      setInitialInfo()
      setCommentList()
   }

   def setInitialInfo() = {
      fileNameLabel.setText(file.getName())
      filePathLabel.setText(file.getPath())
      creatorLabel.setText("Uploaded by: " + file.getOwner(database).getUsername)
      createdDateLabel.setText("Uploaded on: " + file.getCreationDate().toString)
   }

   def createCommentItem(comment: Comment): Unit = {
      val timeString = comment.getCreationDateTime.getHour + ":" + comment.getCreationDateTime.getMinute + " on " + comment.getCreationDateTime.toLocalDate
      val commentItem = comment.getOwner(database).getUsername + ":        " + timeString + "\n" + comment.getContent
      commentListView.getItems.add(commentItem)
      commentListView.scrollTo(commentItem)
   }

   def setCommentList(): Unit = {
      commentListView.getItems.clear
      file.getComments(database).map(createCommentItem(_))
   }

   def newComment(): Unit = {
      val commentContent = commentTextArea.getText
      val comment = Comment({
         if (database.getTableByName("Comment").records.size > 0) database.getTableByName("Comment").records.values.toList.asInstanceOf[List[Comment]].last.getId() + 1 else 0
      }, file.getId, user.getId, content = commentContent)
      commentTextArea.setText("")
      val fileEntry = database.getTableByName("SharedFile").records.asInstanceOf[Map[Int, SharedFile]].find(x => x._2.id == file.getId).get
      val newFile = file.addComment(comment)
      val savedFiles = database.getTableByName("SharedFile")
      val tempDatabase = database.swapTable("SharedFile", savedFiles.updateTable(fileEntry, newFile))
      val newDatabase = tempDatabase.insertInTable(comment, "Comment")
      setData(newFile, user, newDatabase)
   }

   def backButtonClicked(): Unit = {
      parent.setData(file.getProject(database), user, database)
      fileNameLabel.getScene.setRoot(parentRoot)
   }

   def setParentRoot(parentRoot: Parent): Unit = {
      this.parentRoot = parentRoot
   }

   def setParent(parent: ProjectController): Unit = {
      this.parent = parent
   }


}
