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

    @Autowired
    private lateinit var winConditionChecker: TicTacToeWinConditionChecker

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
            if(game.winner == TicTacToeCharacters.DRAW) {
                throw IllegalMoveException("Game has already ended, it was a draw")
            }

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

        var winner: Char = winConditionChecker.findWinner(game)
        if(winner != TicTacToeCharacters.EMPTY) {
            game.winner = winner
            gameDTO.saveGame(game)
            return game
        }

        game.board = computerPlayer.makeMove(game)
        winner = winConditionChecker.findWinner(game)
        if(winner != TicTacToeCharacters.EMPTY) {
            game.winner = winner
        }
        gameDTO.saveGame(game)
        return game
    }
}