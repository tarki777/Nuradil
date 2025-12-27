package com.ktor.services

import com.ktor.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq 
import org.jetbrains.exposed.sql.transactions.transaction
import org.mindrot.jbcrypt.BCrypt

class UserService {

    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

    fun verifyPassword(password: String, hash: String): Boolean = BCrypt.checkpw(password, hash)

    fun findUser(username: String): ResultRow? = transaction {
        Users.select { Users.username eq username }.singleOrNull()
    }

    fun registerUser(username: String, passwordHash: String): Boolean = transaction {
        try {

            Users.insert {
                it[Users.username] = username
                it[Users.passwordHash] = passwordHash
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}