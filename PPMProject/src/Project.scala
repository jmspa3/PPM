package PPMProject
import java.io._
import java.util.Date



case class Project(owner: User, name: String, description: String, id: Int, list_members: List[User], list_files: List[SharedFile], list_tasks: List[Task], date_creation: Date) extends Serializable {

  def getOwner: User = Project.getOwner(this)
  def getProjectName: String = Project.getProjectName(this)
  def getProjectDescription: String = Project.getProjectDescription(this)
  def getProjectId: Int = Project.getProjectId(this)
  def getListMembers: List[User] = Project.getListMembers(this)
  def getListFiles: List[SharedFile] = Project.getListFiles(this)
  def getListTasks: List[Task] = Project.getListTasks(this)
  def getCreationDate: Date = Project.getCreationDate(this)
  override def toString: String = Project.toString(this)

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
    p.owner
  }

  def getProjectName(p: Project): String = {
    p.name
  }

  def getProjectDescription(p: Project): String = {
    p.description
  }

  def getProjectId(p: Project): Int = {
    p.id
  }

  def getListMembers(p: Project): List[User] = {
    p.list_members
  }

  def getListFiles(p: Project): List[SharedFile] = {
    p.list_files
  }

  def getListTasks(p: Project): List[Task] = {
    p.list_tasks
  }

  def getCreationDate(p: Project): Date = {
    p.date_creation
  }

  def toString(p: Project): String = {
    "ID: " + p.id + "\nName: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + p.owner.getUsername + "; " + p.owner.getUserId + "\nCreated On: " + p.date_creation + "\n"
  }

}