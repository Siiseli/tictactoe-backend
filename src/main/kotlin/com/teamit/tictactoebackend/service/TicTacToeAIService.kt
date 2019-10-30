package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.springframework.stereotype.Service

@Service
class TicTacToeAIService {
    fun makeMove(game: TicTacToeGame) : Array<CharArray> {
        val result = game.board.copyOf()
        for (row in 0 until game.board.size) {
            for (col in 0 until game.board[row].size) {
                if (game.board[row][col] == ' ') {
                    result[row][col] = game.computerCharacter
                    return result
                }
            }
        }
        return result
    }
}