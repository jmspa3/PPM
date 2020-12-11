package PPMProject

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

case class Table(records : ListMap[Int, SavedClass], tableName : String) {

   def insert(entry: SavedClass): Table = Table.insert(this, entry)
   def filterTable(id: Int): Table = Table.filterTable(this, id)
   def getFromIdList(idList: List[Int]): List[SavedClass] = Table.getFromIdList(this, idList)
   def filterTableFromList(idList: List[Int]): Table = Table.filterTableFromList(this, idList)
   def updateTable(oldEntry: (Int,SavedClass), newValue: SavedClass): Table = Table.updateTable(this, oldEntry, newValue)
}


object Table {

   def insert(table: Table, entry: SavedClass): Table = {
      if (table.records.size != 0) Table(table.records + (table.records.last._1 + 1 -> entry), table.tableName)
      else Table(ListMap(0 -> entry), table.tableName)
   }

   def createTable(tableName: String, entryList: List[SavedClass]): Table = {
      createTableLoop(ListMap(), entryList, tableName)

   }

   @tailrec def createTableLoop(map: ListMap[Int, SavedClass], l: List[SavedClass], tableName: String): Table = l match {
      case Nil => Table(map, tableName)
      case h::t =>
      {
         val newMap = map + (map.size -> h)
         createTableLoop(newMap, t, tableName)
      }
   }

   def filterTableFromList(table: Table, idList: List[Int]): Table = {
      idList match {
         case Nil => table
         case h::t => filterTableFromList(filterTable(table, h), t)
      }
   }

   def filterTable(table: Table, id: Int): Table =
   {
      val entriesToRemove = getEntriesToRemove(table.records, id, List())
      new Table(table.records -- entriesToRemove,table.tableName)
   }

   @tailrec def getEntriesToRemove(original: Map[Int, SavedClass], id: Int, toRemove: List[Int]): List[Int] =
   {
      original.size match {
         case 0 => toRemove;
         case _ =>
         {
            if (original.head._2.getId() equals id) getEntriesToRemove(original.tail, id, toRemove ++ List(original.head._1))
            else getEntriesToRemove(original.tail, id, toRemove)
         }
      }
   }

   def getFromIdList(table: Table, idList: List[Int]): List[SavedClass] =
   {
      table.records.values.filter(x => idList.contains(x.getId())).toList
   }

   def updateTable(t: Table, oldEntry: (Int, SavedClass), newValue: SavedClass): Table =
   {
      Table(t.records - oldEntry._1 + (oldEntry._1 -> newValue), t.tableName)
   }

}
