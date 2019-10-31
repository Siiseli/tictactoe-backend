package com.teamit.tictactoebackend.exception.mapper

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.model.error.ApiError
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
@Component
class GameNotFoundExceptionMapper: ExceptionMapper<GameNotFoundException> {
    override fun toResponse(ex: GameNotFoundException): Response {
        return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(ApiError(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), ex.message)).build()
    }
}
