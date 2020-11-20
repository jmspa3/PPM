package PPMProject
import java.text.SimpleDateFormat
import java.util.Calendar
import com.github.nscala_time.time.Imports._
import org.joda.time.Days

case class User(id : Int)

case class Task(id: Int, name : String, creationDate: DateTime, done : Boolean ,priority: String) extends Serializable {
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
  type creationDate = DateTime
  type deadline = DateTime
  type done = Boolean
  type priority = String
  type Members = List[User]

  // 2: get a date to represent Christmas

  def editName(t:Task, newName:String): Task  = {
    Task(t.id,newName,t.creationDate,t.done,t.priority)
  }
  def daysLeft(t:Task): Unit = {
    val deadline = (new DateTime).withYear(2020)
      .withMonthOfYear(11)
      .withDayOfMonth(6)
    val daysToDeadline = Days.daysBetween(t.creationDate, deadline).getDays
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