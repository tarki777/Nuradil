package com.ktor.plugins

import com.ktor.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException


fun Application.configureErrorHandling() {

    intercept(ApplicationCallPipeline.Monitoring) {
        try {
            proceed()
        } catch (e: SerializationException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Некорректный формат JSON или данных", e.message)
            )
        } catch (e: NumberFormatException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Некорректный числовой параметр", e.message)
            )
        } catch (e: Throwable) {
            call.application.environment.log.error("Необработанная ошибка:", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Внутренняя ошибка сервера")
            )
        }
    }
}
