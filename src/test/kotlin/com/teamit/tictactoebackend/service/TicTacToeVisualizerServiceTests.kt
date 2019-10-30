package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TicTacToeVisualizerServiceTests {

    @Autowired
    private lateinit var visualizer: TicTacToeVisualizerService

    @Test
    fun visualizesHeader() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        val result = visualizer.visualize(game)
        assertTrue(result.startsWith("   A B C \n"))
    }

    @Test
    fun visualizesBoardRow() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = TicTacToeCharacters.X
        game.board[0][1] = TicTacToeCharacters.X
        game.board[0][2] = TicTacToeCharacters.O
        val result = visualizer.visualize(game)
        assertTrue(result.contains("|x|x|o|\n"))
    }

    @Test
    fun visualizesWinner() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.winner = game.computerCharacter
        val result = visualizer.visualize(game)
        assertTrue(result.endsWith("The computer won"))
    }

    @Test
    fun visualizesDraw() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.winner = TicTacToeCharacters.DRAW
        val result = visualizer.visualize(game)
        assertTrue(result.endsWith("Draw!"))
    }

    @Test
    fun visualizesOngoingGame() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.winner = TicTacToeCharacters.EMPTY
        val result = visualizer.visualize(game)
        assertTrue(result.contains("The game is ongoing"))
    }

}
