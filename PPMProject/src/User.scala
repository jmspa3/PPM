package PPMProject

import java.util.Date

import scala.annotation.tailrec

//VER MESSAGE DIGEST PARA ENCRIPTAÇÃO DE PASSWORDS


case class User(name: String = "guest", id: Int = 0, creation_date: Date = new Date(), participating_projects: List[Int] = List()) extends SavedClass {
  def getUsername: String = User.getUsername(this)
  def getId: Int = User.getId(this)
  def getCreationDate: Date = User.getCreationDate(this)
  def getParticipatingProjects: List[Int] = User.getParticipatingProjects(this)
  def setParticipatingProjects(): User = User.setParticipatingProjects(this)
  override def toString: String = User.toString(this)

}

object User {

  type name = String
  type id = Int
  type creation_date = Date
  type participating_projects = List[Int]

  def getUsername(u: User): String = {
    u.name
  }

  def getId(u: User): Int = {
    u.id
  }

  def getCreationDate(u: User): Date ={
    u.creation_date
  }

  def getParticipatingProjects(u: User): List[Int] = {
    u.participating_projects
  }

  def setParticipatingProjects(u: User): User = {
    User(u.name, u.id, u.creation_date, List())
  }

  def addParticipatingProject(u: User, p: Int): User = {
    val listToBeAdded = getParticipatingProjects(u)
    User(u.name, u.id, u.creation_date, listToBeAdded ++ List(p))
  }

  //A SER FEITO QUANDO FOR CARREGADO PARA A MEM O FICHEIRO DA BD POR ISSO ISTO PODE SER ELIMINADO
  def removeParticipatingProjects(u: User, p: Project): User = {
    @tailrec
    def loop(acc: List[Int], userProjects: List[Int], projectID: Int): List[Int] = userProjects match {
      case Nil => acc
      case a => if (userProjects.head != projectID) loop(acc:+userProjects.head, userProjects.tail, projectID) else acc++userProjects.tail
    }
    val updatedProjects = loop(List(), getParticipatingProjects(u), Project.getId(p))
    User(getUsername(u), getId(u), getCreationDate(u), updatedProjects)

  }


  def toString(u: User) : String = {

    "Name : " + u.name +
      "\nID = " + u.id + "\nCreated On : " + u.creation_date +
      "\nParticipating Projects : " + u.participating_projects + "\n"
  }

}


