import java.util.Date
import java.io.Serializable


case class Project(owner: User, name: String, description: String, id: Int, list_members: List[User], list_files: List[Any], list_tasks: List[Any], date_creation: Date)

case class User(name: String, id: Int, creation_date: Date, participating_projects: List[Project]) extends Serializable {
  def getUsername() = User.getUsername(this)
  def getUserId() = User.getUserId(this)
  def getCreationDate() = User.getCreationDate(this)
  def getParticipatingProjects() = User.getParticipatingProjects(this)
  def setParticipatingProjects() = User.setParticipatingProjects(this, p)
  override def toString() = User.toString(this)

}

object User {

  type name = String
  type id = Int
  type creation_date = Date
  type participating_projects = List[Project]

  def getUsername(u: User): Unit = {
    return u.name
  }

  def getUserId(u: User): Unit = {
    return u.id
  }

  def getCreationDate(u: User): Unit ={
    return u.creation_date
  }

  def getParticipatingProjects(u: User): Unit = {
    return u.participating_projects
  }

  def setParticipatingProjects(u: User, p: List[Project]): User = {
    User(u.name, u.id, u.creation_date, p)
  }


  def toString(u: User) : String = {

    return "Name : " + u.name +
      "\nID = " + u.id + "\nCreated On : " + u.creation_date +
      "\nParticipating Projects : " + u.participating_projects + "\n";
  }

}


