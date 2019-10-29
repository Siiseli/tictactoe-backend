package com.teamit.tictactoebackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class TicTacToeBackendApplication : SpringBootServletInitializer() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TicTacToeBackendApplication().configure(SpringApplicationBuilder(TicTacToeBackendApplication::class.java)).run(*args)
        }
    }
}
