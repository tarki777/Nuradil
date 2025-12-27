package com.ktor.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.ktor.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

// JWT
const val JWT_SECRET = "your_strong_jwt_secret_key"
const val JWT_ISSUER = "ktor-student-api"
const val JWT_AUDIENCE = "student-users"
const val JWT_REALM = "Ktor Auth"

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = JWT_REALM
            verifier(
                JWT.require(Algorithm.HMAC256(JWT_SECRET))
                    .withAudience(JWT_AUDIENCE)
                    .withIssuer(JWT_ISSUER)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JWT_AUDIENCE)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Требуется токен авторизации"))
            }
        }
    }
}