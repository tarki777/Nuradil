package com.ktor.routes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

val sessions = mutableMapOf<String, DefaultWebSocketServerSession>()

fun Route.chatRouting() {
    authenticate("auth-jwt") {
        webSocket("/chat/ws") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal?.getClaim("username", String::class) ?: "Unknown"

            sessions[username] = this

            try {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        sessions.forEach { (_, session) ->
                            session.send("""{"user":"$username","message":"$text"}""")
                        }
                    }
                }
            } finally {
                sessions.remove(username)
            }
        }
    }
}
