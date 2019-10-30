package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.exception.IllegalMoveException
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicTacToeService {

    @Autowired
    private lateinit var gameDTO: TicTacToeGameDTO

    @Autowired
    private lateinit var computerPlayer: TicTacToeAIService

    fun getGames(): TicTacToeGames {
        return gameDTO.getGames()
    }

    fun getGame(id: String): TicTacToeGame {
        return gameDTO.getGame(id) ?: throw GameNotFoundException("Could not find game")
    }

    fun startGame(name: String, playerCharacter: Char) : TicTacToeGame {
        val game = TicTacToeGame(UUID.randomUUID().toString(), name, playerCharacter, if(playerCharacter == 'x') 'o' else 'x')

        gameDTO.saveGame(game)

        return game
    }

    fun makeMove(id: String, col: String, row: String) {
        val game = getGame(id)

        if (game.winner != ' ') {
            throw IllegalMoveException("Game has already ended, the winner is ${game.winner}")
        }

        val colPos = mapPositionFromString(col)
        val rowPos = mapPositionFromString(row)

        val character = game.board[rowPos][colPos]
        if (character == ' ') {
            game.board[rowPos][colPos] = game.playerCharacter
        } else {
            throw IllegalMoveException("Can not make move at $col, $row: already occupied")
        }

        val winner: Char = checkWinCondition(game)
        if(winner != ' ') {
            game.winner = winner
            gameDTO.saveGame(game)
            return
        }

        game.board = computerPlayer.makeMove(game)
        checkWinCondition(game)
        if(winner != ' ') {
            game.winner = winner
        }
        gameDTO.saveGame(game)
    }

    private fun checkWinCondition(game: TicTacToeGame): Char {
        // Check horizontal
        for (row in 0 until game.board.size) {
            var score: Int = 0
            for (col in 0 until game.board[row].size) {
                val character = game.board[row][col]
                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if(winner !== ' ') return winner
            }
        }

        // Check vertical
        for (col in 0 until game.board[0].size) {
            var score: Int = 0
            for (row in 0 until game.board.size) {
                val character = game.board[row][col]
                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if(winner !== ' ') return winner
            }
        }

        // Check diagonals
        var score: Int = 0
        for (i in 0 until game.board.size) {
            val character = game.board[i][i]
            score += getCharacterScore(character, game)
        }
        var winner: Char = getWinnerFromScore(score, game)
        if(winner !== ' ') return winner

        score = 0
        for (i in 0 until game.board.size) {
            val character = game.board[i][game.board.size - 1 - i]
            score += getCharacterScore(character, game)
        }
        winner = getWinnerFromScore(score, game)

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
        return ' '
    }

    private fun mapPositionFromString(pos: String) : Int {
        return when(pos.toLowerCase()) {
            "a" -> 0
            "b" -> 1
            "c" -> 2
            else -> throw IllegalMoveException("Can not make move outside of game board")
        }
    }
}