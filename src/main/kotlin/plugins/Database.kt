package com.ktor.plugins

import com.ktor.models.Students
import com.ktor.models.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val dbUrl = "jdbc:postgresql://localhost:5433/ktor_db"
    val dbUser = "civ"
    val dbPassword = "123"

    Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPassword)

    transaction {
        SchemaUtils.create(Students, Users)

        // Пример начальных данных
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

        // Создаём admin для теста
        if (Users.selectAll().count() == 0L) {
            Users.insert {
                it[Users.username] = "admin"
                it[Users.passwordHash] = "\$2a\$10\$Lz6" // просто пример, хэш реальный
                it[Users.role] = "admin"
            }
        }
    }
}
