package com.teamit.tictactoebackend.model.game

data class TicTacToeGame (
        val id: String,
        val name: String,
        val playerCharacter: Char,
        val computerCharacter: Char,
        var winner: Char = TicTacToeCharacters.EMPTY,
        var board: Array<CharArray> = Array(3) {
        charArrayOf(TicTacToeCharacters.EMPTY, TicTacToeCharacters.EMPTY, TicTacToeCharacters.EMPTY)
    }
)