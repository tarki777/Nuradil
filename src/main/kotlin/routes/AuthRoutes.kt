package com.ktor.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ktor.models.*
import com.ktor.plugins.*
import com.ktor.services.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.authRouting(userService: UserService) {
    val algorithm = Algorithm.HMAC256(JWT_SECRET)

    route("/auth") {

        post("/register") {
            val userCredentials = try {
                call.receive<UserCredentials>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Некорректный формат данных"))
                return@post
            }

            if (userService.findUser(userCredentials.username) != null) {
                call.respond(HttpStatusCode.Conflict, ErrorResponse("Пользователь с таким именем уже существует"))
                return@post
            }

            val passwordHash = userService.hashPassword(userCredentials.password)
            val success = userService.registerUser(userCredentials.username, passwordHash, role = "user")

            if (success) {
                call.respond(HttpStatusCode.Created, MessageResponse("Пользователь ${userCredentials.username} успешно зарегистрирован"))
            } else {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Не удалось зарегистрировать пользователя"))
            }
        }

        post("/login") {
            val userCredentials = try {
                call.receive<UserCredentials>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Некорректный формат данных"))
                return@post
            }

            val userRow = userService.findUser(userCredentials.username)
            if (userRow == null) {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверное имя пользователя или пароль"))
                return@post
            }

            val storedHash = userRow[Users.passwordHash]
            val role = userRow[Users.role]

            if (userService.verifyPassword(userCredentials.password, storedHash)) {
                val token = JWT.create()
                    .withAudience(JWT_AUDIENCE)
                    .withIssuer(JWT_ISSUER)
                    .withSubject(userCredentials.username)
                    .withClaim("role", role) // <-- добавляем роль
                    .withExpiresAt(Date(System.currentTimeMillis() + 60 * 60 * 1000))
                    .sign(algorithm)

                call.respond(HttpStatusCode.OK, TokenResponse(token))
            } else {
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Неверное имя пользователя или пароль"))
            }
        }
    }
}
