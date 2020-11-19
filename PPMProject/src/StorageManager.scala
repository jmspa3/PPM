import java.io.{EOFException, File, FileInputStream, FileOutputStream, IOException, ObjectInputStream, ObjectOutputStream}

import DBPackage.{Database, Table}

import scala.annotation.tailrec

object StorageManager {

   def writeObjectListInFile(objectList: List[Serializable], pathToFile: String): Unit = {
      val savedDatabase = readDatabaseFile(pathToFile).asInstanceOf[Database]
      val className = objectList.head.getClass.getName
      var out = None: Option[ObjectOutputStream]
      try {
         out = Some(new ObjectOutputStream(new FileOutputStream(pathToFile)))
         out.get.writeObject(savedDatabase.swapTable(className, Table.createTable(className, objectList).asInstanceOf[Table]))
      } catch {
      case e: IOException => e.printStackTrace()
      } finally {
      if (out.isDefined) out.get.close()
      }
   }

   def addObjectToFile(dataToWrite: Serializable, pathToFile: String) {
      val savedDatabase = readDatabaseFile(pathToFile).asInstanceOf[Database]
      var out = None: Option[ObjectOutputStream]
      try {
         out = Some(new ObjectOutputStream(new FileOutputStream(pathToFile)))
         out.get.writeObject(savedDatabase.insertInTable(dataToWrite, dataToWrite.getClass.getName))
      } catch {
         case e: IOException => e.printStackTrace()
      } finally {
         if (out.isDefined) out.get.close()
      }
   }


   def readDatabaseFile(pathToFile: String) = {
      var in = None: Option[ObjectInputStream]
      var file = new File(pathToFile)
      file.createNewFile()
      try {
         in = Some(new ObjectInputStream(new FileInputStream(file)))
         val temp = in.get.readObject()
         temp
      } catch {
         case e1: EOFException => Database.newDatabase()
         case e2: IOException => e2.printStackTrace()
      } finally {
         if (in.isDefined) in.get.close()
      }
   }
}
