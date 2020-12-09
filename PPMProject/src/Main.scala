package PPMProject
import java.util.Date
import java.time._
import scala.io.StdIn.readLine
import scala.util.Try
import java.time._

object Main {

   val databasePath = "savedFiles"

   def main(args: Array[String]): Unit = {
      println("Welcome to PPMProject. Insert your Username: ")
      val username = readLine().trim
      //procurar na BD o user com o nome fornecido
      val database = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
      val userList = database.getTableByName("User").records.values.asInstanceOf[Iterable[User]].toList
      try {
         val userlist = userList filter (x => x.getUsername == username)
         val newUser = userlist.head
         mainMenu(newUser, database)
      } catch {
         case e: NoSuchElementException => {
            val newInfo = createUser(userList, database)
            mainMenu(newInfo._1, newInfo._2)
         }
      }

   }

   def createUser(existingUsers: List[User], database: Database): (User, Database) = {
      println("We couldn't find you on the User Database. Please select one of the options: ")
      println("1. Create new User ")
      println("2. Continue as guest: ")


      val userChoice = readLine.trim
      userChoice match {
         case "1" => {
            println("Insert your username")
            val username = readLine().trim
            addUser(username)(database)
         }
         case _ =>{
            println("Continuing as Guest") //Have a guest User already created?
            (new User(), database)
         }
      }

   }


   //Exemplo de Currying, addUser com 2 args e addUser2 que vem com o 2º arg predefinido. Útil para adicionar o utilizador no login (removido devido á nova implementação de database como objeto)
   def addUser(newUser: String)(database: Database): (User, Database) = {
      val existingUsers = database.getTableByName("User")
      val us = new User(newUser, {
         if (existingUsers.records.values.size > 0) existingUsers.records.values.last.asInstanceOf[User].getId + 1 else 0
      }, new Date(), List())
      val newDatabase = database.insertInTable(us, "User") //TODO change depending on professor's answer
      (us, newDatabase)
   }


   def mainMenu(user: User, database: Database) {
      println("Main Menu:")
      println("1. Shared Files")
      println("2. Users")
      println("3. Projects")
      println("4. Tasks")
      println("5. Save Progress")
      println("0. Exit program")
      val userChoice = readLine.trim
      userChoice match {
         case "0" => println("Exiting...")
         case "1" => {
            sharedFileMenu(user, database)
            mainMenu(user, database)
         }

         case "2" => {
            userMenu(user, database)
            mainMenu(user, database)
         }
         case "3" => {
            projectMenu(user, database)
            mainMenu(user, database)
         }

         case "4" => {
            taskMenu(user, database)
            mainMenu(user, database)
         }
         case "5" => {
            StorageManager.saveDatabase(database, databasePath)
            mainMenu(user, database)
         }
         case _ => {
            println("Invalid choice!")
            mainMenu(user, database)
         }
      }
   }


   def userMenu(user: User, database: Database) = {
      val usersPath = "savedUsers"
      mainLoopUserMenu(user, database)

      def mainLoopUserMenu(user: User, database: Database): Any = {
         println("User Menu:")
         println("1. Check Users")
         println("2. Create New User")
         println("3. Delete User")
         println("0. Go Back")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => println("Exiting...")
            case "1" => {
               val existingUsers = database.getTableByName("User")
               existingUsers.records.values map (x => println(x))
               mainLoopUserMenu(user, database)
            }
            case "2" => {
               println("Insert Username: ")
               val newUser = readLine().trim
               val newInfo = addUser(newUser)(database)
               mainLoopUserMenu(newInfo._1, newInfo._2)
            }

            case "3" => {
               val savedUsers = database.getTableByName("User")
               savedUsers.records.values.asInstanceOf[Iterable[User]] map (x => println(x))
               println("Insert ID of User to delete: ")
               val userId = readLine().trim.toInt
               val usersToMaintain = savedUsers.filterTable(userId)
               val newDatabase = database.swapTable( "User", usersToMaintain)
               mainLoopUserMenu(user, newDatabase)
            }
            case _ => {
               println("Invalid choice!")
               mainLoopUserMenu(user, database)
            }
         }
      }
   }
   def taskMenu(user: User, database: Database) = {
      //val databasePath = "savedTasks"
      mainLoopTaskMenu(user, database)

      def mainLoopTaskMenu(user: User, database: Database): Any = {
         println("Task Menu:\n")
         println("1. Check uploaded tasks")
         println("2. Upload new Task")
         println("3. Discard Task")
         println("0. Go Back")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => println("Exiting...")
            case "1" => {
               val savedFiles = database.getTableByName("Task").records.values.asInstanceOf[Iterable[Task]]
               if (savedFiles.size != 0) {
                  savedFiles.map(x => println("Task: " + x.getId + " " + x.getName))
               }
               else {
                  println("There are no tasks at the moment!")
               }
               mainLoopTaskMenu(user, database)
            }
            case "2" => {
               println("Insert task name: ")
               val newTaskName = readLine()
               val savedTasks = database.getTableByName("Task")
               val sh = new Task({
                  if (savedTasks.records.values.size > 0) savedTasks.records.values.last.asInstanceOf[Task].getId() + 1 else 0
               },newTaskName, LocalDate.now(),false,"High")
               val newDatabase = database.insertInTable(sh, "Task")
               mainLoopTaskMenu(user, newDatabase)
            }
            case "3" => {
               val className = "Task"
               val savedTasks = database.getTableByName(className)

               savedTasks.records.values.asInstanceOf[Iterable[Task]].map(x => println("Task: " + x.getId + " " + x.getName))
               println("Insert Task ID To Delete: ")
               val taskId = readLine().trim.toInt
               val tasksToMaintain = savedTasks.filterTable(taskId)
               val newDatabase = database.swapTable("Task", tasksToMaintain)
               mainLoopTaskMenu(user, newDatabase)
            }
            case _ => {
               println("Invalid choice!")
               mainLoopTaskMenu(user, database)
            }
         }
         mainMenu(user, database)
      }
   }

   def projectMenu(user: User, database: Database) = {
      mainLoopProjectMenu(user, database)

      def mainLoopProjectMenu(user: User, database: Database): Any = {
         println("1. Check Existing Projects")
         println("2. Create New Project")
         println("3. Delete Project")
         println("0. Go Back")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => println("Voltar ao menu principal")
            case "1" => {
               val savedProjects = database.getTableByName("Project")
               savedProjects.records.values map (x => println(x))
               mainLoopProjectMenu(user, database)
            }
            case "2" => {
               println("Insert New Project Name: ")
               val newProjectName = readLine().trim
               println("Insert Description: ")
               val description = readLine().trim
               val existingProjects = database.getTableByName("Project")
               val pr = new Project(user, newProjectName, description, {
                  if (existingProjects.records.values.size > 0) existingProjects.records.values.last.asInstanceOf[Project].getId + 1 else 0
               }, List(), List(), List(), new Date())
               val newDatabase = database.insertInTable(pr, "Project")
               mainLoopProjectMenu(user, newDatabase)
            }
            case "3" => {
               val className = "Project"
               val savedProjects = database.getTableByName(className)
               savedProjects.records.values.asInstanceOf[Iterable[Project]] map (x => println(x))
               println("Insert Project ID To Delete: ")
               val projectId = readLine().trim.toInt
               val projectsToMaintain = savedProjects.filterTable(projectId)
               val newDatabase = database.swapTable("Project", projectsToMaintain)
               mainLoopProjectMenu(user, newDatabase)
            }
            case _ => {
               println("Invalid choice!\n")
               mainMenu(user, database)
            }
         }
         mainMenu(user, database)
      }
   }

   def sharedFileMenu(user: User, database: Database) = {

      mainLoopFileMenu(user, database)

      def mainLoopFileMenu(user: User, database: Database): Any = {
         println("SharedFile Menu:\n")
         println("1. Check uploaded files")
         println("2. Upload new File")
         println("3. Discard File")
         println("0. Exit program")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => println("Exiting...")
            case "1" => {
               val savedFiles = database.getTableByName("SharedFile").records.values.asInstanceOf[Iterable[SharedFile]]
               if (savedFiles.size != 0) {
                  savedFiles.map(x => println("File: " + x.getId + " " + x.getName))
                  println("What file do you wish to inspect (ID)? Write Q if you don't wish to inspect any of them")
                  val fileId = readLine().trim
                  fileId match {
                     case "q" | "Q" => mainLoopFileMenu(user, database)
                     case _ => {
                        if (Try(fileId.toInt).isSuccess) inspectFileMenu(fileId, database, user)
                        else {
                           println("Not a valid file Id!")
                           mainLoopFileMenu(user, database)
                        }
                     }
                  }
               }
               else {
                  println("There are no shared files at the moment!")
                  mainLoopFileMenu(user, database)
               }
            }
            case "2" => {
               println("Insert file path: ")
               val newFilePath = readLine().trim
               val newFileName = newFilePath.split("/").last
               val savedFiles = database.getTableByName("SharedFile")
               val sh = new SharedFile(newFileName, {
                  if (savedFiles.records.values.size > 0) savedFiles.records.values.last.asInstanceOf[SharedFile].getId() + 1 else 0
               }, newFilePath, List())
               val newDatabase = database.insertInTable(sh, "SharedFile")
               mainLoopFileMenu(user, newDatabase)
            }
            case "3" => {
               val className = "SharedFile"
               val savedFiles = database.getTableByName(className)

               savedFiles.records.values.asInstanceOf[Iterable[SharedFile]].map(x => println("File: " + x.getId + " " + x.getName))
               println("Insert ID of file to delete: ")
               val fileId = readLine().trim.toInt
               val filesToMaintain = savedFiles.filterTable(fileId)
               val newDatabase = database.swapTable("SharedFile", savedFiles)
               mainLoopFileMenu(user, newDatabase)
            }
            case _ => {
               println("Invalid choice!")
               mainLoopFileMenu(user, database)
            }
         }
         mainMenu(user, database)
      }
   }


   def inspectFileMenu(fileId: String, database: Database, user: User): Unit = {
      val fileEntry = database.getTableByName("SharedFile").records.asInstanceOf[Map[Int, SharedFile]].find(x => x._2.id == fileId.toInt).get
   val file = fileEntry._2
      mainLoopInspectedFileMenu(file, database)
      def mainLoopInspectedFileMenu(inspectedFile : SharedFile, database: Database): Any = {
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
               sharedFileMenu(user, database)
            }
            case "1" =>
            {
               println("File Id: " + inspectedFile.getId)
               println("File Name: " + inspectedFile.getName)
               println("File Path: " + inspectedFile.getPath)
               println("Number of comments: " + inspectedFile.getComments.length) //TODO Add new comment boolean to comment class? Show if there are any new comments here instead?
               mainLoopInspectedFileMenu(inspectedFile, database)
            }
            case "2" =>
            {
               inspectedFile.getComments.map(x => println("Poster: " + x.user + " Data of comment: " + x.date + "\n" + x.content + "\n"))
               mainLoopInspectedFileMenu(inspectedFile, database)
            }
            case "3" =>
            {
               println("Write your comment: ")
               val commentContent = readLine.trim
               val newFile = SharedFile(inspectedFile.getName, inspectedFile.getId, inspectedFile.getPath, inspectedFile.getComments ++ List(new Comment(user, LocalDate.now, commentContent)))
               val className = "SharedFile"
               val savedFiles = database.getTableByName(className).records
               val newDatabase = database.swapTable(className, new Table(savedFiles - fileEntry._1 + (fileEntry._1 -> newFile), className))
               mainLoopInspectedFileMenu(newFile, newDatabase)
            }
         }
      }
   }
}