package com.teamit.tictactoebackend.controller

import com.teamit.tictactoebackend.model.game.MakeMoveRequest
import com.teamit.tictactoebackend.model.game.StartGameRequest
import com.teamit.tictactoebackend.model.game.StartGameResponse
import com.teamit.tictactoebackend.model.game.TicTacToeGames
import com.teamit.tictactoebackend.service.TicTacToeService
import com.teamit.tictactoebackend.service.TicTacToeVisualizerService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Path("/game")
@Api(value = "Tic tac toe resource", produces = MediaType.APPLICATION_JSON)
class TicTacToeResource {

    @Autowired
    private lateinit var ticTacToeService: TicTacToeService

    @Autowired
    private lateinit var ticTacToeVisualizer: TicTacToeVisualizerService

    @GET
    @ApiOperation(value = "Gets all games", response = TicTacToeGames::class)
    @ApiResponses( value = [
        ApiResponse(code = 200, message = "List of all games"),
        ApiResponse(code = 500, message = "Something went horribly wrong")])
    @Produces(MediaType.APPLICATION_JSON)
    fun getGames(): TicTacToeGames {
        return ticTacToeService.getGames()
    }

    @POST
    @ApiOperation(value = "Starts a new game", response = TicTacToeGames::class)
    @ApiResponses( value = [
        ApiResponse(code = 200, message = "Game was started successfully, and a game id is returned"),
        ApiResponse(code = 400, message = "Invalid player character supplied"),
        ApiResponse(code = 500, message = "Something went horribly wrong")])
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun startGame(startGameRequest: StartGameRequest) : Response {
        val game = ticTacToeService.startGame(startGameRequest.name, startGameRequest.character)
        return Response.status(Response.Status.CREATED).entity(StartGameResponse(game.id)).build()
    }

    @GET
    @Path("{id}")
    @ApiOperation(value = "Gets game with game id", response = TicTacToeGames::class)
    @ApiResponses( value = [
        ApiResponse(code = 200, message = "Current game status returned. Specify accept header application/json to get response in JSON, otherwise it will be an ASCII representation"),
        ApiResponse(code = 404, message = "Game with this id was not found"),
        ApiResponse(code = 500, message = "Something went horribly wrong")])
    fun getGame(
            @PathParam("id") id: String,
            @HeaderParam("accept") accept: String) : Response {
        val game = ticTacToeService.getGame(id)
        if(accept.contains(MediaType.APPLICATION_JSON)) {
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(ticTacToeVisualizer.visualize(game)).build()
    }

    @POST
    @ApiOperation(value = "Makes a move to an ongoing game with a specific game id", response = TicTacToeGames::class)
    @ApiResponses( value = [
        ApiResponse(code = 200, message = "Move was made. Current game status returned. Specify accept header application/json to get response in JSON, otherwise it will be an ASCII representation"),
        ApiResponse(code = 400, message = "Tried to make illegal move"),
        ApiResponse(code = 404, message = "Game with this id was not found"),
        ApiResponse(code = 500, message = "Something went horribly wrong")])
    @Path("{id}/move")
    @Consumes(MediaType.APPLICATION_JSON)
    fun makeMove(
            @PathParam("id") id: String,
            @HeaderParam("accept") accept: String,
            makeMoveRequest: MakeMoveRequest) : Response {
        val game = ticTacToeService.makeMove(id, makeMoveRequest.col, makeMoveRequest.row)
        if(accept.contains(MediaType.APPLICATION_JSON)) {
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(game).build()
        }
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity(ticTacToeVisualizer.visualize(game)).build()
    }
}
