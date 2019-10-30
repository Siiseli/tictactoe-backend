package com.teamit.tictactoebackend.config

import com.teamit.tictactoebackend.controller.TicTacToeResource
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.stereotype.Component

@Component
class JerseyConfig : ResourceConfig() {
    init {
        registerEndpoints()
    }

    private fun registerEndpoints() {
        register(TicTacToeResource::class.java)
    }
}