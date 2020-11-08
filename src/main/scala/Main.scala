import Task.isDone
import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.Days
import org.joda.time.Days

import scala.annotation.tailrec

object Main {

  def main(args: Array[String]): Unit = {
    val deadline = (new DateTime).withYear(2020)
      .withMonthOfYear(11)
      .withDayOfMonth(6)

    val task: Task = Task(1,"nome",DateTime.now(),deadline,false,"high")
    task.daysLeft()
    val taskDone : Task = task.setDone()
    println(taskDone.name)

  }

}
