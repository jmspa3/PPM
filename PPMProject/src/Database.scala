package PPMProject

import scala.collection.immutable.ListMap

case class Database(tables : List[Table]) {

  def insertInTable( objectToSave: SavedClass, tableName: String) = Database.insertInTable(this, objectToSave, tableName.split('.').last)
  def swapTable(tableName: String, newTable: Table) = Database.swapTable(this, tableName.split('.').last, newTable)
  def getTableByName(tableName: String) = Database.getTableByName(this, tableName.split('.').last)
}


object Database{

  type tables = List[Table]

  def insertInTable(database: Database, objectToSave: SavedClass, tableName: String): Database =
  {
    val table = database.getTableByName(tableName)
    new Database(database.tables.updated(database.tables.indexOf(table), table.insert(objectToSave)))
  }

  def swapTable(database: Database, tableName: String, newTable: Table): Database = {
    val newTables = database.tables.updated(database.tables.indexOf(database.getTableByName(tableName)), newTable)
    new Database(newTables)
  }

  def getTableByName(database: Database, tableName: String): Table = {
    database.tables.filter(x => x.tableName.equals(tableName)).head
  }

  def newDatabase() : Database = {
    val tableslist = List(Table(ListMap(),"Project"),Table(ListMap(),"Task"),Table(ListMap(),"User"),Table(ListMap(),"SharedFile"), Table(ListMap(), "Comment"))
    new Database(tableslist)
  }

}