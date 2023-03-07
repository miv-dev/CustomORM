package routing

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import core.request
import domain.Article
import domain.IRepository
import domain.User
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.nio.charset.StandardCharsets

fun HttpServer.articleRouting(repository: IRepository) {

    request("/articles", "GET") {
        print("request")
        val articles = repository.allArticles()
        it.respond(200, articles)
    }
    request("/articles/create", "POST") { call ->
        val article = call.receive<Article>()
        if (article != null) {
            val result = repository.createArticle(article)
            result.fold({
                call.respond(200, "OK")

            }, { exception ->
                call.respond(400, mapOf("error" to exception.message))
            })
        } else {

            call.respond(400, "Article is not right format!")
        }
    }
}




