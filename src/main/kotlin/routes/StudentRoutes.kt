package com.ktor.routes

import com.ktor.models.ErrorResponse
import com.ktor.models.MessageResponse
import com.ktor.models.Student
import com.ktor.services.StudentService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.studentRouting(studentService: StudentService) {
    
    authenticate("auth-jwt") {
        route("/students") {
            
            // GET
            get {
                val minAge = call.request.queryParameters["minAge"]?.toIntOrNull()
                
                val studentsList = studentService.getAllStudents(minAge)
                call.respond(HttpStatusCode.OK, studentsList)
            }

            // POST
            post {
                val newStudent = call.receive<Student>()
                
                if (newStudent.name.isBlank() || newStudent.age <= 0) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Имя и возраст должны быть указаны корректно"))
                    return@post
                }

                val createdStudent = studentService.createStudent(newStudent)
                call.respond(HttpStatusCode.Created, createdStudent)
            }

            // GET 
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()

                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Некорректный ID студента"))
                    return@get
                }

                val student = studentService.getStudentById(id)

                if (student != null) {
                    call.respond(HttpStatusCode.OK, student)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Студент с ID $id не найден"))
                }
            }
            
            // DELETE
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Некорректный ID студента"))
                    return@delete
                } 
                
                if (studentService.deleteStudent(id)) {
                    call.respond(HttpStatusCode.OK, MessageResponse("Студент $id удалён"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("Студент $id не найден"))
                }
            }
        }
    }
}