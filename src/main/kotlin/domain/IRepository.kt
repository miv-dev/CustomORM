package domain

interface IRepository {

    fun allArticles(): List<Article>

    fun allUsers(): List<User>

    fun getArticleById(id: Int): Article?

    fun getUserById(id: Int): User?

    fun createUser(user: User): Result<Unit>
    fun createArticle(article: Article): Result<Unit>
}
