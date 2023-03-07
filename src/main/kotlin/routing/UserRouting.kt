package routing

import com.sun.net.httpserver.HttpServer
import core.request
import domain.IRepository
import domain.User

fun HttpServer.userRouting(repository: IRepository) {
    request("/users", "GET") { call ->
        val id = call.parameters["id"]?.toInt() ?: -1
        if (id == -1) {
            val users = repository.allUsers()
            call.respond(200, users)
        } else {
            val user = repository.getUserById(id)
            call.respond(200, user)
        }

    }

    request("/users/create", "POST") { call ->
        val user = call.receive<User>()
        if (user != null) {
            val result = repository.createUser(user)
            result.fold(
                {
                    call.respond(200, "OK")
                },
                { exception ->
                    call.respond(400, mapOf("error" to exception.message))
                }
            )
        } else {
            call.respond(400, "Article is not right format!")
        }
    }

}
