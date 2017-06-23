package edu.psu.vmhosting

import java.sql.Connection

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}


object DatabaseSupport {

  def getDataSource(dbConfig: DBConfig): HikariDataSource = {
    val hikariConfig = new HikariConfig()
    hikariConfig.setJdbcUrl(dbConfig.url)
    hikariConfig.setUsername(dbConfig.username)
    hikariConfig.setPassword(dbConfig.password)
    hikariConfig.setDriverClassName(dbConfig.driver)
    hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")

    new HikariDataSource(hikariConfig)
  }

  def withConnection[T](f: Connection => T)(implicit dbConfig: DBConfig): T = {
    val dataSource = getDataSource(dbConfig)
    val connection = dataSource.getConnection()
    try {
      f(connection)
    } finally {
      connection.close()
    }
  }
}
