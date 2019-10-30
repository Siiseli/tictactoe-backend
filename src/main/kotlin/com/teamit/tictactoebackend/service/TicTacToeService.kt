package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.exception.IllegalMoveException
import com.teamit.tictactoebackend.exception.InvalidCharacterException
import com.teamit.tictactoebackend.mapper.StringPositionMapper
import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
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
        val playerCharacterNormalized = playerCharacter.toLowerCase()
        if (!TicTacToeCharacters.VALID_CHARACTERS.contains(playerCharacterNormalized)) {
            throw InvalidCharacterException("Invalid character, please select either ${TicTacToeCharacters.X} or ${TicTacToeCharacters.O}")
        }

        val game = TicTacToeGame(
                UUID.randomUUID().toString(),
                name,
                playerCharacterNormalized,
                if(playerCharacterNormalized == TicTacToeCharacters.X) TicTacToeCharacters.O else TicTacToeCharacters.X
        )

        gameDTO.saveGame(game)

        return game
    }

    fun makeMove(id: String, col: String, row: String) : TicTacToeGame {
        val game = getGame(id)

        if (game.winner != TicTacToeCharacters.EMPTY) {
            throw IllegalMoveException("Game has already ended, the winner is ${game.winner}")
        }

        val colPos = StringPositionMapper.mapPositionFromString(col)
        val rowPos = StringPositionMapper.mapPositionFromString(row)

        val character = game.board[rowPos][colPos]
        if (character == TicTacToeCharacters.EMPTY) {
            game.board[rowPos][colPos] = game.playerCharacter
        } else {
            throw IllegalMoveException("Can not make move at $col, $row: already occupied")
        }

        var winner: Char = checkWinCondition(game)
        if(winner != TicTacToeCharacters.EMPTY) {
            game.winner = winner
            gameDTO.saveGame(game)
            return game
        }

        game.board = computerPlayer.makeMove(game)
        winner = checkWinCondition(game)
        if(winner != TicTacToeCharacters.EMPTY) {
            game.winner = winner
        }
        gameDTO.saveGame(game)
        return game
    }

    private fun checkWinCondition(game: TicTacToeGame): Char {
        // Check horizontal
        for (row in 0 until game.board.size) {
            var score: Int = 0
            for (col in 0 until game.board[row].size) {
                val character = game.board[row][col]
                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if(winner !== TicTacToeCharacters.EMPTY) return winner
            }
        }

        // Check vertical
        for (col in 0 until game.board[0].size) {
            var score: Int = 0
            for (row in 0 until game.board.size) {
                val character = game.board[row][col]
                score += getCharacterScore(character, game)

                val winner: Char = getWinnerFromScore(score, game)
                if(winner !== TicTacToeCharacters.EMPTY) return winner
            }
        }

        // Check diagonals
        var score: Int = 0
        for (i in 0 until game.board.size) {
            val character = game.board[i][i]
            score += getCharacterScore(character, game)
        }
        var winner: Char = getWinnerFromScore(score, game)
        if(winner !== TicTacToeCharacters.EMPTY) return winner

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
        return TicTacToeCharacters.EMPTY
    }
}