package PPMProject

import java.io.Serializable
import com.github.nscala_time.time.Imports.DateTime

case class Comment(user: User, date: DateTime, content: String)

case class SharedFile(fileName: String, id: Int, path: String, comments: List[Comment]) extends Serializable{
  def getName() = SharedFile.getName(this)
  def getId() = SharedFile.getId(this)
  def getPath() = SharedFile.getPath(this)
  def getComments() = SharedFile.getComments(this)
  def addComment(comment: Comment) = SharedFile.addComment(this, comment)
  override def toString() = SharedFile.toString(this)

}


object SharedFile {

  type fileName = String
  type id = Int
  type path = String
  type comments = List[Comment]

  def getName(sh: SharedFile) =
  {
    sh.fileName
  }

  def getId(sh: SharedFile) =
  {
    sh.id
  }

  def getPath(sh: SharedFile) =
  {
    sh.path
  }

  def getComments(sh: SharedFile) = {
    sh.comments
  }

  def addComment(sh: SharedFile, comment: Comment): SharedFile =
  {
    new SharedFile(sh.fileName, sh.id, sh.path, sh.comments ++ List(comment))
  }

  def toString(sh: SharedFile): String =
  {
    sh.id + ": " + sh.fileName
  }

}