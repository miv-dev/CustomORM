package data.database.models

data class ArticleEntity(
    val id: Int,
    val text: String,
    val title: String,
    val authorId: Int
)

