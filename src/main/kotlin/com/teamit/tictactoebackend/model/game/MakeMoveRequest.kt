package com.teamit.tictactoebackend.model.game

data class MakeMoveRequest(
    val col: String,
    val row: String
)