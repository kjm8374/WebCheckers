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
 * Tests the functionality of PostResignGameRoute
 * @author Chris Piccoli
 */

public class PostResignGameRouteTest {

    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private MoveValidator moveValidator = mock(MoveValidator.class);

    /**
     * Creates mocks for each test in the class
     */
    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = mock(PlayerLobby.class);

    }

    /**
     * Tests the case if the move is false
     */
    @Test
    public void testResigned() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "INFO");
        jsonObject.addProperty("text", "false");
        GameLobby game = new GameLobby(0, new Player("Billy Eilish"), new Player("Joke here"), moveValidator);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(playerLobby.getGame(0)).thenReturn(game);
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Billy Eilish"));


        assertEquals(jsonObject, new PostResignGameRoute(playerLobby).handle(request, response));
    }

    /**
     * Tests the case if the game is over
     */
    @Test
    public void testOpponentResigned() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ERROR");
        jsonObject.addProperty("text", "This game is already over, because your opponent resigned.Click my home to enter a game. Click my home to enter a game.");
        GameLobby game = mock(GameLobby.class);
        when(game.isGameDone()).thenReturn(true);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(playerLobby.getGame(0)).thenReturn(game);

        assertEquals(jsonObject, new PostResignGameRoute(playerLobby).handle(request, response));
    }
}
