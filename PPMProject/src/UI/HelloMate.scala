package UI

import java.time.LocalDate

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import PPMProject.Task

class HelloMate extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("My Hello World App")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("Login/LoginController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
object FxApp {
  var task: Task = Task(1,"Jose", LocalDate.now(),false,"High")

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[HelloMate], args: _*)
  }
}