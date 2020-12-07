package PPMProject

case class Database(tables : List[Table]) {

  def insertInTable( objectToSave: Serializable, tableName: String) = Database.insertInTable(this, objectToSave, tableName.split('.').last)
  def swapTable(tableName: String, newTable: Table) = Database.swapTable(this, tableName.split('.').last, newTable)
  def getTableByName(tableName: String) = Database.getTableByName(this, tableName.split('.').last)
}


object Database{

  type tables = List[Table]

  def insertInTable(database: Database, objectToSave: Serializable, tableName: String): Database =
  {
    val table = database.getTableByName(tableName)
    new Database(database.tables.updated(database.tables.indexOf(table), table.insert(objectToSave: Serializable)))
  }

  def swapTable(database: Database, tableName: String, newTable: Table): Database = {
    val newTables = database.tables.updated(database.tables.indexOf(database.getTableByName(tableName)), newTable)
    new Database(newTables)
  }

  def getTableByName(database: Database, tableName: String): Table = {
    database.tables.filter(x => x.tableName.equals(tableName)).head
  }

  def newDatabase() : Database = {
    val tableslist = List(Table(Map(),"Project"),Table(Map(),"Task"),Table(Map(),"User"),Table(Map(),"SharedFile"))
    new Database(tableslist)
  }

}