package PPMProject

import java.time._

//VER MESSAGE DIGEST PARA ENCRIPTAÇÃO DE PASSWORDS

case class User(id: Int = 0, name: String = "guest", creationDate: LocalDate = LocalDate.now()) extends SavedClass {

   def getUsername: String = User.getUsername(this)
   def getId: Int = User.getId(this)
   def getCreationDate: LocalDate = User.getCreationDate(this)
   def getParticipatingProjects(database: Database): List[Project] = User.getParticipatingProjects(this, database)
   def customToString(database: Database): String = User.customToString(this, database)

}

object User {

   def getUsername(u: User): String = {
      u.name
   }

   def getId(u: User): Int = {
      u.id
   }

   def getCreationDate(u: User): LocalDate ={
      u.creationDate
   }

   def getParticipatingProjects(u: User, db: Database): List[Project] = {
      val projects = db.getTableByName("Project").records.values.toList.asInstanceOf[List[Project]]
      projects.filter(x => x.memberIds.contains(u.id) || (x.ownerId equals u.id))
   }


   def customToString(u: User, db: Database) : String = {

      "Name : " + u.name +
         "\nID = " + u.id + "\nCreated On : " + u.creationDate +
         "\nParticipating Projects : " + User.getParticipatingProjects(u, db) + "\n"
   }

}


