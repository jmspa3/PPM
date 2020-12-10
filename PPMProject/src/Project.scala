package PPMProject
import java.util.Date



case class Project(owner: User, name: String, description: String, id: Int, list_members: List[Int], list_files: List[Int], list_tasks: List[Int], date_creation: Date) extends SavedClass {

  def getOwner: User = Project.getOwner(this)
  def getProjectName: String = Project.getProjectName(this)
  def getProjectDescription: String = Project.getProjectDescription(this)
  def getId: Int = Project.getId(this)
  def getListMembers: List[Int] = Project.getListMembers(this)
  def getListFiles: List[Int] = Project.getListFiles(this)
  def getListTasks: List[Int] = Project.getListTasks(this)
  def getCreationDate: Date = Project.getCreationDate(this)
  override def toString: String = Project.toString(this)

}



object Project {

  type owner = String
  type name = String
  type description = String
  type id = Int
  type list_members = List[Int]
  type list_files = List[Int]
  type list_tasks = List[Int]
  type date_creation = Date

  //FALTA
  //lista sharedfiles/tasks associados
  //adicionar/remover sharedfiles/tasks

  def getOwner(p: Project): User = {
    p.owner
  }

  def getProjectName(p: Project): String = {
    p.name
  }

  def getProjectDescription(p: Project): String = {
    p.description
  }

  def getId(p: Project): Int = {
    p.id
  }

  def getListMembers(p: Project): List[Int] = {
    p.list_members
  }

  def getListFiles(p: Project): List[Int] = {
    p.list_files
  }

  def getListTasks(p: Project): List[Int] = {
    p.list_tasks
  }

  def getCreationDate(p: Project): Date = {
    p.date_creation
  }

  def addMember(p: Project, u: User) = {
    //before calling save project to be deleted to val, then call using such val, using returned, save to DB
    Project(p.getOwner, p.getProjectName, p.getProjectDescription, p.getId, p.getListMembers:+u.getId, p.getListFiles, p.getListTasks, p.getCreationDate)
  }

  def toString(p: Project): String = {
    "ID: " + p.id + "\nName: " + p.name + "\nDescription: " + p.description + "\nOwned By: " + p.owner.getUsername + "; " + p.owner.getId + "\nCreated On: " + p.date_creation + "\n"
  }

}