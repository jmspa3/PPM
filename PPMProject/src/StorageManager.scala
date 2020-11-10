import java.io.{EOFException, File, FileInputStream, FileOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

import scala.annotation.tailrec

object StorageManager {

   @tailrec def writeObjectListInFile[A](fileList: List[A], pathToFile: String, append: Boolean): Unit = fileList match {
      case Nil => ;
      case a::t => {
         writeObjectInFile(a, pathToFile, append)
         writeObjectListInFile(t, pathToFile, append = true)
      }
   }

   def writeObjectInFile[A](dataToWrite: A, pathToFile: String, append: Boolean) {
      val savedData = readObjectFromFile(pathToFile)
      var out = None: Option[ObjectOutputStream]
      try {
         out = Some(new ObjectOutputStream(new FileOutputStream(pathToFile)))
         if (append) out.get.writeObject(savedData.asInstanceOf[List[A]] ++ List(dataToWrite))
         else  out.get.writeObject(List(dataToWrite))
      } catch {
         case e: IOException => e.printStackTrace()
      } finally {
         if (out.isDefined) out.get.close()
      }
   }


   def readObjectFromFile[A](pathToFile: String) = {
      var in = None: Option[ObjectInputStream]
      var file = new File(pathToFile)
      file.createNewFile()
      try {
         in = Some(new ObjectInputStream(new FileInputStream(file)))
         in.get.readObject()
      } catch {
         case e1: EOFException => List[A]()
         case e2: IOException => e2.printStackTrace()
      } finally {
         if (in.isDefined) in.get.close()
      }
   }
}
