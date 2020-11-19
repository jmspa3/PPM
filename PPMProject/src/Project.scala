package PPM
import java.io._
import java.util.Date



case class Project(owner: User, name: String, description: String, id: Int, list_members: List[User], list_files: List[SharedFile], list_tasks: List[Task], date_creation: Date) extends Serializable {

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
  type list_members = List[User]
  type list_files = List[SharedFile]
  type list_tasks = List[Task]
  type date_creation = Date


  def getOwner(p: Project): User = {
    return p.owner
  }

  def getProjectName(p: Project): String = {
    return p.name
  }

  def getProjectDescription(p: Project): String = {
    return p.description
  }

  def getProjectId(p: Project): Int = {
    return p.id
  }

  def getListMembers(p: Project): List[User] = {
    return p.list_members
  }

  def getListFiles(p: Project): List[SharedFile] = {
    return p.list_files
  }

  def getListTasks(p: Project): List[Task] = {
    return p.list_tasks
  }

  def getCreationDate(p: Project): Date = {
    return p.date_creation
  }

  def toString(p: Project): String = {
    return "Name: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + p.owner + "\nCreated On: " + p.date_creation
  }

}