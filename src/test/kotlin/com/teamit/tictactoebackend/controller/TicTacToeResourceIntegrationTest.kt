package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.*
import junit.framework.TestCase.*
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import javax.ws.rs.core.Response

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicTacToeResourceIntegrationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun shouldGetAllGames() {
        val result = testRestTemplate.getForEntity("/game/", TicTacToeGames::class.java)

        assertEquals(result.statusCode, HttpStatus.OK)
        assertNotNull(result.body?.games)
    }

    @Test
    fun shouldStartGame() {
        val startGameRequest = StartGameRequest("testName", "x")
        val result = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertTrue(result.body?.id?.isNotEmpty()!! && result.body?.id?.isNotBlank()!!)
    }

    @Test
    fun shouldGetGameWithId() {
        val startGameRequest = StartGameRequest("testName", "x")
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id
        val result = testRestTemplate.getForEntity(
                "/game/$gameId",
                TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(gameId, result.body?.id)
    }

    @Test
    fun shouldGetNotFoundWhenFetchingGameWithInvalidId() {
        val result = testRestTemplate.getForEntity(
                "/game/-1",
                Exception::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun shouldNotStartGameWithEmptyPost() {
        val result = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity.EMPTY, StartGameResponse::class.java)

        assertNotEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun shouldBeAbleToMakeMove() {
        val startGameRequest = StartGameRequest("testName", "x")
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), Response::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun shouldNotBeAbleToMakeMoveToInvalidGame() {
        val makeMoveRequest = MakeMoveRequest("a", "c")
        val result = testRestTemplate.exchange("/game/-1/move", HttpMethod.POST, HttpEntity(makeMoveRequest), Exception::class.java)

        assertEquals(HttpStatus.NOT_FOUND, result.statusCode)
    }

    @Test
    fun shouldNotBeAbleToMakeSameMoveTwice() {
        val startGameRequest = StartGameRequest("testName", "x")
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), ResponseEntity::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)

        val secondResult = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), Exception::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, secondResult.statusCode)
    }
}
