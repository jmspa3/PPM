package PPMProject

import scala.annotation.tailrec

case class Table(records : Map[Int, SavedClass], tableName : String) {

   def insert(entry: SavedClass): Table = Table.insert(this, entry)
   def filterTable(userId: Int): Table = Table.filterTable(this, userId)
   def getFromIdList(idList: List[Int]): List[SavedClass] = Table.getFromIdList(this, idList)
   def updateTable(oldEntry: (Int,SavedClass), newValue: SavedClass): Table = Table.updateTable(this, oldEntry, newValue)
}


object Table {

   type records = Map[Int, SavedClass]
   type tableName = String

   def insert(table: Table, entry: SavedClass): Table = {
      if (table.records.size != 0) Table(table.records + (table.records.last._1 + 1 -> entry), table.tableName)
      else Table(Map(0 -> entry), table.tableName)
   }

   def createTable(tableName: String, entryList: List[SavedClass]): Table = {
      createTableLoop(Map(), entryList, tableName)

   }

   def createTableLoop(map: Map[Int, SavedClass], l: List[SavedClass], tableName: String): Table = l match {
      case Nil => Table(map, tableName)
      case h::t =>
      {
         val newMap = map + (map.size -> h)
         createTableLoop(newMap, t, tableName)
      }
   }

   def filterTable(table: Table, userId: Int): Table =
   {
      val entriesToRemove = getEntriesToRemove(table.records, userId, List())
      new Table(table.records -- entriesToRemove,table.tableName)
   }

   @tailrec def getEntriesToRemove(original: Map[Int, SavedClass], userId: Int, toRemove: List[Int]): List[Int] =
   {
      original.size match {
         case 0 => toRemove;
         case _ =>
         {
            if (original.head._2.getId() equals userId) getEntriesToRemove(original.tail, userId, toRemove ++ List(original.head._1))
            else getEntriesToRemove(original.tail, userId, toRemove)
         }
      }
   }

   def getFromIdList(table: Table, idList: List[Int]): List[SavedClass] =
   {
      table.records.values.filter(x => idList.contains(x.getId())).toList
   }

   def updateTable(t: Table, oldEntry: (Int, SavedClass), newValue: SavedClass): Table =
   {
      val temp = Table(t.records - oldEntry._1 + (oldEntry._1 -> newValue), t.tableName)
      println(temp)
      temp
   }

}
