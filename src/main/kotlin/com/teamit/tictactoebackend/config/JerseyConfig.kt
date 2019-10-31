package com.teamit.tictactoebackend.config

import com.teamit.tictactoebackend.controller.TicTacToeResource
import com.teamit.tictactoebackend.exception.mapper.GameNotFoundExceptionMapper
import com.teamit.tictactoebackend.exception.mapper.IllegalMoveExceptionMapper
import com.teamit.tictactoebackend.exception.mapper.InvalidCharacterExceptionMapper
import com.teamit.tictactoebackend.exception.mapper.UnhandledExceptionMapper
import io.swagger.jaxrs.config.BeanConfig
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.jaxrs.listing.SwaggerSerializers
import org.glassfish.jersey.server.ResourceConfig
import org.springframework.context.annotation.Configuration

@Configuration
class JerseyConfig : ResourceConfig() {
    init {
        registerEndpoints()
        registerExceptionMappers()
        configureSwagger()
    }

    private fun registerEndpoints() {
        register(TicTacToeResource::class.java)
    }

    private fun registerExceptionMappers() {
        register(GameNotFoundExceptionMapper::class.java)
        register(IllegalMoveExceptionMapper::class.java)
        register(InvalidCharacterExceptionMapper::class.java)
        register(UnhandledExceptionMapper::class.java)
    }

    private fun configureSwagger() {
        this.register(ApiListingResource::class.java)
        this.register(SwaggerSerializers::class.java)

        val config = BeanConfig()
        config.configId = "tictactoe-backend"
        config.title = "Tic tac toe API"
        config.version = "v1"
        config.schemes = arrayOf("http")
        config.basePath = "/api"
        config.resourcePackage = "com.teamit.tictactoebackend.controller"
        config.prettyPrint = true
        config.scan = true
    }
}