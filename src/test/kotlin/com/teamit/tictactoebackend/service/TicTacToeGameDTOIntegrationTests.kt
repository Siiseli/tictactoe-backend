package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeCharacters
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class TicTacToeGameDTOIntegrationTests {

    @Autowired
    private lateinit var gameDTO: TicTacToeGameDTO

    // Used for cleaning up created games after tests are done
    private val createdGames: ArrayList<String> = ArrayList()

    @Test
    fun shouldInitialize() {
        assertNotNull(gameDTO)
    }

    @Test
    fun shouldSaveGame() {
        val game = TicTacToeGame(UUID.randomUUID().toString(), "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        gameDTO.saveGame(game)
        createdGames.add(game.id)
    }

    @Test
    fun shouldLoadGame() {
        val id = UUID.randomUUID().toString()
        val game = TicTacToeGame(id, "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        gameDTO.saveGame(game)
        createdGames.add(game.id)

        val loadedGame = gameDTO.getGame(id)

        assertNotNull(loadedGame)
        if (loadedGame != null) {
            assertEquals(game.id, loadedGame.id)
            assertEquals(game.name, loadedGame.name)
        }
    }

    @Test
    fun shouldLoadGames() {
        val game = TicTacToeGame(UUID.randomUUID().toString(), "testName", TicTacToeCharacters.X, TicTacToeCharacters.O)
        gameDTO.saveGame(game)
        createdGames.add(game.id)

        val games = gameDTO.getGames()
        assertTrue(games.games.size > 0)
    }

    @AfterAll
    fun cleanup() {
        gameDTO.deleteGames(createdGames)
    }
}
