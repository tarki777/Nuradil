package com.ktor.plugins

import com.ktor.models.Students
import com.ktor.models.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert

fun Application.configureDatabase() {

    val dbUrl = "jdbc:postgresql://localhost:5433/ktor_db" 
    val dbUser = "civ"
    val dbPassword = "123"

    Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPassword)

    transaction {
        SchemaUtils.create(Students, Users)
        

        if (Students.selectAll().count() == 0L) {
            Students.insert { 
                it[Students.name] = "Иван"
                it[Students.age] = 20
            }
            Students.insert { 
                it[Students.name] = "Мария"
                it[Students.age] = 22
            }
        }
    }
}