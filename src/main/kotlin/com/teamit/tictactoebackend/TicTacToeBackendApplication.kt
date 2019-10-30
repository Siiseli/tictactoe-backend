package com.teamit.tictactoebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicTacToeBackendApplication

fun main(args: Array<String>) {
    runApplication<TicTacToeBackendApplication>(*args)
}
