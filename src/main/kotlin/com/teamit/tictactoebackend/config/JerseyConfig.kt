package com.teamit.tictactoebackend.config

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
        packages("com.teamit.tictactoebackend.controller")
    }

    private fun registerExceptionMappers() {
        packages("com.teamit.tictactoebackend.exception.mapper")
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