package com.teamit.tictactoebackend.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.teamit.tictactoebackend.model.game.TicTacToeGame
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import org.springframework.stereotype.Service
import java.io.File

@Service
class TicTacToeGameDTO {

    fun saveGame(game: TicTacToeGame) {
        val objectMapper = jacksonObjectMapper()
        val gameFile = getGameFile(game.id)
        gameFile.parentFile.mkdirs()
        objectMapper.writeValue(gameFile, game)
    }

    fun getGame(id: String): TicTacToeGame? {
        val gameFile = getGameFile(id)
        return loadGameFile(gameFile)
    }

    fun getGames(): TicTacToeGames {
        val games: ArrayList<TicTacToeGame> = ArrayList()
        File("data/games/").walkBottomUp().map {
            games.add(loadGameFile(it))
        }
        return TicTacToeGames(games)
    }

    private fun loadGameFile(path: File): TicTacToeGame {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(path, TicTacToeGame::class.java)
    }

    private fun getGameFile(id: String) : File {
        return File("data/games/$id.json")
    }

}
