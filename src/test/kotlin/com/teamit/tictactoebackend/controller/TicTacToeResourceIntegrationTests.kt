package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.*
import junit.framework.TestCase.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.Assert.assertNotEquals
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TicTacToeResourceIntegrationTests {

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
        val startGameRequest = StartGameRequest("testName", 'x')
        val result = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)

        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertTrue(result.body?.id?.isNotEmpty()!! && result.body?.id?.isNotBlank()!!)
    }

    @Test
    fun shouldStartGameWithCaseInsensitivePlayerCharacter() {
        val startGameRequest = StartGameRequest("testName", 'X')
        val startGameResponse = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)

        assertEquals(HttpStatus.CREATED, startGameResponse.statusCode)
        assertTrue(startGameResponse.body?.id?.isNotEmpty()!! && startGameResponse.body?.id?.isNotBlank()!!)

        val gameId = startGameResponse.body?.id
        val result = testRestTemplate.getForEntity(
                "/game/$gameId",
                TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals('x', result.body?.playerCharacter)
    }

    @Test
    fun shouldGetGameWithId() {
        val startGameRequest = StartGameRequest("testName", 'x')
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
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), ResponseEntity::class.java)

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
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), ResponseEntity::class.java)

        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), Exception::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun shouldNotBeAbleToMakeMoveOutsideGameBoard() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "d")
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), Exception::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun shouldBeAbleToWinGame() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        // We can win like this every time, because the AI is not the sharpest knife in the kitchen.
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("A", "C")), ResponseEntity::class.java)
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("B", "C")), ResponseEntity::class.java)
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("C", "C")), ResponseEntity::class.java)

        val result = testRestTemplate.getForEntity(
                "/game/$gameId",
                TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(gameId, result.body?.id)
        assertEquals('x', result.body?.winner)
    }
}
