package data.repositories

import core.Database
import data.database.dao.ArticleDao
import data.database.dao.UserDao
import data.mappers.ArticleMapper
import data.mappers.UserMapper
import domain.Article
import domain.IRepository
import domain.User

class RepositoryImpl : IRepository {


    override fun allArticles(): List<Article> {
        val articles = mutableListOf<Article>()
        ArticleDao.all().forEach {articleEntity ->
            val userEntity = UserDao.read(articleEntity.authorId)
            if (userEntity != null) {
                val article = ArticleMapper.toDomain(articleEntity, userEntity)
                articles.add(article)
            }
        }
        return articles
    }

    override fun allUsers(): List<User> = UserDao.all().map { entity -> UserMapper.toDomain(entity) }


    override fun getArticleById(id: Int): Article? {
        ArticleDao.read(id)?.let { articleEntity ->
            UserDao.read(articleEntity.authorId)?.let { userEntity ->
                return ArticleMapper.toDomain(articleEntity, userEntity)
            }
        }
        return null
    }

    override fun getUserById(id: Int): User? {
        UserDao.read(id)?.let { userEntity ->
            return UserMapper.toDomain(userEntity)
        }
        return null
    }

    override fun createUser(user: User): Result<Unit> {
        val userEntity = UserMapper.toEntity(user)
        return try {
            UserDao.create(userEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun createArticle(article: Article): Result<Unit> {

        val articleEntity = ArticleMapper.toEntity(article)

        return try {
            ArticleDao.create(articleEntity)
            Result.success(Unit)
        } catch (e: Exception) {
            print(e)
            Result.failure(e)
        }
    }
}
