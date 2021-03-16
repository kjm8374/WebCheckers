package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of PostSubmitTurnRoute
 * @author Halle Massaryk
 */

public class PostSubmitTurnRouteTest {

    private Request request;
    private Response response;
    private PlayerLobby playerLobby;
    private MoveValidator moveValidator = mock(MoveValidator.class);

    /**
     * Sets up the mocks for the other tests in the class
     */
    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = mock(PlayerLobby.class);

    }

    /**
     * Adds tests the case if a move is true
     */
    @Test
    public void testHandMoveTrue() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "INFO");
        GameLobby game = new GameLobby(0, new Player("Billy Eilish"), new Player("Joke here"), moveValidator);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(playerLobby.getGame(0)).thenReturn(game);
        when(moveValidator.ValidateTurn(game.getRedBoard(), game.getWhiteBoard(), game.getActiveColor())).thenReturn(jsonObject);

        assertEquals(jsonObject, new PostSubmitTurnRoute(playerLobby).handle(request, response));
    }

    /**
     * Tests the case if the move is false
     */
    @Test
    public void testHandMoveFalse() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ERROR");
        GameLobby game = new GameLobby(0, new Player("Billy Eilish"), new Player("Joke here"), moveValidator);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(playerLobby.getGame(0)).thenReturn(game);
        when(moveValidator.ValidateTurn(game.getRedBoard(), game.getWhiteBoard(), game.getActiveColor())).thenReturn(jsonObject);

        assertEquals(jsonObject, new PostSubmitTurnRoute(playerLobby).handle(request, response));
    }

    /**
     * Tests the case if the game is over
     */
    @Test
    public void testGameOver() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "INFO");
        jsonObject.addProperty("text", "The game is already over.");
        GameLobby game = mock(GameLobby.class);
        when(game.isGameDone()).thenReturn(true);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(playerLobby.getGame(0)).thenReturn(game);
        when(moveValidator.ValidateTurn(game.getRedBoard(), game.getWhiteBoard(), game.getActiveColor())).thenReturn(jsonObject);

        assertEquals(jsonObject, new PostSubmitTurnRoute(playerLobby).handle(request, response));
    }
}
