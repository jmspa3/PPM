import DBPackage.{Database, Table}
import Task.isDone
import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.Days
import org.joda.time.Days

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.util.Try

object Main {
   sharedFileMenu(User(1))

   def main(args: Array[String]): Unit = {
      val deadline = (new DateTime).withYear(2020)
         .withMonthOfYear(11)
         .withDayOfMonth(6)

      val task: Task = Task(1, "nome", DateTime.now(), deadline, false, "high")
      task.daysLeft()
      val taskDone: Task = task.setDone()
      println(taskDone.name)

   }

   def sharedFileMenu(user: User) = {
      val databasePath = "savedFiles"
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
               val savedDatabase = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
               val savedFiles = savedDatabase.getTableByName("SharedFile").records.values.asInstanceOf[Iterable[SharedFile]]
               if (savedFiles.size != 0) {
                  savedFiles.map(x => println("File: " + x.getId + " " + x.getName))
                  println("What file do you wish to inspect (ID)? Write Q if you don't wish to inspect any of them")
                  val fileId = readLine().trim
                  fileId match {
                     case "q" | "Q" => mainLoopFileMenu()
                     case _ => {
                        if (Try(fileId.toInt).isSuccess) inspectFileMenu(fileId, databasePath, user)
                        else {
                           println("Not a valid file Id!")
                           mainLoopFileMenu()
                        }
                     }
                  }
               }
               else {
                  println("There are no shared files at the moment!")
                  mainLoopFileMenu()
               }
            }
            case "2" => {
               println("Insert file path: ")
               val newFilePath = readLine().trim
               val newFileName = newFilePath.split("/").last
               val savedFiles = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("SharedFile")
               val sh = new SharedFile(newFileName, {
                  if (savedFiles.records.values.size > 0) savedFiles.records.values.last.asInstanceOf[SharedFile].getId() + 1 else 0
               }, newFilePath, List())
               StorageManager.addObjectToFile(sh, databasePath)
               mainLoopFileMenu()
            }
            case "3" => {
               val savedDatabase = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
               val savedFiles = savedDatabase.getTableByName("SharedFile").records.values.asInstanceOf[Iterable[SharedFile]]
               savedFiles.map(x => println("File: " + x.getId + " " + x.getName))
               println("Insert ID of file to delete: ")
               val fileId = readLine().trim.toInt
               val newFileList = savedFiles.filter(x => x.getId() != fileId)
               StorageManager.writeObjectListInFile(newFileList.toList, databasePath)
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
      val file = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("SharedFile").records.asInstanceOf[Map[Int, SharedFile]].values.find(x => x.id == fileId.toInt).get
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
               sharedFileMenu(user)
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

               val savedFiles = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("SharedFile").records.values.asInstanceOf[Iterable[SharedFile]]
               val fileIndex = savedFiles.toList.indexOf(inspectedFile)
               val newSavedFiles = savedFiles.toList.updated(fileIndex, newFile)

               StorageManager.writeObjectListInFile(newSavedFiles, databasePath)
               mainLoopInspectedFileMenu(newFile)
            }
         }
      }
   }






}