import com.sun.net.httpserver.HttpServer
import data.repositories.RepositoryImpl
import routing.articleRouting
import routing.userRouting
import java.net.InetSocketAddress

fun main() {
    val repository = RepositoryImpl()

    val server = HttpServer.create(InetSocketAddress(8000), 0)
    server.articleRouting(repository)
    server.userRouting(repository)

    server.executor = null // creates a default executor
    server.start()

}
