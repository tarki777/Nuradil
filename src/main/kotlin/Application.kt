package com.ktor

import com.ktor.plugins.*
import com.ktor.routes.*
import com.ktor.services.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.websocket.*
import java.time.Duration

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val studentService = StudentService()
    val userService = UserService()

    configureDatabase()
    configureSerialization()
    configureSecurity()
    configureErrorHandling()
    configureSwagger()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        // Статика фронта
        static("/") {
            resources("static")
            defaultResource("index.html", "static")
        }

        // Маршруты
        authRouting(userService)
        studentRouting(studentService)
        chatRouting()
    }
}
