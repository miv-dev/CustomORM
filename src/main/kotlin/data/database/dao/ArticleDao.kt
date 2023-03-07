package data.database.dao

import core.Database
import data.database.models.ArticleEntity
import java.sql.ResultSet


object ArticleDao {

    private val db: Database = Database
    init {
        db.createArticleTable()
    }

    fun create(article: ArticleEntity) {
        val articleEntity = if (article.id == -1) {
            val maxID = all().maxOf { it.id }
            article.copy(id = maxID + 1)
        } else {
            article
        }

        val query =
            "INSERT INTO article (id, title, text, author_id) VALUES (${articleEntity.id}, '${articleEntity.title}', '${articleEntity.text}', ${articleEntity.authorId})"
        db.executeCreateQuery(query)
    }

    fun all(): List<ArticleEntity> {
        val query = "SELECT * FROM article"
        val resultSet = db.executeQuery(query)
        return getArticlesFromQuery(resultSet)
    }

    fun read(id: Int): ArticleEntity? {
        val query = "SELECT * FROM article WHERE id = $id"
        val resultSet = db.executeQuery(query)
        return if (resultSet.next()) {
            ArticleEntity(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("text"),
                resultSet.getInt("author_id")
            )
        } else {
            null
        }
    }

    fun update(article: ArticleEntity) {
        val query =
            "UPDATE article SET title = '${article.title}', text = '${article.text}', author_id = ${article.authorId} WHERE id = ${article.id}"
        db.executeUpdate(query)
    }

    fun delete(id: Int) {
        val query = "DELETE FROM article WHERE id = $id"
        db.executeUpdate(query)
    }

    private fun getArticlesFromQuery(resultSet: ResultSet): List<ArticleEntity> {
        val articles = mutableListOf<ArticleEntity>()
        while (resultSet.next()) {
            articles.add(
                ArticleEntity(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("text"),
                    resultSet.getInt("author_id")
                )
            )
        }
        return articles
    }

    fun findByAuthor(authorId: Int): List<ArticleEntity> {
        val query = "SELECT * FROM article WHERE author_id = $authorId"
        val resultSet = db.executeQuery(query)
        return getArticlesFromQuery(resultSet)
    }


}
