package UI


import java.time.LocalDate
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage


class GUIMain extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("StudentHub: Project Manager")
    val fxmlLoader =
      new FXMLLoader(getClass.getResource("Login/LoginController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}
object FxApp {

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GUIMain], args: _*)
  }
}