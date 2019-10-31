package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.exception.GameNotFoundException
import com.teamit.tictactoebackend.exception.IllegalMoveException
import com.teamit.tictactoebackend.exception.InvalidCharacterException
import com.teamit.tictactoebackend.model.game.*
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
        return try {
            val game = ticTacToeService.startGame(startGameRequest.name, startGameRequest.character)
            Response.status(Response.Status.CREATED).entity(StartGameResponse(game.id)).build()
        } catch(e: InvalidCharacterException) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e).build()
        } catch(e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build()
        }
    }

    @GET
    @Path("{id}")
    fun getGame(@PathParam("id") id: String, @HeaderParam("accept") accept: String) : Response {
        return try {
            val game = ticTacToeService.getGame(id)
            if(accept.contains(MediaType.APPLICATION_JSON)) {
                return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
            }
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(ticTacToeVisualizer.visualize(game)).build()
        } catch(e: GameNotFoundException) {
            Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(e).build()
        } catch(e: Exception) {
            Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity(e).build()
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
