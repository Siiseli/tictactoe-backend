package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.MakeMoveRequest
import com.teamit.tictactoebackend.model.game.StartGameRequest
import com.teamit.tictactoebackend.model.game.StartGameResponse
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import com.teamit.tictactoebackend.service.TicTacToeService
import com.teamit.tictactoebackend.service.TicTacToeVisualizerService
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

    @Autowired
    private lateinit var ticTacToeVisualizer: TicTacToeVisualizerService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getGames(): TicTacToeGames {
        return ticTacToeService.getGames()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun startGame(startGameRequest: StartGameRequest) : Response {
        val game = ticTacToeService.startGame(startGameRequest.name, startGameRequest.character)
        return Response.status(Response.Status.CREATED).entity(StartGameResponse(game.id)).build()
    }

    @GET
    @Path("{id}")
    fun getGame(@PathParam("id") id: String, @HeaderParam("accept") accept: String) : Response {
        val game = ticTacToeService.getGame(id)
        if(accept.contains(MediaType.APPLICATION_JSON)) {
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(ticTacToeVisualizer.visualize(game)).build()
    }

    @POST
    @Path("{id}/move")
    @Consumes(MediaType.APPLICATION_JSON)
    fun makeMove(@PathParam("id") id: String, @HeaderParam("accept") accept: String, makeMoveRequest: MakeMoveRequest) : Response {
        val game = ticTacToeService.makeMove(id, makeMoveRequest.col, makeMoveRequest.row)
        if(accept.contains(MediaType.APPLICATION_JSON)) {
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(ticTacToeVisualizer.visualize(game)).build()
    }
}
