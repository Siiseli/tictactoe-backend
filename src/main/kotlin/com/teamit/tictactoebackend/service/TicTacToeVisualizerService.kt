package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.mapper.StringPositionMapper
import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.springframework.stereotype.Service

@Service
class TicTacToeVisualizerService {
    fun visualize(game: TicTacToeGame): String {
        var result = ""

        result += "   A B C \n"
        for (row in 0 until game.board.size) {
            result += StringPositionMapper.mapStringFromPosition(row) + " |"
            for (col in 0 until game.board[row].size) {
                val character = game.board[row][col]
                result += "$character|"
            }
            result += "\n"
        }

        when {
            game.winner == game.playerCharacter -> result += "The humans are victorious!"
            game.winner == game.computerCharacter -> result += "The computer won"
            game.winner == TicTacToeCharacters.EMPTY -> result += "The game is ongoing, human's turn"
            game.winner == TicTacToeCharacters.DRAW -> result += "Draw!"
        }

        return result
    }
}