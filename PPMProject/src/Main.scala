package PPM;
import java.util.Date

import scala.io.StdIn.readLine

object Main {


  def main(args: Array[String]): Unit = {
    println("Welcome to PPMProject. Insert your Username: ")
    val username = readLine().trim
    //procurar na BD o user com o nome fornecido
    val existingUsers = StorageManager.readObjectFromFile("savedUsers").asInstanceOf[List[User]]
    val userlist = existingUsers.asInstanceOf[List[User]] filter(x => x.getUsername == username)
    val user = userlist.head
    mainMenu(user)
  }

  def mainMenu(user: User) {
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
        //sharedFileMenu(user)
      }

      case "2" => {
        userMenu(user)
      }
      case "3" => {
        projectMenu(user)
      }

      case "4" => {
        //taskMenu(user)
      }
      case _ => {
        println("Invalid choice!")
        mainMenu(user)
      }
    }
  }


  def userMenu(user: User) = {
    val usersPath = "savedUsers"
    mainLoopUserMenu(user)

    def mainLoopUserMenu(user: User): Any = {
      println("SharedFile Menu:")
      println("1. Check Users")
      println("2. Create New User")
      println("3. Delete User")
      println("0. Exit program")
      val userChoice = readLine.trim
      userChoice match {
        case "0" => println("Exiting...")
        case "1" => {
          val existingUsers = StorageManager.readObjectFromFile(usersPath)
          existingUsers.asInstanceOf[List[User]] map (x => println(x))
          mainLoopUserMenu(user)
        }
        case "2" => {
          println("Insert Username: ")
          val newUser = readLine().trim
          val existingUsers = StorageManager.readObjectFromFile(usersPath).asInstanceOf[List[User]]
          val us = new User(newUser, {
            if (existingUsers.length > 0) existingUsers.last.getUserId + 1 else 0
          }, new Date(), List())
          StorageManager.writeObjectInFile(us, usersPath, append = true)
          mainLoopUserMenu(user)
        }

        case "3" => {
          val existingUsers = StorageManager.readObjectFromFile(usersPath)
          existingUsers.asInstanceOf[List[User]] map (x => println(x))
          println("Insert ID of User to delete: ")
          val userId = readLine().trim.toInt
          val userToDelete = existingUsers.asInstanceOf[List[User]] filter (x => x.getUserId != userId)
          StorageManager.writeObjectListInFile(userToDelete, usersPath, append = false)
          mainLoopUserMenu(user)
        }
        case _ => {
          println("Invalid choice!")
          mainLoopUserMenu(user)
        }
      }
    }
  }

  def projectMenu(user: User) = {
    val projectsPath = "savedProjects"
    mainLoopProjectMenu(user)

    def mainLoopProjectMenu(user: User): Any = {
      println("1. Check Existing Projects")
      println("2. Create New Project")
      println("3. Delete Project")
      val userChoice = readLine.trim
      userChoice match {
        case "1" => {
          val savedProjects = StorageManager.readObjectFromFile(projectsPath)
          savedProjects.asInstanceOf[List[Project]] map (x => println(x))
          mainLoopProjectMenu(user)
        }
        case "2" => {
          println("Insert New Project Name: ")
          val newProjectPath = readLine().trim
          println("Insert Description: ")
          val description = readLine().trim
          val newProjectName = newProjectPath.split("/").last
          val existingProjects = StorageManager.readObjectFromFile(projectsPath).asInstanceOf[List[Project]]
          //val existingUsers = StorageManager.readObjectFromFile(usersPath).asInstanceOf[List[User]]
          val pr = new Project(user, newProjectName, description, {
            if (existingProjects.length > 0) existingProjects.last.getProjectId() + 1 else 0
          }, List(), List(), List(), new Date())
          StorageManager.writeObjectInFile(pr, projectsPath, append = true)
          mainLoopProjectMenu(user)
        }
        case "3" => {
          val existingProjects = StorageManager.readObjectFromFile(projectsPath)
          existingProjects.asInstanceOf[List[Project]] map (x => println(x))
          println("Insert Project Name To Delete: ")
          val projectName = readLine().trim.toInt
          val newExistingProjects = existingProjects.asInstanceOf[List[Project]] filter (x => x.getProjectName() != projectName)
          StorageManager.writeObjectListInFile(newExistingProjects, projectsPath, append = false)
          mainLoopProjectMenu(user)
        }
        case _ => {
          println("Invalid choice!")
          mainMenu(user)
        }
      }
    }
  }
}