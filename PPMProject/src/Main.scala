package PPMProject
import java.util.Date

import com.github.nscala_time.time.Imports.DateTime

import scala.io.StdIn.readLine
import scala.util.Try

object Main {


  def main(args: Array[String]): Unit = {
    println("Welcome to PPMProject. Insert your Username: ")
    val databasePath = "savedFiles"
    val pathToUsers = "savedUsers"
    val username = readLine().trim
    //procurar na BD o user com o nome fornecido
    val existingUsers = StorageManager.readDatabaseFile(databasePath)
    val teste = existingUsers.asInstanceOf[Database].getTableByName("User").records.values.asInstanceOf[Iterable[User]].toList
    var user: User = new User()
    try {
      val userlist = teste filter (x => x.getUsername == username)
      user = userlist.head
    } catch {
      case e: NoSuchElementException => user = createUser(teste, databasePath)
    }

    mainMenu(user, databasePath)
  }

  def createUser(existingUsers: List[User], databasePath: String): User = {
    println("We couldn't find you on the User Database. Please select one of the options: ")
    println("1. Create new User ")
    println("2. Continue as guest: ")


    val userChoice = readLine.trim
    userChoice match {
      case "1" => println("Insert your username")
        val username = readLine().trim
        new User(username, { if (existingUsers.length > 0) existingUsers.last.getUserId + 1 else 0 }, new Date(), List())
      case _ => println("Continuing as Guest")
        new User()
    }

  }

  def mainMenu(user: User, databasePath: String) {
    println("Main Menu:")
    println("1. Shared Files")
    println("2. Users")
    println("3. Projects")
    println("4. Tasks")
    println("0. Exit program")
    val userChoice = readLine.trim
    userChoice match {
      case "0" => println("Exiting...")
      case "1" => {
        sharedFileMenu(user, databasePath)
        mainMenu(user, databasePath)
      }

      case "2" => {
        userMenu(user, databasePath)
        mainMenu(user, databasePath)
      }
      case "3" => {
        projectMenu(user, databasePath)
        mainMenu(user, databasePath)
      }

      case "4" => {
       taskMenu(user, databasePath)
        mainMenu(user, databasePath)
      }
      case _ => {
        println("Invalid choice!")
        mainMenu(user, databasePath)
      }
    }
  }


  def userMenu(user: User, databasePath: String) = {
    val usersPath = "savedUsers"
    mainLoopUserMenu(user, databasePath)

    def mainLoopUserMenu(user: User, databasePath: String): Any = {
      println("User Menu:")
      println("1. Check Users")
      println("2. Create New User")
      println("3. Delete User")
      println("0. Voltar atrás")
      val userChoice = readLine.trim
      userChoice match {
        case "0" => println("Exiting...")
        case "1" => {
          val existingUsers = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("User")
          existingUsers.records.values map (x => println(x))
          mainLoopUserMenu(user, databasePath)
        }
        case "2" => {
          println("Insert Username: ")
          val newUser = readLine().trim
          val existingUsers = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("User")
          val us = new User(newUser, {
            if (existingUsers.records.values.size > 0) existingUsers.records.values.last.asInstanceOf[User].getUserId + 1 else 0
          }, new Date(), List())
          StorageManager.addObjectToFile(us, databasePath)
          mainLoopUserMenu(user, databasePath)
        }

        case "3" => {
          val className = "User"
          val existingUsers = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
          val savedUsers = existingUsers.getTableByName(className).records.values.asInstanceOf[Iterable[User]]


          savedUsers map (x => println(x))
          println("Insert ID of User to delete: ")
          val userId = readLine().trim.toInt
          val usersToMaintain = savedUsers filter (x => x.getUserId != userId)
          StorageManager.writeObjectListInFile(usersToMaintain.toList.asInstanceOf[List[User]], databasePath, className)
          mainLoopUserMenu(user, databasePath)
        }
        case _ => {
          println("Invalid choice!")
          mainLoopUserMenu(user, databasePath)
        }
      }
    }
  }
  def taskMenu(user: User, databasePath: String) = {
    val databasePath = "savedTasks"
    mainLoopTaskMenu(user, databasePath)

    def mainLoopTaskMenu(user: User, databasePath: String): Any = {
      println("Task Menu:\n")
      println("1. Check uploaded tasks")
      println("2. Upload new Task")
      println("3. Discard Task")
      println("0. Exit program")
      val userChoice = readLine.trim
      userChoice match {
        case "0" => println("Exiting...")
        case "1" => {
          val savedDatabase = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
          val savedFiles = savedDatabase.getTableByName("Task").records.values.asInstanceOf[Iterable[Task]]
          if (savedFiles.size != 0) {
            savedFiles.map(x => println("Task: " + x.getId + " " + x.getName))
          }
          else {
            println("There are no tasks at the moment!")
          }
          mainLoopTaskMenu(user, databasePath)
        }
        case "2" => {
          println("Insert task name: ")
          val newTaskName = readLine()
          val savedTasks = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("Task")
          val sh = new Task({
            if (savedTasks.records.values.size > 0) savedTasks.records.values.last.asInstanceOf[Task].getId() + 1 else 0
          },newTaskName, DateTime.now(),false,"High")

          StorageManager.addObjectToFile(sh, databasePath)
          mainLoopTaskMenu(user, databasePath)
        }
        case "3" => {
           val className = "Task"
           val existingProjects = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
           val savedTasks = existingProjects.getTableByName(className).records.values.asInstanceOf[Iterable[Task]]

           savedTasks.map(x => println("Task: " + x.getId + " " + x.getName))
           println("Insert Task ID To Delete: ")
           val taskId = readLine().trim.toInt
           val newExistingTasks = savedTasks filter (x => x.getId != taskId)
           try {
              StorageManager.writeObjectListInFile(newExistingTasks.toList, databasePath, className)
           } catch {
              case e: NoSuchElementException => println("There are no tasks to delete")
           }
           mainLoopTaskMenu(user, databasePath)
        }
        case _ => {
          println("Invalid choice!")
          mainLoopTaskMenu(user, databasePath)
        }
      }
    }   }
  def projectMenu(user: User, databasePath: String) = {
    val projectsPath = "savedProjects"
    mainLoopProjectMenu(user, databasePath)

    def mainLoopProjectMenu(user: User, databasePath: String): Any = {
      println("1. Check Existing Projects")
      println("2. Create New Project")
      println("3. Delete Project")
      println("0. Voltar atrás")
      val userChoice = readLine.trim
      userChoice match {
        case "0" => println("Voltar ao menu principal")
        case "1" => {
          val savedProjects = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("Project")
          savedProjects.records.values map (x => println(x))
          mainLoopProjectMenu(user, databasePath)
        }
        case "2" => {
          println("Insert New Project Name: ")
          val newProjectPath = readLine().trim
          println("Insert Description: ")
          val description = readLine().trim
          val newProjectName = newProjectPath.split("/").last
          val existingProjects = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName("Project")
          //val existingUsers = StorageManager.readObjectFromFile(usersPath).asInstanceOf[List[User]]
          val pr = new Project(user, newProjectName, description, {
            if (existingProjects.records.values.size > 0) existingProjects.records.values.last.asInstanceOf[Project].getProjectId + 1 else 0
          }, List(), List(), List(), new Date())
          StorageManager.addObjectToFile(pr, databasePath)
          mainLoopProjectMenu(user, databasePath)
        }
        case "3" => {
          val className = "Project"
          val existingProjects = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
          val savedProjects = existingProjects.getTableByName(className).records.values.asInstanceOf[Iterable[Project]]

          savedProjects map (x => println(x))
          println("Insert Project ID To Delete: ")
          val projectId = readLine().trim.toInt
          val newExistingProjects = savedProjects filter (x => x.getProjectId != projectId)
          try {
            StorageManager.writeObjectListInFile(newExistingProjects.toList, databasePath, className)
          } catch {
            case e: NoSuchElementException => println("There are no projects to delete")
          }
          mainLoopProjectMenu(user, databasePath)
        }
        case _ => {
          println("Invalid choice!\n")
          mainMenu(user, databasePath)
        }
      }
      mainMenu(user, databasePath)
    }
  }

  def sharedFileMenu(user: User, databasePath: String) = {

    mainLoopFileMenu(user, databasePath)

    def mainLoopFileMenu(user: User, databasePath: String): Any = {
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
              case "q" | "Q" => mainLoopFileMenu(user, databasePath)
              case _ => {
                if (Try(fileId.toInt).isSuccess) inspectFileMenu(fileId, databasePath, user)
                else {
                  println("Not a valid file Id!")
                  mainLoopFileMenu(user, databasePath)
                }
              }
            }
          }
          else {
            println("There are no shared files at the moment!")
            mainLoopFileMenu(user, databasePath)
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
          mainLoopFileMenu(user, databasePath)
        }
        case "3" => {
          val className = "SharedFile"
          val savedDatabase = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database]
          val savedFiles = savedDatabase.getTableByName(className).records.values.asInstanceOf[Iterable[SharedFile]]

          savedFiles.map(x => println("File: " + x.getId + " " + x.getName))
          println("Insert ID of file to delete: ")
          val fileId = readLine().trim.toInt
          val newFileList = savedFiles.filter(x => x.getId() != fileId)
          StorageManager.writeObjectListInFile(newFileList.toList, databasePath, className)
          mainLoopFileMenu(user, databasePath)
        }
        case _ => {
          println("Invalid choice!")
          mainLoopFileMenu(user, databasePath)
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
          sharedFileMenu(user, databasePath)
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
          val className = "SharedFile"
          val savedFiles = StorageManager.readDatabaseFile(databasePath).asInstanceOf[Database].getTableByName(className).records.values.asInstanceOf[Iterable[SharedFile]]

          val fileIndex = savedFiles.toList.indexOf(inspectedFile)
          val newSavedFiles = savedFiles.toList.updated(fileIndex, newFile)

          StorageManager.writeObjectListInFile(newSavedFiles, databasePath, className)
          mainLoopInspectedFileMenu(newFile)
        }
      }
    }
  }
}