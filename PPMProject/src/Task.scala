package PPMProject
import java.time._

import org.graalvm.compiler.debug.DebugContext.Description

import scala.concurrent.duration.Deadline

sealed trait Priority
case object LowPriority extends Priority {
   override def toString: String = "Low Priority"
}
case object MediumPriority extends Priority {
   override def toString: String = "Medium Priority"
}
case object HighPriority extends Priority {
   override def toString: String = "High Priority"
}


case class Task(id: Int, ownerId: Int, projectId: Int, creationDate: LocalDate = LocalDate.now(), deadline: LocalDate, name : String, description: String, done : Boolean = false, priority: Priority, memberIds: List[Int] = List(), fileIds: List[Int] = List()) extends SavedClass {

   def getId(): Int = Task.getID(this)
   def daysLeft(): Unit = Task.daysLeft(this)
   def getOwnerId(): Int = Task.getOwnerId(this)
   def getOwner(database: Database): User = Task.getOwner(this, database)
   def getProjectId: Int = Task.getProjectId(this)
   def getProject(database: Database): Project = Task.getProject(this, database)
   def setDone(): Task = Task.setDone(this)
   def isDone(): Boolean = Task.isDone(this)
   def getName(): String = Task.getName(this)
   def getDescription: String = Task.getDescription(this)
   def editDescription(description: String): Task = Task.editDescription(this, description)
   def getPriority(): String = Task.getPriority(this)
   def editPriority(priority: Priority): Task = Task.editPriority(this, priority)
   def getDeadline(): LocalDate = Task.getDeadline(this)
   def editDeadline(deadline: LocalDate): Task = Task.editDeadline(this, deadline)
   def editName(newName : String): Task = Task.editName(this)(newName)
   def getMemberIds(): List[Int] = Task.getMemberIds(this)
   def getMembers(database: Database): List[User] = Task.getMembers(this, database)
   def addMember(newMember: User): Task = Task.addMember(this, newMember)
   def getMembersAsString(database: Database): String = Task.getMembersAsString(this, database)
   def getFileIds(): List[Int] = Task.getFileIds(this)
   def getFiles(database: Database): List[SharedFile] = Task.getFiles(this, database)
   def addFile(newFile: SharedFile): Task = Task.addFile(this, newFile)
   def removeFile(fileToRemove: Int): Task = Task.removeFile(this, fileToRemove)
   def removeMember(memberToRemove: Int): Task = Task.removeMember(this, memberToRemove)

}


object Task{

   // 2: get a date to represent Christmas

   def editName(t:Task)(newName:String): Task  = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, newName, t.description, t.done, t.priority, t.memberIds, t.fileIds)
   }

   def daysLeft(t:Task): Unit = {
      val daysToDeadline = Duration.between(t.creationDate, t.deadline)
      println(daysToDeadline)
   }

   def isDone(t:Task): Boolean = {
      t.done
   }

   def getID(t:Task): Int = {
      t.id
   }
   def getName(t:Task): String = {
      t.name
   }

   def getDescription(t: Task): String = {
      t.description
   }

   def getPriority(t:Task): String = {
      t.priority.toString
   }

   def setDone(t:Task): Task = {
      Task(t.id, t.ownerId, t.projectId ,t.creationDate, t.deadline, t.name, t.description, true, t.priority, t.memberIds, t.fileIds)
   }

   def getOwnerId(t: Task): Int =
   {
      t.ownerId
   }

   def getOwner(t: Task, db: Database): User =
   {
      db.getTableByName("User").records.find(_._2.getId() equals t.ownerId).get._2.asInstanceOf[User]
   }

   def getProjectId(t: Task): Int =
   {
      t.projectId
   }

   def getProject(t: Task, db: Database): Project =
   {
      db.getTableByName("Project").records.find(_._2.getId() equals t.projectId).get._2.asInstanceOf[Project]
   }

   def getFileIds(t: Task): List[Int] =
   {
      t.fileIds
   }

   def getFiles(t: Task, db: Database): List[SharedFile] =
   {
      db.getTableByName("SharedFile").getFromIdList(t.fileIds).asInstanceOf[List[SharedFile]]
   }

   def addFile(t:Task, newFile: SharedFile): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, t.description, t.done, t.priority, t.memberIds, t.fileIds ++ List(newFile.id))
   }

   def removeFile(t: Task, fileToRemove: Int): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, t.description, t.done, t.priority, t.memberIds, t.fileIds.filter(x => x != fileToRemove))
   }

   def getDeadline(t: Task): LocalDate = {
      t.deadline
   }

   def getMemberIds(t: Task): List[Int] =
   {
      t.memberIds
   }

   def getMembers(t: Task, db: Database): List[User] =
   {
      db.getTableByName("User").getFromIdList(t.memberIds).asInstanceOf[List[User]]
   }

   def getMembersAsString(t: Task, db: Database): String = {
      Task.getMembers(t, db).foldRight("")(_.getUsername + ", " + _).trim.dropRight(1)
   }

   def editPriority(t: Task, priority: Priority): Task =
   {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, t.description, t.done, priority, t.memberIds, t.fileIds)
   }

   def addMember(t: Task, newMember: User): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, t.description, t.done, t.priority, t.memberIds ++ List(newMember.id), t.fileIds)
   }

   def removeMember(t: Task, memberToRemove: Int): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, t.description, t.done, t.priority, t.memberIds.filter(x => x != memberToRemove), t.fileIds)
   }

   def editDescription(t: Task, description: String): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, t.deadline, t.name, description, t.done, t.priority, t.memberIds, t.fileIds)
   }

   def editDeadline(t: Task, deadline: LocalDate): Task = {
      Task(t.id, t.ownerId, t.projectId , t.creationDate, deadline, t.name, t.description, t.done, t.priority, t.memberIds, t.fileIds)
   }

}