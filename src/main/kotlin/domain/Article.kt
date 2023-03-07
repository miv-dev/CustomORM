package domain

import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val id: Int = -1,
    val text: String,
    val title: String,
    val author: User
)

@Serializable
data class User(
    val id: Int = -1,
    val name: String
)
