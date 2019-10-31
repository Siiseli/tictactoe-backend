package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.exception.IllegalMoveException
import com.teamit.tictactoebackend.exception.InvalidCharacterException
import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TicTacToeServiceTests {

    @Mock
    private lateinit var gameDTO: TicTacToeGameDTO

    @Mock
    private lateinit var computerPlayer: TicTacToeAIService

    @Mock
    private lateinit var winConditionChecker: TicTacToeWinConditionChecker

    @InjectMocks
    private lateinit var ticTacToe: TicTacToeService

    @Test
    fun shouldInitialize() {
        assertNotNull(ticTacToe)
    }

    @Test
    fun shouldGetGames() {
        val id = "testId"
        `when`(gameDTO.getGames()).thenReturn(TicTacToeGames(arrayListOf(TicTacToeGame(id, "testName", TicTacToeCharacters.X, TicTacToeCharacters.O))))
        val games = ticTacToe.getGames()
        assertTrue(games.games.size == 1)
        assertEquals(id, games.games[0].id)
    }

    @Test
    fun shouldGetGameWithId() {
        val id = "testId"
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", TicTacToeCharacters.X, TicTacToeCharacters.O))
        val game = ticTacToe.getGame(id)
        assertEquals(id, game.id)
    }

    @Test
    fun shouldThrowGameNotFoundExceptionWhenGameNotFound() {
        val id = "invalidId"
        `when`(gameDTO.getGame(id)).thenReturn(null)
        assertThrows(GameNotFoundException::class.java) {
            ticTacToe.getGame(id)
        }
    }

    @Test
    fun shouldSetNameWhenStartingGame() {
        val name = "name"
        val game = ticTacToe.startGame(name, TicTacToeCharacters.X)
        assertEquals(name, game.name)
    }

    @Test
    fun shouldSetIdWhenStartingGame() {
        val game = ticTacToe.startGame("testName", TicTacToeCharacters.X)
        assertTrue(game.id.isNotEmpty())
    }

    @Test
    fun shouldSetCharactersOnStartGame() {
        val playerCharacter = TicTacToeCharacters.X
        val game = ticTacToe.startGame("testName", playerCharacter)
        assertEquals(playerCharacter, game.playerCharacter)
        assertEquals(TicTacToeCharacters.O, game.computerCharacter)
    }

    @Test
    fun shouldThrowInvalidCharacterExceptionWhenStartingGameWithInvalidCharacter() {
        val id = "invalidId"
        `when`(gameDTO.getGame(id)).thenReturn(null)
        assertThrows(InvalidCharacterException::class.java) {
            ticTacToe.startGame("testName", 'y')
        }
    }

    @Test
    fun shouldMakePlayerMove() {
        val id = "testId"
        val playerCharacter = TicTacToeCharacters.X
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", playerCharacter, TicTacToeCharacters.O))
        `when`(winConditionChecker.findWinner(any(TicTacToeGame::class.java))).thenReturn(TicTacToeCharacters.EMPTY)
        `when`(computerPlayer.makeMove(any(TicTacToeGame::class.java))).thenCallRealMethod()
        val game = ticTacToe.makeMove(id, "A", "A")
        assertEquals(playerCharacter, game.board[0][0])
    }

    @Test
    fun shouldMakeComputerMove() {
        val id = "testId"
        val playerCharacter = TicTacToeCharacters.X
        val computerCharacter = TicTacToeCharacters.O
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", playerCharacter, computerCharacter))
        `when`(winConditionChecker.findWinner(any(TicTacToeGame::class.java))).thenReturn(TicTacToeCharacters.EMPTY)
        `when`(computerPlayer.makeMove(any(TicTacToeGame::class.java))).thenReturn(
                Array(3) {
                    charArrayOf(playerCharacter, computerCharacter, TicTacToeCharacters.EMPTY)
                }
        )
        val game = ticTacToe.makeMove(id, "A", "A")
        assertEquals(computerCharacter, game.board[0][1])
    }

    @Test
    fun shouldDetermineWinner() {
        val id = "testId"
        val playerCharacter = TicTacToeCharacters.X
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", playerCharacter, TicTacToeCharacters.O))
        `when`(winConditionChecker.findWinner(any(TicTacToeGame::class.java))).thenReturn(playerCharacter)
        val game = ticTacToe.makeMove(id, "A", "A")
        assertEquals(playerCharacter, game.winner)
    }

    @Test
    fun shouldThrowInvalidMoveExceptionWhenAttemptingToMakeMoveOnOccupiedSpace() {
        val id = "testId"
        val playerCharacter = TicTacToeCharacters.X
        val computerCharacter = TicTacToeCharacters.O
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", playerCharacter, computerCharacter, TicTacToeCharacters.EMPTY,
                Array(3) {
                    charArrayOf(playerCharacter, computerCharacter, playerCharacter)
                })
        )

        assertThrows(IllegalMoveException::class.java) {
            ticTacToe.makeMove(id, "A", "A")
        }
    }

    @Test
    fun shouldThrowInvalidMoveExceptionWhenAttemptingToMakeMoveOutsideBoard() {
        val id = "testId"
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", TicTacToeCharacters.X, TicTacToeCharacters.O))

        assertThrows(IllegalMoveException::class.java) {
            ticTacToe.makeMove(id, "D", "D")
        }
    }

    @Test
    fun shouldThrowInvalidMoveExceptionWhenAttemptingToMakeMoveOnGameThatHasEnded() {
        val id = "testId"
        val playerCharacter = TicTacToeCharacters.X
        `when`(gameDTO.getGame(id)).thenReturn(TicTacToeGame(id, "testName", playerCharacter, TicTacToeCharacters.O, playerCharacter))

        assertThrows(IllegalMoveException::class.java) {
            ticTacToe.makeMove(id, "A", "A")
        }
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}