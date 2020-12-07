package PPMProject
import java.time._




case class Task(id: Int, name : String, creationDate: LocalDate, done : Boolean ,priority: String) extends Serializable {
  def daysLeft(): Unit = Task.daysLeft(this)
  def setDone(): Task = Task.setDone(this)
  def isDone(): Boolean = Task.isDone(this)
  def getId(): Int = Task.getID(this)
  def getName(): String = Task.getName(this)
  def getPriority(): String = Task.getPriority(this)
  def editName(newName : String): Task = Task.editName(this,newName)

}


object Task{

  type ID = Int
  type name = String
  type description = String
  type creationDate = LocalDate
  type deadline = LocalDate
  type done = Boolean
  type priority = String
  type Members = List[User]

  // 2: get a date to represent Christmas

  def editName(t:Task)(newName:String): Task  = {
    Task(t.id,newName,t.creationDate,t.done,t.priority)
  }
  def daysLeft(t:Task): Unit = {
    val deadline = LocalDate.of(2001, 1, 31)
    val daysToDeadline = Duration.between(t.creationDate,deadline)
    println(daysToDeadline)
  }

  def isDone(t:Task): Boolean = {
    t.done
  }

  def getID(t:Task): Int = {
    t.id
  }
  def getName(t:Task): String = {
    t.name
  }
  def getPriority(t:Task): String = {
    t.priority
  }

  def setDone(t:Task): Task = {
    Task(t.id,t.name,t.creationDate,true,t.priority)
  }





}