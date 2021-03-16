package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the functionality of PostCheckTurnRoute
 * @author Chris Piccoli
 */
public class PostCheckTurnRouteTest {
    /**
     * tests the handle function
     */
    @Test
    public void handleTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session mockSess = mock(Session.class);
        PlayerLobby playerLobby = mock(PlayerLobby.class);
        MoveValidator validator = mock(MoveValidator.class);

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Fred"));

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(request.session()).thenReturn(mockSess);

        GameLobby game = new GameLobby(0, new Player("Fred"), new Player("Scooby"), validator);

        when(playerLobby.getGame(0)).thenReturn(game);

        when(validator.undoMove(game)).thenReturn(new JsonObject());

        PostCheckTurnRoute route = new PostCheckTurnRoute(playerLobby);


        JsonObject expected = new JsonObject();
        expected.addProperty("type", "INFO");
        expected.addProperty("text", "true");

        assertEquals(expected, route.handle(request, response));
    }

    /**
     * Tests the case if the game is over
     */
    @Test
    public void testOpponentResigned() {

        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session mockSess = mock(Session.class);
        PlayerLobby playerLobby = mock(PlayerLobby.class);
        GameLobby game = mock(GameLobby.class);

        when(request.session()).thenReturn(mockSess);

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Billy Brown"));
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");

        JsonObject expected = new JsonObject();
        expected.addProperty("type", "INFO");
        expected.addProperty("text", "true");

        when(game.isGameDone()).thenReturn(true);
        when(playerLobby.getGame(0)).thenReturn(game);

        assertEquals(expected, new PostCheckTurnRoute(playerLobby).handle(request, response));
    }
}
