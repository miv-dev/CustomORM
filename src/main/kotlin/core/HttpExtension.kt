package core

import com.sun.net.httpserver.HttpServer
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


fun HttpServer.request(path: String, method: String, body: (Call) -> Unit) {


    this.createContext(path) { exchange ->
        exchange?.let {
            if (it.requestMethod != method) throw Exception("")
            val call = Call(it, path)
            print(call.parameters)
            body(call)
        }
    }

}

