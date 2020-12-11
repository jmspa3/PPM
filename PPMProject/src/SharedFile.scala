package PPMProject

import java.time._

case class SharedFile(id: Int, ownerId: Int, projectId: Int, creationDate: LocalDate = LocalDate.now(), fileName: String, path: String, commentIds: List[Int] = List()) extends SavedClass {

   def getName() = SharedFile.getName(this)
   def getId() = SharedFile.getId(this)
   def getPath() = SharedFile.getPath(this)
   def getOwnerId: Int = SharedFile.getOwnerId(this)
   def getOwner(database: Database): User = SharedFile.getOwner(this, database)
   def getProjectId: Int = SharedFile.getProjectId(this)
   def getProject(database: Database): Project = SharedFile.getProject(this, database)
   def getCreationDate(): LocalDate = SharedFile.getCreationDate(this)
   def getCommentIds: List[Int] = SharedFile.getCommentIds(this)
   def getComments(database: Database): List[Comment] = SharedFile.getComments(this, database)
   def addComment(newComment: Comment): SharedFile = SharedFile.addComment(this, newComment)
   override def toString() = SharedFile.toString(this)

}


object SharedFile {

   def getName(sh: SharedFile) =
   {
      sh.fileName
   }

   def getId(sh: SharedFile) =
   {
      sh.id
   }

   def getPath(sh: SharedFile) = {
      sh.path
   }

   def getCreationDate(sh: SharedFile) = {
      sh.creationDate
   }

   def getOwnerId(sh: SharedFile): Int =
   {
      sh.ownerId
   }

   def getOwner(sh: SharedFile, db: Database): User =
   {
      db.getTableByName("User").records.find(_._2.getId() equals sh.ownerId).get._2.asInstanceOf[User]
   }

   def getProjectId(sh: SharedFile): Int =
   {
      sh.projectId
   }

   def getProject(sh: SharedFile, db: Database): Project =
   {
      db.getTableByName("Project").records.find(_._2.getId() equals sh.projectId).get._2.asInstanceOf[Project]
   }

   def getCommentIds(sh: SharedFile): List[Int] =
   {
      sh.commentIds
   }

   def getComments(sh: SharedFile, db: Database) = {
      db.getTableByName("Comment").getFromIdList(sh.commentIds).asInstanceOf[List[Comment]]
   }

   def addComment(sh: SharedFile, newComment: Comment): SharedFile =
   {
      SharedFile(sh.id, sh.ownerId, sh.projectId, sh.creationDate, sh.fileName, sh.path, sh.commentIds ++ List(newComment.getId()))
   }

   def toString(sh: SharedFile): String =
   {
      sh.id + ": " + sh.fileName
   }

}