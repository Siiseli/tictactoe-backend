package com.teamit.tictactoebackend.model.error

import org.springframework.http.HttpStatus

data class ApiError(
    val status: HttpStatus,
    val statusCode: Int,
    val message: String?
)
