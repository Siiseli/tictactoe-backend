package com.teamit.tictactoebackend.service

import com.teamit.tictactoebackend.model.game.TicTacToeGame
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TicTacToeService {

    @Autowired
    private lateinit var gameDTO: TicTacToeGameDTO

    fun getGames(): TicTacToeGames {
        return gameDTO.getGames()
    }

    fun startGame(name: String, character: String) : TicTacToeGame {
        val game = TicTacToeGame(UUID.randomUUID().toString())

        gameDTO.saveGame(game)

        return game
    }

    fun getGame(id: String): TicTacToeGame? {
        return gameDTO.getGame(id)
    }
}