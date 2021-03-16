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
 * Tests the functionality of postBackUpMoveRoute
 * @author Chris Piccoli
 */

public class PostBackUpMoveRouteTest {
    /**
     * Tests if there is no valid next move to make
     */
    @Test
    public void handleTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session mockSess = mock(Session.class);
        PlayerLobby playerLobby = mock(PlayerLobby.class);
        MoveValidator validator = mock(MoveValidator.class);

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Billy Brown"));

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(request.session()).thenReturn(mockSess);

        GameLobby game = new GameLobby(0, new Player("Fred"), new Player("Scooby"), validator);

        when(playerLobby.getGame(0)).thenReturn(game);

        when(validator.undoMove(game)).thenReturn(new JsonObject());

        PostBackUpMoveRoute route = new PostBackUpMoveRoute(playerLobby);

        assertEquals(new JsonObject(), route.handle(request, response));

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

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Billy Brown"));

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(request.session()).thenReturn(mockSess);

        GameLobby game = mock(GameLobby.class);
        when(game.isGameDone()).thenReturn(true);
        when(playerLobby.getGame(0)).thenReturn(game);

        PostBackUpMoveRoute route = new PostBackUpMoveRoute(playerLobby);

        JsonObject handleResponse = (JsonObject) route.handle(request, response);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "ERROR");
        jsonObject.addProperty("text", " You can't backup a move because your opponent resigned. Click my home to enter a game.");

        assertEquals(jsonObject, handleResponse);
    }
}
