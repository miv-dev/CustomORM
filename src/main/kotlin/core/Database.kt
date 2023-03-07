package core

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet


object Database {
    private val url = "jdbc:postgresql://localhost:5432/mike"
    private val user = "postgres"
    private val password = "postgres"
    private var connection: Connection = DriverManager.getConnection(url, user, password)

    fun createUserTable(){
        val sql = "CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY, name VARCHAR(50))"
        val statement = connection.createStatement()
        statement.executeUpdate(sql)
    }
    fun createArticleTable(){
        val query = "CREATE TABLE IF NOT EXISTS article (id INT PRIMARY KEY, title VARCHAR(100), text TEXT, author_id INT, FOREIGN KEY (author_id) REFERENCES users(id))"
        val statement = connection.createStatement()
        statement.executeUpdate(query)
    }

    fun executeQuery(query: String): ResultSet {
        val statement = connection.createStatement()
        return statement.executeQuery(query)
    }

    fun executeCreateQuery(query: String){
        val statement = connection.createStatement()
        statement.execute(query)
    }

    fun executeUpdate(query: String) {
        val statement = connection.createStatement()
        statement.executeUpdate(query)
    }
}
