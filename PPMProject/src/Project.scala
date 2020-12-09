package PPMProject
import java.time._



case class Project(id: Int, ownerId: Int, creationDate: LocalDate = LocalDate.now(), name: String, description: String, memberIds: List[Int] = List(), fileIds: List[Int] = List(), taskIds: List[Int] = List()) extends SavedClass {

   def getOwnerId: Int = Project.getOwnerId(this)
   def getOwner(database: Database): User = Project.getOwner(this, database)
   def getProjectName: String = Project.getProjectName(this)
   def getProjectDescription: String = Project.getProjectDescription(this)
   def getId: Int = Project.getId(this)
   def getMemberIds: List[Int] = Project.getMemberIds(this)
   def getMembers(database: Database): List[User] = Project.getMembers(this, database)
   def addMember(newMember: User): Project = Project.addMember(this, newMember)
   def getFileIds: List[Int] = Project.getFileIds(this)
   def getFiles(database: Database): List[SharedFile] = Project.getFiles(this, database)
   def addFile(newFile: SharedFile): Project = Project.addFile(this, newFile)
   def removeFile(fileToRemove: Int): Project = Project.removeFile(this, fileToRemove)
   def getTaskIds: List[Int] = Project.getTaskIds(this)
   def getTasks(database: Database): List[Task] = Project.getTasks(this, database)
   def addTask(newTask: Task): Project = Project.addTask(this, newTask)
   def getCreationDate: LocalDate = Project.getCreationDate(this)
   def customToString(database: Database): String = Project.customToString(this, database)

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

   def addTask(p: Project, newTask: Task): Project =
   {
      Project(p.id, p.ownerId, p.creationDate, p.name, p.description, p.memberIds, p.fileIds, p.taskIds ++ List(newTask.getId))
   }

   def customToString(p: Project, db: Database): String = {
      "ID: " + p.id + "\nName: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + Project.getOwner(p, db).getUsername + "; " + Project.getOwner(p, db).getId + "\nCreated On: " + p.creationDate + "\n"
   }

}