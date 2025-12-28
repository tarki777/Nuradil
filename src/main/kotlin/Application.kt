package com.ktor

import com.ktor.plugins.*
import com.ktor.routes.*
import com.ktor.services.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

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


    routing {
        authRouting(userService)
        studentRouting(studentService)
    }
}