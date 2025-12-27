package com.ktor.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table



@Serializable
data class Student(val id: Int = 0, val name: String, val age: Int)

@Serializable
data class UserCredentials(val username: String, val password: String)

@Serializable
data class TokenResponse(val token: String)

@Serializable
data class ErrorResponse(val error: String, val details: String? = null)

@Serializable
data class MessageResponse(val message: String)


object Students : IntIdTable("students") {
    val name = varchar("name", 255)
    val age = integer("age")
}


object Users : IntIdTable("users") {
    val username = varchar("username", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255) 

}