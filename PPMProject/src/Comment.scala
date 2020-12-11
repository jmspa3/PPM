package PPMProject

import java.time.LocalDate


case class Comment(id: Int, ownerId: Int, creationDate: LocalDate = LocalDate.now(), content: String) extends SavedClass {

   def getId:Int = Comment.getId(this)
   def getOwnerId: Int = Comment.getOwnerId(this)
   def getOwner(database: Database): User = Comment.getOwner(this, database)
   def getCreationDate: LocalDate = Comment.getCreationDate(this)
   def getContent: String = Comment.getContent(this)
   def customToString(database: Database): String = Comment.customToString(this, database)

}

object Comment {

   def getId(c: Comment): Int =
   {
      c.id
   }

   def getOwnerId(c: Comment): Int =
   {
      c.id
   }

   def getOwner(c: Comment, db: Database): User =
   {
      db.getTableByName("User").records.find(_._2.getId() equals c.ownerId).get._2.asInstanceOf[User]
   }

   def getCreationDate(c: Comment): LocalDate =
   {
      c.creationDate
   }

   def getContent(c: Comment): String =
   {
      c.content
   }

   def customToString(c: Comment, db: Database): String =
   {
      "Poster: " + c.getOwner(db).getUsername + " Date of comment: " + c.getCreationDate + "\n" + c.getContent + "\n"
   }

}
