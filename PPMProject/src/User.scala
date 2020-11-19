package PPM

import java.io.Serializable
import java.util.Date




case class User(name: String, id: Int, creation_date: Date, participating_projects: List[Project]) extends Serializable {
  def getUsername = User.getUsername(this)
  def getUserId = User.getUserId(this)
  def getCreationDate = User.getCreationDate(this)
  def getParticipatingProjects = User.getParticipatingProjects(this)
  def setParticipatingProjects = User.setParticipatingProjects(this)
  override def toString = User.toString(this)

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
      "\nParticipating Projects : " + u.participating_projects + "\n";
  }

}


