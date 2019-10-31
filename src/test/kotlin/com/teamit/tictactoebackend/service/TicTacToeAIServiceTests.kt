package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TicTacToeAIServiceTests {

    @Autowired
    private lateinit var ai: TicTacToeAIService

    @Test
    fun shouldInitialize() {
        Assertions.assertNotNull(ai)
    }

    @Test
    fun shouldMakeMove() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        val updatedBoard = ai.makeMove(game)
        assertNotEquals(game.board, updatedBoard)
    }
}