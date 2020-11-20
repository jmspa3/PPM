package PPMProject

import java.io.Serializable
import java.util.Date

//VER MESSAGE DIGEST PARA ENCRIPTAÇÃO DE PASSWORDS


case class User(name: String = "guest", id: Int = 0, creation_date: Date = new Date(), participating_projects: List[Project] = List()) extends Serializable {
  def getUsername: String = User.getUsername(this)
  def getUserId: Int = User.getUserId(this)
  def getCreationDate: Date = User.getCreationDate(this)
  def getParticipatingProjects: List[Project] = User.getParticipatingProjects(this)
  def setParticipatingProjects(): User = User.setParticipatingProjects(this)
  override def toString: String = User.toString(this)

}

object User {

  type name = String
  type id = Int
  type creation_date = Date
  type participating_projects = List[Project]

  def getUsername(u: User): String = {
    u.name
  }

  def getUserId(u: User): Int = {
    u.id
  }

  def getCreationDate(u: User): Date ={
    u.creation_date
  }

  def getParticipatingProjects(u: User): List[Project] = {
    u.participating_projects
  }

  def setParticipatingProjects(u: User): User = {
    User(u.name, u.id, u.creation_date, List())
  }


  def toString(u: User) : String = {

    "Name : " + u.name +
      "\nID = " + u.id + "\nCreated On : " + u.creation_date +
      "\nParticipating Projects : " + u.participating_projects + "\n"
  }

}


