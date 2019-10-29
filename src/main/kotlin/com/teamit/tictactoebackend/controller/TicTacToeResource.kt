package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.StartGameRequest
import com.teamit.tictactoebackend.model.game.StartGameResponse
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import com.teamit.tictactoebackend.service.TicTacToeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Path("/game")
class TicTacToeResource {

    @Autowired
    private lateinit var ticTacToeService: TicTacToeService

    @GET
    fun getGames(): TicTacToeGames {
        return ticTacToeService.getGames()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun startGame(startGameRequest: StartGameRequest) : Response {
        return try {
            val game = ticTacToeService.startGame(startGameRequest.name, startGameRequest.character)
            Response.status(Response.Status.CREATED).entity(StartGameResponse(game.id)).build()
        } catch(e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGame(@PathParam("id") id: String) : Response {
        try {
            val game = ticTacToeService.getGame(id) ?: return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build()
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
        } catch(e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build()
        }
    }
}
