import java.io._
import java.util.Date
import scala.io.Source

case class User(name: String, id: Int, creation_date: Date, participating_projects: List[Project])


case class Project(owner: User, name: String, description: String, id: Int, list_members: List[Any], list_files: List[Any], list_tasks: List[Any], date_creation: Date) extends Serializable {

  def getOwner() = Project.getOwner(this)
  def getProjectName() = Project.getProjectName(this)
  def getProjectDescription() = Project.getProjectDescription(this)
  def getProjectId() = Project.getProjectId(this)
  def getListMembers() = Project.getListMembers(this)
  def getListFiles() = Project.getListFiles(this)
  def getListTasks() = Project.getListTasks(this)
  def getCreationDate() = Project.getCreationDate(this)
  override def toString() = Project.toString(this)

}



object Project {

  type owner = String
  type name = String
  type description = String
  type id = Int
  type list_members = List[Any]
  type list_files = List[Any]
  type list_tasks = List[Any]
  type date_creation = Date


  def getOwner(p: Project): Unit = {
    return p.owner
  }

  def getProjectName(p: Project): Unit = {
    return p.name
  }

  def getProjectDescription(p: Project): Unit = {
    return p.description
  }

  def getProjectId(p: Project): Unit = {
    return p.id
  }

  def getListMembers(p: Project): Unit = {
    return p.list_members
  }

  def getListFiles(p: Project): Unit = {
    return p.list_files
  }

  def getListTasks(p: Project): Unit = {
    return p.list_tasks
  }

  def getCreationDate(p: Project): Unit = {
    return p.date_creation
  }

  def toString(p: Project): String = {
    return "Name: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + p.owner + "\nCreated On: " + p.date_creation
  }

}