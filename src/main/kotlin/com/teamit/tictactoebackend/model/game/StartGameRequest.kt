package com.teamit.tictactoebackend.model.game

data class StartGameRequest(
    val name: String,
    val character: Char
)