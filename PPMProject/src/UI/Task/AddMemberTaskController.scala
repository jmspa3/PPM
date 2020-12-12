package UI.Task

import PPMProject.{Database, Project, Task, User}
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.layout.HBox

class AddMemberTaskController {

   @FXML
   private var memberListView: ListView[HBox] = _

   private var task: Task = _
   private var database: Database = _
   private var parent: TaskController = _

   def setData(task: Task, database: Database): Unit = {
      this.task = task
      this.database = database
      memberListView.getItems.clear
      createMemberList(task.getProject(database).getMembers(database))
   }

   def createMemberList(members: List[User]): Unit = {
      members match {
         case Nil => ;
         case h::t => {
            val buttonA = new Button("Add")
            val label = new Label(h.getUsername)
            if (task.getMembers(database).contains(h)) {
               buttonA.setDisable(true)
            }
            label.setMaxWidth(358)
            label.setPrefWidth(label.getMaxWidth)
            buttonA.setOnMouseClicked(event => addMember(h, label, buttonA))
            memberListView.getItems.add(new HBox(label, buttonA))
            createMemberList(t)
         }
      }
   }

   def setParent(parent: TaskController): Unit = {
      this.parent = parent
   }

   def addMember(user: User, label: Label, button: Button): Unit ={
      val newTask = task.addMember(user)
      val taskEntry = database.getTableByName("Task").records.asInstanceOf[Map[Int, Task]].find(x => x._2.id == task.getId).get
      val newDatabase = database.swapTable("Task", database.getTableByName("Task").updateTable(taskEntry, newTask))
      parent.addMember(newTask, newDatabase)
      setData(newTask, newDatabase)
   }

   def doneButtonClicked(): Unit ={
      memberListView.getScene.getWindow.hide
   }

   @FXML def initialize(): Unit = {
      Platform.runLater(() => memberListView.getParent.requestFocus)
   }

}
