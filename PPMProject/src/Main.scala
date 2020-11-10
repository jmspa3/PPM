import Task.isDone
import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.Days
import org.joda.time.Days

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object Main {
   sharedFileMenu()

   def main(args: Array[String]): Unit = {
      val deadline = (new DateTime).withYear(2020)
         .withMonthOfYear(11)
         .withDayOfMonth(6)

      val task: Task = Task(1,"nome",DateTime.now(),deadline,false,"high")
      task.daysLeft()
      val taskDone : Task = task.setDone()
      println(taskDone.name)

   }

   def sharedFileMenu() = {
      val databasePath = "savedFiles"
      mainLoopFileMenu()
      def mainLoopFileMenu():Any =
      {
         println("SharedFile Menu:\n")
         println("1. Check uploaded files")
         println("2. Upload new File")
         println("3. Discard File")
         println("0. Exit program")
         val userChoice = readLine.trim
         userChoice match
         {
            case "0" => println("Exiting...")
            case "1" =>
            {
               val savedFiles = StorageManager.readObjectFromFile(databasePath)
               savedFiles.asInstanceOf[List[SharedFile]] map (x => println(x))
               mainLoopFileMenu()
            }
            case "2" =>
            {
               println("Insert file path: ")
               val newFilePath = readLine().trim
               val newFileName = newFilePath.split("/").last
               val savedFiles = StorageManager.readObjectFromFile(databasePath).asInstanceOf[List[SharedFile]]
               val sh = new SharedFile(newFileName, {if (savedFiles.length > 0) savedFiles.last.getId() + 1 else 0}, newFilePath, List())
               StorageManager.writeObjectInFile(sh, databasePath, append = true)
               mainLoopFileMenu()
            }
            case "3" =>
            {
               val savedFiles = StorageManager.readObjectFromFile(databasePath)
               savedFiles.asInstanceOf[List[SharedFile]] map (x => println(x))
               println("Insert ID of file to delete: ")
               val fileId = readLine().trim.toInt
               val newSavedFiles = savedFiles.asInstanceOf[List[SharedFile]] filter (x => x.getId() != fileId)
               StorageManager.writeObjectListInFile(newSavedFiles, databasePath, append = false)
               mainLoopFileMenu()
            }
            case _ => {
               println("Invalid choice!")
               mainLoopFileMenu()
            }
         }
      }
   }

}