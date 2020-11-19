package DBPackage

case class Table(records : Map[Int, Serializable], tableName : String) {

   def insert(entry: Serializable) = Table.insert(this, entry)

}


object Table {

   type records = Map[Int, Serializable]
   type tableName = String

   def insert(table: Table, entry: Serializable): Table = {
      if (table.records.size != 0) Table(table.records + (table.records.last._1 + 1 -> entry), table.tableName)
      else Table(Map(0 -> entry), table.tableName)
   }

   def createTable(tableName: String, entryList: List[Serializable]): Table = {
      createTableLoop(Map(), entryList, tableName)

   }

   def createTableLoop(map: Map[Int, Serializable], l: List[Serializable], tableName: String): Table = l match {
      case Nil => Table(map, tableName)
      case h::t =>
      {
         val newMap = map + (map.size -> h)
         createTableLoop(newMap, t, tableName)
      }
   }

}

