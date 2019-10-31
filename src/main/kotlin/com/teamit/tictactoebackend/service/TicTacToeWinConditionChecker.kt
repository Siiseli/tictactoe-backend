package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.springframework.stereotype.Service

@Service
class TicTacToeWinConditionChecker {

    fun findWinner(game: TicTacToeGame): Char {
        var emptyFound = false

        // Check horizontal
        for (row in 0 until game.board.size) {
            var score = 0
            for (col in 0 until game.board[row].size) {
                val character = game.board[row][col]
                if (!emptyFound && character == TicTacToeCharacters.EMPTY) {
                    emptyFound = true
                }
                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if (winner != TicTacToeCharacters.EMPTY) return winner
            }
        }

        // Check vertical
        for (col in 0 until game.board[0].size) {
            var score: Int = 0
            for (row in 0 until game.board.size) {
                val character = game.board[row][col]

                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if (winner != TicTacToeCharacters.EMPTY) return winner
            }
        }

        // Check diagonals
        var score: Int = 0
        for (i in 0 until game.board.size) {
            val character = game.board[i][i]
            score += getCharacterScore(character, game)
        }
        var winner: Char = getWinnerFromScore(score, game)
        if (winner != TicTacToeCharacters.EMPTY) return winner

        score = 0
        for (i in 0 until game.board.size) {
            val character = game.board[i][game.board.size - 1 - i]
            score += getCharacterScore(character, game)
        }
        winner = getWinnerFromScore(score, game)

        if (winner == TicTacToeCharacters.EMPTY && !emptyFound) {
            winner = TicTacToeCharacters.DRAW
        }

        return winner
    }

    private fun getCharacterScore(character: Char, game: TicTacToeGame): Int {
        return when (character) {
            game.computerCharacter -> -1
            game.playerCharacter -> 1
            else -> 0
        }
    }

    private fun getWinnerFromScore(score: Int, game: TicTacToeGame): Char {
        if(score <= -game.board.size) {
            return game.computerCharacter
        } else if(score >= game.board.size) {
            return game.playerCharacter
        }
        return TicTacToeCharacters.EMPTY
    }
}