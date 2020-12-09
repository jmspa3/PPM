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
      val userList = database.getTableByName("User").records.values.toList.asInstanceOf[List[User]]
      try {
         val existingUser = userList filter (x => x.getUsername == username)
         val newUser = existingUser.head
         mainMenu(newUser, database)
      } catch {
         case e: NoSuchElementException => {
            val newInfo = registerUser(userList, database)
            mainMenu(newInfo._1, newInfo._2)
         }
      }

   }

   def registerUser(existingUsers: List[User], database: Database): (User, Database) = {
      println("We couldn't find you on the User Database. Please select one of the options: ")
      println("1. Create new User ")
      println("2. Continue as guest: ")


      val userChoice = readLine.trim
      userChoice match {
         case "1" => {
            println("Insert your username")
            val username = readLine().trim
            createUser(username)(database)
         }
         case _ => {
            println("Continuing as Guest") //Have a guest User already created?
            (new User(), database)
         }
      }

   }

   def createUser(newUser: String)(database: Database): (User, Database) = {
      val existingUsers = database.getTableByName("User")
      val us = new User({
         if (existingUsers.records.values.size > 0) existingUsers.records.values.last.asInstanceOf[User].getId + 1 else 0
      }, newUser)
      val newDatabase = database.insertInTable(us, "User")
      (us, newDatabase)
   }


   def mainMenu(user: User, database: Database) {
      println("Main Menu:")
      println("1. Users")
      println("2. Projects")
      println("3. Save Progress")
      println("0. Exit program")
      val userChoice = readLine.trim
      userChoice match {
         case "0" => println("Exiting...")
         case "1" => {
            userMenu(user, database)
            mainMenu(user, database)
         }
         case "2" => {
            projectMenu(user, database)
            mainMenu(user, database)
         }
         case "3" => {
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
            case "0" =>
            {
               println("Exiting...")
               mainMenu(user, database)
            }
            case "1" => {
               val existingUsers = database.getTableByName("User")
               existingUsers.records.values.toList.asInstanceOf[List[User]] map (x => println(x.customToString(database)))
               mainLoopUserMenu(user, database)
            }
            case "2" => {
               println("Insert Username: ")
               val newUser = readLine().trim
               val newInfo = createUser(newUser)(database)
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

   def taskMenu(user: User, projectId: Int, database: Database) = {
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == projectId).get
      mainLoopTaskMenu(user, projectEntry, database)

      def mainLoopTaskMenu(user: User, projectEntry: (Int, Project), database: Database): Any = {
         println("Task Menu:\n")
         println("1. Check tasks")
         println("2. Upload new Task")
         println("3. Discard Task")
         println("4. Check High priority tasks")
         println("5. Check Medium priority tasks")
         println("6. Check Low priority tasks")
         println("0. Go Back")
         val userChoice = readLine.trim
         userChoice match {
            case "0" =>
            {
               println("Exiting...")
               inspectProjectMenu(user, projectEntry._1, database)
            }
            case "1" => {
               val savedTasks = projectEntry._2.getTasks(database)
               if (savedTasks.size != 0) {
                  savedTasks.map(x => println("Task: " + x.getId + " " + x.getName))
               }
               else {
                  println("There are no tasks at the moment!")
               }
               mainLoopTaskMenu(user, projectEntry,database)
            }
            case "2" => {
               println("Insert task name: ")
               val newTaskName = readLine.trim
               val savedTasks = database.getTableByName("Task")
               println("Insert task deadline (DD-MM-YYYY): ")
               val deadlineString = readLine.trim
               val deadlineList = deadlineString.split("-")
               val deadline = LocalDate.of(deadlineList(2).toInt, deadlineList(1).toInt, deadlineList(0).toInt)
               println("What priority should the task be? (1-Low, 2-Medium, 3-High)")
               val priority = readLine.trim
               val t = new Task({
                  if (savedTasks.records.values.size > 0) savedTasks.records.values.last.asInstanceOf[Task].getId() + 1 else 0
               }, user.getId, projectEntry._2.getId, deadline = deadline, name = newTaskName, priority = {
                  priority match {
                     case "2" => MediumPriority;
                     case "3" => HighPriority;
                     case _ => LowPriority;
                  }
               })
               val newProject = projectEntry._2.addTask(t)
               val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
               val newDatabase = tempDatabase.insertInTable(t, "Task")
               mainLoopTaskMenu(user, (projectEntry._1, newProject),newDatabase)
            }
            case "3" => {
               val className = "Task"
               val savedTasks = database.getTableByName(className)

               savedTasks.records.values.asInstanceOf[Iterable[Task]].map(x => println("Task: " + x.getId + " " + x.getName))
               println("Insert Task ID To Delete: ")
               val taskId = readLine().trim.toInt
               val tasksToMaintain = savedTasks.filterTable(taskId)
               val newProject = projectEntry._2.removeTask(taskId)
               val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
               val newDatabase = tempDatabase.swapTable("Task", tasksToMaintain)
               mainLoopTaskMenu(user,(projectEntry._1, newProject) ,newDatabase)
            }
            case "4" => {
               val savedTasks = projectEntry._2.getHighPriorityTasks(database)
               if (savedTasks.size != 0) {
                  savedTasks.map(x => println("Task: " + x.getId + " " + x.getName))
               }
               else {
                  println("There are no high priority tasks at the moment!")
               }
               mainLoopTaskMenu(user, projectEntry,database)
            }
            case "5" => {
               val savedTasks = projectEntry._2.getMediumPriorityTasks(database)
               if (savedTasks.size != 0) {
                  savedTasks.map(x => println("Task: " + x.getId + " " + x.getName))
               }
               else {
                  println("There are no medium priority tasks at the moment!")
               }
               mainLoopTaskMenu(user, projectEntry,database)
            }
            case "6" => {
               val savedTasks = projectEntry._2.getLowPriorityTasks(database)
               if (savedTasks.size != 0) {
                  savedTasks.map(x => println("Task: " + x.getId + " " + x.getName))
               }
               else {
                  println("There are no low priority tasks at the moment!")
               }
               mainLoopTaskMenu(user, projectEntry,database)
            }
            case _ => {
               println("Invalid choice!")
               mainLoopTaskMenu(user, projectEntry, database)
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
               val savedProjects = database.getTableByName("Project").records.values.asInstanceOf[Iterable[Project]]
               if (savedProjects.size != 0) {
                  savedProjects.map(x => println(x.customToString(database)))
                  println("What Project do you wish to inspect (ID)? Write Q if you don't wish to inspect any of them")
                  val projectId = readLine().trim
                  projectId match {
                     case "q" | "Q" => mainLoopProjectMenu(user, database)
                     case _ => {
                        if (Try(projectId.toInt).isSuccess) inspectProjectMenu(user, projectId.toInt, database)
                        else {
                           println("Not a valid project Id!")
                           mainLoopProjectMenu(user, database)
                        }
                     }
                  }
               }
               else {
                  println("There are no shared files at the moment!")
                  mainLoopProjectMenu(user, database)
               }
            }
            case "2" => {
               println("Insert New Project Name: ")
               val newProjectName = readLine().trim
               println("Insert Description: ")
               val description = readLine().trim
               val existingProjects = database.getTableByName("Project")
               val pr = new Project({
                  if (existingProjects.records.values.size > 0) existingProjects.records.values.last.asInstanceOf[Project].getId + 1 else 0
               }, user.getId, name = newProjectName, description = description)
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

   def inspectProjectMenu(user: User, projectId: Int, database: Database): Unit = {
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == projectId.toInt).get
      val project = projectEntry._2
      mainLoopInspectedProjectMenu(project, database)
      def mainLoopInspectedProjectMenu(project: Project, database: Database): Unit =
      {
         println("Inspected Project Menu:\n")
         println("1. Check properties")
         println("2. Files")
         println("3. Tasks")
         println("4. Add user")
         println("0. Go back to the Project Menu")
         val userChoice = readLine.trim
         userChoice match {
            case "0" => {
               println("Exiting...")
               projectMenu(user, database)
            }
            case "1" => {
               println("Project Id: " + project.getId)
               println("Project Name: " + project.getProjectName)
               println("Number of members: " + project.getMemberIds.length)
               println("Number of files: " + project.getFileIds.length)
               println("Number of tasks: " + project.getTaskIds.length)
               mainLoopInspectedProjectMenu(project, database)
            }
            case "2" => {
               sharedFileMenu(user, projectId, database)
            }
            case "3" => {
               taskMenu(user, projectId, database)
            }
            case "4" => {
               val existingUsers = database.getTableByName("User").records.values.toList.asInstanceOf[List[User]]
               existingUsers.map(x => println(x.customToString(database)))
               println("What user do you wish to add (ID)? Write Q if you don't wish to inspect any of them")
               val userId = readLine().trim
               userId match {
                  case "q" | "Q" => mainLoopInspectedProjectMenu(project, database)
                  case _ => {
                     if (Try(userId.toInt).isSuccess)
                     {
                        val newUser = database.getTableByName("User").records.asInstanceOf[Map[Int, User]].find(x => x._2.id == userId.toInt).get._2
                        val newProject = project.addMember(newUser)
                        val newDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
                        mainLoopInspectedProjectMenu(newProject, newDatabase)
                     }
                     else {
                        println("Not a valid file Id!")
                        mainLoopInspectedProjectMenu(project, database)
                     }
                  }
               }
            }
         }
      }
   }

   def sharedFileMenu(user: User, projectId: Int, database: Database) = {
      val projectEntry = database.getTableByName("Project").records.asInstanceOf[Map[Int, Project]].find(x => x._2.id == projectId).get
      val project = projectEntry._2
      mainLoopFileMenu(user, projectEntry, database)

      def mainLoopFileMenu(user: User, projectEntry: (Int, Project), database: Database): Any = {
         println("SharedFile Menu:\n")
         println("1. Check uploaded files")
         println("2. Upload new File")
         println("3. Discard File")
         println("0. Exit program")
         val userChoice = readLine.trim
         userChoice match {
            case "0" =>
            {
               println("Exiting...")
               inspectProjectMenu(user, projectEntry._1, database)
            }
            case "1" => {
               val savedFiles = projectEntry._2.getFiles(database)
               if (savedFiles.size != 0) {
                  savedFiles.map(x => println("File: " + x.getId + " " + x.getName))
                  println("What file do you wish to inspect (ID)? Write Q if you don't wish to inspect any of them")
                  val fileId = readLine().trim
                  fileId match {
                     case "q" | "Q" => mainLoopFileMenu(user, projectEntry, database)
                     case _ => {
                        if (Try(fileId.toInt).isSuccess) inspectFileMenu(fileId, database, user)
                        else {
                           println("Not a valid file Id!")
                           mainLoopFileMenu(user, projectEntry, database)
                        }
                     }
                  }
               }
               else {
                  println("There are no shared files at the moment!")
                  mainLoopFileMenu(user, projectEntry, database)
               }
            }
            case "2" => {
               println("Insert file path: ")
               val newFilePath = readLine().trim
               val newFileName = newFilePath.split("/").last
               val savedFiles = database.getTableByName("SharedFile")
               val sh = new SharedFile({
                  if (savedFiles.records.values.size > 0) savedFiles.records.values.last.asInstanceOf[SharedFile].getId() + 1 else 0
               }, user.getId, project.getId, fileName = newFileName, path = newFilePath)
               val newProject = projectEntry._2.addFile(sh)
               val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
               val newDatabase = tempDatabase.insertInTable(sh, "SharedFile")
               mainLoopFileMenu(user, (projectEntry._1, newProject), newDatabase)
            }
            case "3" => {
               val className = "SharedFile"
               val savedFiles = database.getTableByName(className)
               savedFiles.records.values.asInstanceOf[Iterable[SharedFile]].map(x => println("File: " + x.getId + " " + x.getName))
               println("Insert ID of file to delete: ")
               val fileId = readLine().trim.toInt
               val filesToMaintain = savedFiles.filterTable(fileId)
               val newProject = projectEntry._2.removeFile(fileId)
               val tempDatabase = database.swapTable("Project", database.getTableByName("Project").updateTable(projectEntry, newProject))
               val newDatabase = tempDatabase.swapTable("SharedFile", filesToMaintain)
               mainLoopFileMenu(user, (projectEntry._1, newProject), newDatabase)
            }
            case _ => {
               println("Invalid choice!")
               mainLoopFileMenu(user, projectEntry, database)
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
               sharedFileMenu(user, file.getProjectId, database)
            }
            case "1" =>
            {
               println("File Id: " + inspectedFile.getId)
               println("File Name: " + inspectedFile.getName)
               println("File Path: " + inspectedFile.getPath)
               println("Number of comments: " + inspectedFile.getCommentIds.length)
               mainLoopInspectedFileMenu(inspectedFile, database)
            }
            case "2" =>
            {
               inspectedFile.getComments(database).map(x => println(x.customToString(database)))
               mainLoopInspectedFileMenu(inspectedFile, database)
            }
            case "3" =>
            {
               println("Write your comment: ")
               val commentContent = readLine.trim
               val newComment = Comment({
                  if (database.getTableByName("Comment").records.size > 0) database.getTableByName("Comment").records.values.toList.asInstanceOf[List[Comment]].last.getId() + 1 else 0
               }, user.getId, content = commentContent)
               val newFile = inspectedFile.addComment(newComment)
               val className = "SharedFile"
               val savedFiles = database.getTableByName(className)
               val tempDatabase = database.swapTable(className, savedFiles.updateTable(fileEntry, newFile))
               val newDatabase = database.insertInTable(newComment, "Comment")
               mainLoopInspectedFileMenu(newFile, newDatabase)
            }
         }
      }
   }
}