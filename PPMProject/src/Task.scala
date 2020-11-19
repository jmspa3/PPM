package PPM
import com.github.nscala_time.time.Imports._
import org.joda.time.Days


case class Task(id: Int, name : String, creationDate: DateTime, deadline : DateTime, done : Boolean ,priority: String) {
  def daysLeft(): Unit = Task.daysLeft(this)
  def setDone(): Task = Task.setDone(this)
  def isDone(): Boolean = Task.isDone(this)
  def getPriority(): String = Task.getPriority(this)
  def editName(newName : String): Task = Task.editName(this,newName)

}

object Task{

  type ID = Int
  type name = String
  type description = String
  type creationDate = DateTime
  type deadline = DateTime
  type done = Boolean
  type priority = String
  type Members = List[User]

  // 2: get a date to represent Christmas

  def editName(t:Task, newName:String): Task  = {
    Task(t.id,newName,t.creationDate,t.deadline,t.done,t.priority)
  }
  def daysLeft(t:Task): Unit = {
    val daysToDeadline = Days.daysBetween(t.creationDate, t.deadline).getDays
    println(daysToDeadline)
  }

  def isDone(t:Task): Boolean = {
    t.done
  }

  def getPriority(t:Task): String = {
    t.priority
  }

  def setDone(t:Task): Task = {
    Task(t.id,t.name,t.creationDate,t.deadline,true,t.priority)
  }





}