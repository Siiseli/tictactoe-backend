package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class TicTacToeGameDTOIntegrationTests {

    @Autowired
    private lateinit var gameDTO: TicTacToeGameDTO

    @Test
    fun shouldInitialize() {
        assertNotNull(gameDTO)
    }

    @Test
    fun shouldSaveGame() {
        val game = TicTacToeGame(UUID.randomUUID().toString(), "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        gameDTO.saveGame(game)
    }

    @Test
    fun shouldLoadGame() {
        val id = UUID.randomUUID().toString()
        val game = TicTacToeGame(id, "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        gameDTO.saveGame(game)
        val loadedGame = gameDTO.getGame(id)

        assertNotNull(loadedGame)
        if (loadedGame != null) {
            assertEquals(game.id, loadedGame.id)
            assertEquals(game.name, loadedGame.name)
        }
    }

    @Test
    fun shouldLoadGames() {
        gameDTO.saveGame(TicTacToeGame(UUID.randomUUID().toString(), "testName", TicTacToeCharacters.X, TicTacToeCharacters.O))
        val games = gameDTO.getGames()
        assertTrue(games.games.size > 0)
    }
}
