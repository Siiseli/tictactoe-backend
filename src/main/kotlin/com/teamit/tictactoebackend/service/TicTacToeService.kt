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

    fun getGames(): TicTacToeGames {
        return gameDTO.getGames()
    }

    fun getGame(id: String): TicTacToeGame {
        return gameDTO.getGame(id) ?: throw GameNotFoundException("Could not find game")
    }

    fun startGame(name: String, character: String) : TicTacToeGame {
        val game = TicTacToeGame(UUID.randomUUID().toString(), name, character.toCharArray()[0])

        gameDTO.saveGame(game)

        return game
    }

    fun makeMove(id: String, col: String, row: String) {
        val game = getGame(id)
        val colPos = mapPositionFromString(col)
        val rowPos = mapPositionFromString(row)
        val character = game.board[rowPos][colPos]
        if (character == ' ') {
            game.board[rowPos][colPos] = game.playerCharacter
        } else {
            throw IllegalMoveException("Can not make move at $col, $row: already occupied")
        }

        gameDTO.saveGame(game)
    }

    fun mapPositionFromString(pos: String) : Int {
        return when(pos.toLowerCase()) {
            "a" -> 0
            "b" -> 1
            "c" -> 2
            else -> -1
        }
    }
}