package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.exception.IllegalMoveException
import com.teamit.tictactoebackend.model.game.*
import com.teamit.tictactoebackend.service.TicTacToeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Path("/game")
class TicTacToeResource {

    @Autowired
    private lateinit var ticTacToeService: TicTacToeService

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getGames(): TicTacToeGames {
        return ticTacToeService.getGames()
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun startGame(startGameRequest: StartGameRequest) : Response {
        return try {
            val game = ticTacToeService.startGame(startGameRequest.name, startGameRequest.character)
            Response.status(Response.Status.CREATED).entity(StartGameResponse(game.id)).build()
        } catch(e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build()
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getGame(@PathParam("id") id: String) : Response {
        return try {
            val game = ticTacToeService.getGame(id)
            Response.status(Response.Status.OK).entity(game).build()
        } catch(e: GameNotFoundException) {
            Response.status(Response.Status.NOT_FOUND).entity(e).build()
        } catch(e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build()
        }
    }

    @POST
    @Path("{id}/move")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun makeMove(@PathParam("id") id: String, makeMoveRequest: MakeMoveRequest) : Response {
        return try {
            val game = ticTacToeService.makeMove(id, makeMoveRequest.col, makeMoveRequest.row)
            Response.status(Response.Status.OK).entity(game).build()
        } catch(e: GameNotFoundException) {
            Response.status(Response.Status.NOT_FOUND).entity(e).build()
        }
        catch(e: IllegalMoveException) {
            Response.status(Response.Status.BAD_REQUEST).entity(e).build()
        }
        catch(e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build()
        }
    }
}
