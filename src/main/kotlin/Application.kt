package com.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import kotlinx.serialization.Serializable

@Serializable
data class Student(val id: Int, val name: String, val age: Int)

@Serializable
data class ErrorResponse(val error: String, val details: String? = null)

@Serializable
data class MessageResponse(val message: String)


val students = mutableListOf(
    Student(1, "Иван", 20),
    Student(2, "Мария", 22)
)

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    
    // плагин для автоматического преобразования объектов в JSON
    install(ContentNegotiation) {
        json()
    }

    // Единая обработка ошибок, чтобы вместо 500 возвращать понятные ответы
    install(StatusPages) {
        exception<kotlinx.serialization.SerializationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Некорректный формат JSON или данных", cause.message)
            )
        }
        exception<NumberFormatException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Некорректный числовой параметр", cause.message)
            )
        }
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Внутренняя ошибка сервера")
            )
            throw cause
        }
    }

    routing {
        
        // получить список студентов (с возможностью фильтрации по query)
        get("/students") {
            val minAge = call.request.queryParameters["minAge"]?.toIntOrNull()
            
            val result: List<Student> = if (minAge != null) {
                students.filter { it.age >= minAge }.toList()
            } else {
                students.toList() 
            }
            call.respond(HttpStatusCode.OK, result)
        }

        // создать нового студента
        post("/students") {
            val newStudent = call.receive<Student>()
            students.add(newStudent)
            call.respond(HttpStatusCode.Created, newStudent)
        }

        // удалить студента по ID
        delete("/students/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Некорректный id"))
            } else {
                val removed = students.removeIf { it.id == id }
                if (removed) {
                    call.respond(HttpStatusCode.OK, MessageResponse("Студент $id удалён"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Студент $id не найден"))
                }
            }
        }
    }
}