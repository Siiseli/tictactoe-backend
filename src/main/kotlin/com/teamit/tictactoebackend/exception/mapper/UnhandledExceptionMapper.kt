package com.teamit.tictactoebackend.exception.mapper

import com.teamit.tictactoebackend.model.error.ApiError
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
@Component
class UnhandledExceptionMapper: ExceptionMapper<Throwable> {
    override fun toResponse(ex: Throwable): Response {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(ApiError(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.message)).build()
    }
}
