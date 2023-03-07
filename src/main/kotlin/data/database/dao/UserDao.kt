package data.database.dao

import core.Database
import data.database.models.UserEntity
import java.sql.ResultSet

object UserDao {

    private val db: Database = Database

    init {
        db.createUserTable()
    }

    fun create(user: UserEntity) {
        val userEntity = if (user.id == -1) {
            val maxID = all().maxOf { it.id }
            user.copy(id = maxID + 1)
        } else {
            user
        }
        val query = "INSERT INTO users (id, name) VALUES (${userEntity.id}, '${userEntity.name}')"

        db.executeCreateQuery(query)
    }

    fun read(id: Int): UserEntity? {
        val query = "SELECT * FROM users WHERE id = $id"
        val resultSet = db.executeQuery(query)
        return if (resultSet.next()) {
            UserEntity(
                resultSet.getInt("id"),
                resultSet.getString("name"),
            )
        } else {
            null
        }
    }

    fun all(): List<UserEntity>{
        val query = "SELECT * FROM users"
        val resultSet = db.executeQuery(query)
        return getUsersFromQuery(resultSet)
    }

    private fun getUsersFromQuery(resultSet: ResultSet): List<UserEntity> {
        val users = mutableListOf<UserEntity>()
        while (resultSet.next()) {
            users.add(
                UserEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                )
            )
        }
        return users
    }

    fun update(person: UserEntity) {
        val query = "UPDATE users SET name = '${person.name}', WHERE id = ${person.id}"
        db.executeUpdate(query)
    }

    fun delete(id: Int) {
        val query = "DELETE FROM users WHERE id = $id"
        db.executeUpdate(query)
    }
}
