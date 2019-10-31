package com.teamit.tictactoebackend.config

import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Configuration

@Configuration
class JerseyConfig : ResourceConfig() {
    init {
        registerEndpoints()
        registerExceptionMappers()
    }

    private fun registerEndpoints() {
        packages("com.teamit.tictactoebackend.controller")
    }

    private fun registerExceptionMappers() {
        packages("com.teamit.tictactoebackend.exception.mapper")
    }
}