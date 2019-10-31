package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TicTacToeWinConditionCheckerTests {

    @Autowired
    private lateinit var winConditionChecker: TicTacToeWinConditionChecker

    @Test
    fun shouldInitialize() {
        Assertions.assertNotNull(winConditionChecker)
    }

    @Test
    fun shouldFindNoWinnersForEmptyBoard() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        val winner = winConditionChecker.findWinner(game)

        assertEquals(TicTacToeCharacters.EMPTY, winner)
    }

    @Test
    fun shouldFindNoWinnersForOccupiedBoardWithoutWinners() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = game.playerCharacter
        game.board[0][1] = game.computerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(TicTacToeCharacters.EMPTY, winner)
    }

    @Test
    fun shouldFindWinnerForHorizontalWin() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = game.playerCharacter
        game.board[0][1] = game.playerCharacter
        game.board[0][2] = game.playerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(game.playerCharacter, winner)
    }

    @Test
    fun shouldFindWinnerForVerticalWin() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = game.playerCharacter
        game.board[1][0] = game.playerCharacter
        game.board[2][0] = game.playerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(game.playerCharacter, winner)
    }

    @Test
    fun shouldFindWinnerForDiagonalLeftToRightWin() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = game.playerCharacter
        game.board[1][1] = game.playerCharacter
        game.board[2][2] = game.playerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(game.playerCharacter, winner)
    }

    @Test
    fun shouldFindWinnerForDiagonalRightToLeftWin() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][2] = game.playerCharacter
        game.board[1][1] = game.playerCharacter
        game.board[2][0] = game.playerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(game.playerCharacter, winner)
    }

    @Test
    fun shouldFindDrawForFullBoard() {
        val game = TicTacToeGame("testId", "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        game.board[0][0] = game.playerCharacter
        game.board[0][1] = game.computerCharacter
        game.board[0][2] = game.playerCharacter
        game.board[1][0] = game.computerCharacter
        game.board[1][1] = game.computerCharacter
        game.board[1][2] = game.playerCharacter
        game.board[2][0] = game.computerCharacter
        game.board[2][1] = game.playerCharacter
        game.board[2][2] = game.computerCharacter
        val winner = winConditionChecker.findWinner(game)

        assertEquals(TicTacToeCharacters.DRAW, winner)
    }

}
