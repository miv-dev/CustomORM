package core

import com.sun.net.httpserver.HttpExchange
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

//fun main() {
//
//    val uri = "/users/2/create/1/2"
//
//    val path = "/users/{user}/create/{id}/{param}"
//
//    val parameters = mutableMapOf<String, String>()
//    val reg = "\\{([a-zA-Z]+)}+".toRegex()
//    val pathSplit = path.split("/")
//    val uriSplit = uri.split("/")
//    reg.findAll(path).forEach { param ->
//        val index = pathSplit.indexOf(param.value)
//        parameters[param.groupValues[1]] = uriSplit[index]
//    }
//    print(parameters)
//}

class Call(val httpExchange: HttpExchange, path: String) {


    inline fun <reified T> receive(): T? {
        val headers = mutableMapOf<String, String>()


        // Print out all the header names and values
        for (entry in httpExchange.requestHeaders.entries) {
            val name = entry.key.lowercase()
            val value = entry.value
            if (value.size == 1) {
                headers[name] = value[0].toString()
                println("$name: $value")
            }


        }


        val stream = httpExchange.requestBody
        return if (headers.containsKey("content-length")) {


            val length = headers.getValue("content-length").toInt()
            val buffer = ByteArray(length)
            stream.read(buffer, 0, length)
            val json = Json { ignoreUnknownKeys = true }
            val value = json.decodeFromString<T>(buffer.toString(StandardCharsets.UTF_8))
            value
        } else {
            null
        }
    }

    val parameters: Map<String, String> by lazy {
        val uri = httpExchange.requestURI.path
        val result = queryToMap(uri)
        return@lazy result ?: mapOf()
    }


    fun queryToMap(query: String?): Map<String, String>? {
        if (query == null) {
            return null
        }
        val result: MutableMap<String, String> = mutableMapOf()
        for (param in query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val entry = param.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (entry.size > 1) {
                result[entry[0]] = entry[1]
            } else {
                result[entry[0]] = ""
            }
        }
        return result
    }

    inline fun <reified T> respond(statusCode: Int, message: T) {

        val encodedMessage = Json.encodeToString(message)

        httpExchange.sendResponseHeaders(statusCode, encodedMessage.length.toLong())
        httpExchange.responseHeaders
        val os = httpExchange.responseBody
        os.write(encodedMessage.toByteArray())
        os.close()
    }


}

