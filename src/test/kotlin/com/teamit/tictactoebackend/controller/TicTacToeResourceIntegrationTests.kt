package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
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
    fun shouldNotStartGameWithInvalidCharacter() {
        val startGameRequest = StartGameRequest("testName", 'y')
        val result = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), Exception::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
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
    fun shouldGetGameWithIdInJson() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val httpHeaders = HttpHeaders()
        httpHeaders.accept = listOf(MediaType.APPLICATION_JSON)
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest, httpHeaders), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id
        val result = testRestTemplate.getForEntity(
                "/game/$gameId",
                TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(gameId, result.body?.id)
    }

    @Test
    fun shouldGetGameWithIdInAscii() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val httpHeaders = HttpHeaders()
        httpHeaders.accept = listOf(MediaType.TEXT_PLAIN)
        val result = testRestTemplate.exchange(
                "/game/$gameId",
                HttpMethod.GET,
                HttpEntity<Any>(httpHeaders),
                String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertTrue(result.body?.isNotEmpty()!!)
        assertTrue(result.body?.contains("A B C")!!)
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
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun shouldBeAbleToMakeMoveAndGetAsciiInReturn() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        val httpHeaders = HttpHeaders()
        httpHeaders.accept = listOf(MediaType.TEXT_PLAIN)
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest, httpHeaders), String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertTrue(result.body?.contains("A B C")!!)
    }

    @Test
    fun shouldBeAbleToMakeMoveAndGetJsonInReturn() {
        val startGameRequest = StartGameRequest("testName", 'x')
        val startGameResult = testRestTemplate.exchange("/game/", HttpMethod.POST, HttpEntity(startGameRequest), StartGameResponse::class.java)
        val gameId = startGameResult.body?.id

        val makeMoveRequest = MakeMoveRequest("a", "c")
        val httpHeaders = HttpHeaders()
        httpHeaders.accept = listOf(MediaType.APPLICATION_JSON)
        val result = testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest, httpHeaders), String::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertTrue(result.body?.startsWith("{")!!)
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
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(makeMoveRequest), TicTacToeGame::class.java)

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
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("A", "C")), TicTacToeGame::class.java)
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("B", "C")), TicTacToeGame::class.java)
        testRestTemplate.exchange("/game/$gameId/move", HttpMethod.POST, HttpEntity(MakeMoveRequest("C", "C")), TicTacToeGame::class.java)

        val result = testRestTemplate.getForEntity(
                "/game/$gameId",
                TicTacToeGame::class.java)

        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(gameId, result.body?.id)
        assertEquals('x', result.body?.winner)
    }
}
