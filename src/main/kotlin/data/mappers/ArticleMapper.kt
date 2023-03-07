package data.mappers

import data.database.models.ArticleEntity
import data.database.models.UserEntity
import domain.Article
import domain.User

object ArticleMapper {
    fun toDomain(entity: ArticleEntity, userEntity: UserEntity): Article{
        val user = UserMapper.toDomain(userEntity)
       return Article(entity.id, entity.text, entity.title, user)
    }
    fun toEntity(domain: Article): ArticleEntity{
        return ArticleEntity(domain.id, domain.text, domain.title, domain.author.id)
    }

}
