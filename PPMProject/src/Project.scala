package PPMProject
import java.time._

import org.graalvm.compiler.debug.DebugContext.Description

case class Project(id: Int, ownerId: Int, creationDate: LocalDate = LocalDate.now(), name: String, description: String, memberIds: List[Int] = List(), fileIds: List[Int] = List(), taskIds: List[Int] = List()) extends SavedClass {

   def getOwnerId: Int = Project.getOwnerId(this)
   def getOwner(database: Database): User = Project.getOwner(this, database)
   def getProjectName: String = Project.getProjectName(this)
   def getProjectDescription: String = Project.getProjectDescription(this)
   def getId: Int = Project.getId(this)
   def getMemberIds: List[Int] = Project.getMemberIds(this)
   def getMembers(database: Database): List[User] = Project.getMembers(this, database)
   def getMembersAsString(database: Database): String = Project.getMembersAsString(this, database)
   def addMember(newMember: User): Project = Project.addMember(this, newMember)
   def getFileIds: List[Int] = Project.getFileIds(this)
   def getFiles(database: Database): List[SharedFile] = Project.getFiles(this, database)
   def getFilesAsString(database: Database): String = Project.getFilesAsString(this, database)
   def addFile(newFile: SharedFile): Project = Project.addFile(this, newFile)
   def removeFile(fileToRemove: Int): Project = Project.removeFile(this, fileToRemove)
   def getTaskIds: List[Int] = Project.getTaskIds(this)
   def getTasks(database: Database): List[Task] = Project.getTasks(this, database)
   def getTasksAsString(database: Database): String = Project.getTasksAsString(this, database)
   def getTasksOfPriority(priority: Priority, database: Database): List[Task] = Project.getTasksOfPriority(this, database)(priority)
   def getHighPriorityTasks(database: Database): List[Task] = Project.getHighPriorityTasks(this, database)
   def getMediumPriorityTasks(database: Database): List[Task] = Project.getMediumPriorityTasks(this, database)
   def getLowPriorityTasks(database: Database): List[Task] = Project.getLowPriorityTasks(this, database)
   def addTask(newTask: Task): Project = Project.addTask(this, newTask)
   def removeTask(taskToRemove: Int): Project = Project.removeTask(this, taskToRemove)
   def getCreationDate: LocalDate = Project.getCreationDate(this)
   def customToString(database: Database): String = Project.customToString(this, database)
   def editDescription(description: String): Project = Project.editDescription(this, description)
   def editName(name: String): Project = Project.editName(this, description)
}



object Project {


   def getProjectName(p: Project): String = {
      p.name
   }

   def getProjectDescription(p: Project): String = {
      p.description
   }

   def getId(p: Project): Int = {
      p.id
   }

   def getCreationDate(p: Project): LocalDate = {
      p.creationDate
   }

   def getOwnerId(p: Project): Int =
   {
      p.ownerId
   }

   def getOwner(p: Project, db: Database): User = {
      db.getTableByName("User").records.find(_._2.getId() equals p.ownerId).get._2.asInstanceOf[User]
   }

   def getMemberIds(p: Project): List[Int] =
   {
      p.memberIds
   }

   def getMembers(p: Project, db: Database): List[User] = {
      db.getTableByName("User").getFromIdList(p.ownerId :: p.memberIds).asInstanceOf[List[User]]
   }

   def getMembersAsString(p: Project, db: Database): String = {
      Project.getMembers(p, db).foldRight("")(_.getUsername + ", " + _).trim.dropRight(1)
   }

   def addMember(p: Project, newMember: User): Project =
   {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds ++ List(newMember.getId), p.fileIds, p.taskIds)
   }

   def getFileIds(p: Project): List[Int] =
   {
      p.fileIds
   }

   def getFiles(p: Project, db: Database): List[SharedFile] = {
      db.getTableByName("SharedFile").getFromIdList(p.fileIds).asInstanceOf[List[SharedFile]]
   }

   def getFilesAsString(p: Project, db: Database): String = {
      Project.getFiles(p, db).foldRight("")(_.getName() + ", " + _).trim.dropRight(1)
   }

   def addFile(p: Project, newFile: SharedFile): Project =
   {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds, p.fileIds ++ List(newFile.getId), p.taskIds)
   }

   def removeFile(p: Project, fileToRemove: Int): Project = {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds, p.fileIds.filter(x => x != fileToRemove), p.taskIds)
   }

   def getTaskIds(p: Project): List[Int] =
   {
      p.taskIds
   }

   def getTasks(p: Project, db: Database): List[Task] = {
      db.getTableByName("Task").getFromIdList(p.taskIds).asInstanceOf[List[Task]]
   }

   def getTasksAsString(p: Project, db: Database): String = {
      Project.getTasks(p, db).foldRight("")(_.getName() + ", " + _).trim.dropRight(1)
   }

   def addTask(p: Project, newTask: Task): Project =
   {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds, p.fileIds, p.taskIds ++ List(newTask.getId))
   }

   def getTasksOfPriority(p: Project, db: Database)(priority: Priority): List[Task] = {
      Project.getTasks(p, db).foldRight(List[Task]())((x, lst) => if (x.priority.equals(priority)) x :: lst else lst)
   }

   def getHighPriorityTasks(p: Project, db: Database): List[Task] = {
      Project.getTasksOfPriority(p, db)(HighPriority)
   }

   def getMediumPriorityTasks(p: Project, db: Database): List[Task] = {
      Project.getTasksOfPriority(p, db)(MediumPriority)
   }

   def getLowPriorityTasks(p: Project, db: Database): List[Task] = {
      Project.getTasksOfPriority(p, db)(LowPriority)
   }

   def removeTask(p: Project, taskToRemove: Int): Project = {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds, p.fileIds, p.taskIds.filter(x => x != taskToRemove))
   }

   def customToString(p: Project, db: Database): String = {
      "ID: " + p.id + "\nName: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + Project.getOwner(p, db).getUsername + "; " + Project.getOwner(p, db).getId + "\nCreated On: " + p.creationDate + "\nMembers: " + Project.getMembersAsString(p, db)
   }

   def editDescription(p: Project, description: String): Project = {
      Project(p.id, p.ownerId, p.creationDate, p.name, description, p.memberIds, p.fileIds, p.taskIds)
   }

   def editName(p: Project, name: String): Project = {
      Project(p.id, p.ownerId, p.creationDate, name, p.description, p.memberIds, p.fileIds, p.taskIds)
   }
}