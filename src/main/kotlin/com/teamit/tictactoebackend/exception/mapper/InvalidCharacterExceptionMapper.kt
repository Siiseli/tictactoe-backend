package com.teamit.tictactoebackend.exception.mapper

import com.teamit.tictactoebackend.exception.InvalidCharacterException
import com.teamit.tictactoebackend.model.error.ApiError
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
@Component
class InvalidCharacterExceptionMapper: ExceptionMapper<InvalidCharacterException> {
    override fun toResponse(ex: InvalidCharacterException): Response {
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(ApiError(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), ex.message)).build()
    }
}
