package data.mappers

import data.database.models.UserEntity
import domain.User

object UserMapper {
    fun toDomain(entity: UserEntity): User{
       return User(entity.id, entity.name)
    }
    fun toEntity(domain: User): UserEntity{
        return UserEntity(domain.id, domain.name)
    }

}
