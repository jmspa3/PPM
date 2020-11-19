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

      val task: Task = Task(1, "nome", DateTime.now(), deadline, false, "high")
      task.daysLeft()
      val taskDone: Task = task.setDone()
      println(taskDone.name)

   }

   def sharedFileMenu() = {
      val databasePath = "savedFiles"
      val user = User(1)
      mainLoopFileMenu()

      def mainLoopFileMenu(): Any = {
         println("SharedFile Menu:\n")
         println("1. Check uploaded files")
         println("2. Upload new File")
         println("3. Discard File")
         println("0. Exit program")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => println("Exiting...")
            case "1" => {
               val savedFiles = StorageManager.readObjectFromFile(databasePath)
               savedFiles.asInstanceOf[List[SharedFile]].map(x => println("File: " + x))
               println("What file do you wish to inspect (ID)? ")
               val fileId = readLine().trim
               inspectFileMenu(fileId, databasePath, user)
            }
            case "2" => {
               println("Insert file path: ")
               val newFilePath = readLine().trim
               val newFileName = newFilePath.split("/").last
               val savedFiles = StorageManager.readObjectFromFile(databasePath).asInstanceOf[List[SharedFile]]
               val sh = new SharedFile(newFileName, {
                  if (savedFiles.length > 0) savedFiles.last.getId() + 1 else 0
               }, newFilePath, List())
               StorageManager.writeObjectInFile(sh, databasePath, append = true)
               mainLoopFileMenu()
            }
            case "3" => {
               val savedFiles = StorageManager.readObjectFromFile(databasePath)
               savedFiles.asInstanceOf[List[SharedFile]] map (x => println(x))
               println("Insert ID of file to delete: ")
               val fileId = readLine().trim.toInt
               val newSavedFiles = savedFiles.asInstanceOf[List[SharedFile]].filter(x => x.getId() != fileId)
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


   def inspectFileMenu(fileId: String, databasePath: String, user: User): Unit = {
      val file = StorageManager.readObjectFromFile(databasePath).asInstanceOf[List[SharedFile]].filter(x => x.getId() == fileId.toInt).head
      mainLoopInspectedFileMenu(file)

      def mainLoopInspectedFileMenu(inspectedFile : SharedFile): Any = {
         println("Inspected File Menu:\n")
         println("1. Check File Properties")
         println("2. Read Comments")
         println("3. Add Comment")
         println("0. Go back to the SharedFile Menu")
         val userChoice = readLine.trim
         userChoice match {
            case "0" =>
            {
               println("Exiting...")
               sharedFileMenu()
            }
            case "1" =>
            {
               println("File Id: " + inspectedFile.getId)
               println("File Name: " + inspectedFile.getName)
               println("File Path: " + inspectedFile.getPath)
               println("Number of comments: " + inspectedFile.getComments.length) //TODO Add new comment boolean to comment class? Show if there are any new comments here instead?
               mainLoopInspectedFileMenu(inspectedFile)
            }
            case "2" =>
            {
                  inspectedFile.getComments.map(x => println("Poster: " + x.user + " Data of comment: " + x.date + "\n" + x.content + "\n"))
                  mainLoopInspectedFileMenu(inspectedFile)
            }
            case "3" =>
            {
               println("Write your comment: ")
               val commentContent = readLine.trim
               val newFile = SharedFile(inspectedFile.getName, inspectedFile.getId, inspectedFile.getPath, inspectedFile.getComments ++ List(new Comment(user, DateTime.now, commentContent)))
               val savedFiles = StorageManager.readObjectFromFile(databasePath).asInstanceOf[List[SharedFile]]
               val fileIndex = savedFiles.indexOf(inspectedFile)
               val newSavedFiles = savedFiles.updated(fileIndex, newFile)
               StorageManager.writeObjectListInFile(newSavedFiles ,databasePath, false)
               mainLoopInspectedFileMenu(newFile)
            }
         }
      }
   }






}