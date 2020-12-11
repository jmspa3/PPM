package PPMProject

import java.time.LocalDateTime


case class Comment(id: Int, fileId: Int, ownerId: Int, creationDate: LocalDateTime = LocalDateTime.now(), content: String) extends SavedClass {

   def getId:Int = Comment.getId(this)
   def getOwnerId: Int = Comment.getOwnerId(this)
   def getFileId: Int = Comment.getFileId(this)
   def getOwner(database: Database): User = Comment.getOwner(this, database)
   def getCreationDateTime: LocalDateTime = Comment.getCreationDateTime(this)
   def getContent: String = Comment.getContent(this)
   def customToString(database: Database): String = Comment.customToString(this, database)

}

object Comment {

   def getId(c: Comment): Int =
   {
      c.id
   }

   def getOwnerId(c: Comment): Int = {
      c.id
   }

   def getFileId(c: Comment): Int = {
      c.fileId
   }

   def getOwner(c: Comment, db: Database): User =
   {
      db.getTableByName("User").records.find(_._2.getId() equals c.ownerId).get._2.asInstanceOf[User]
   }

   def getCreationDateTime(c: Comment): LocalDateTime =
   {
      c.creationDate
   }

   def getContent(c: Comment): String =
   {
      c.content
   }

   def customToString(c: Comment, db: Database): String =
   {
      "Poster: " + c.getOwner(db).getUsername + " Date of comment: " + c.getCreationDateTime + "\n" + c.getContent + "\n"
   }

}
