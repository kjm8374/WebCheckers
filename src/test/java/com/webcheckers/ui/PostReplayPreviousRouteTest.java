package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.GameReplayManager;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostReplayPreviousRouteTest {
    /**
     * Tests the constructor
     */
    @Test
    public void ConstructorTest() {
        PostReplayNextRoute route = new PostReplayNextRoute();

        assertNotNull(route, "Route is null Error");
    }

    /**
     * Tests if there is not valid next move to make
     */
    @Test
    public void handleTestNoPrevious() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session mockSess = mock(Session.class);

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Billy Brown"));

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(request.session()).thenReturn(mockSess);

        GameLobby game = new GameLobby(0, new Player("Fred"), new Player("Scooby"), new MoveValidator());
        GameReplayManager.saveReplay(game);

        PostReplayPreviousRoute route = new PostReplayPreviousRoute();

        JsonObject handleResponse = (JsonObject) route.handle(request, response);


        assertEquals(handleResponse.get("type").getAsString(), "INFO", "Incorrect type Json Returned");
        assertEquals(handleResponse.get("text").getAsString(), "false", "Incorrect type Json Returned");

        GameReplayManager.endReplay(0, new Player("Billy Brown"));


    }

    /**
     * Tests if there is a valid previous move to make
     */
    @Test
    public void handleTesthasPrevious() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session mockSess = mock(Session.class);

        when(mockSess.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Bobby Billy Brown"));

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");
        when(request.session()).thenReturn(mockSess);

        GameLobby game = new GameLobby(0, new Player("Fred"), new Player("Scooby"), new MoveValidator());


        game.makeMove(new Move(new Position(5, 0), new Position(4, 1)));
        GameReplayManager.saveReplay(game);

        GameReplayManager.getReplay(0, new Player("Bobby Billy Brown")).nextMove();

        PostReplayPreviousRoute route = new PostReplayPreviousRoute();

        JsonObject handleResponse = (JsonObject) route.handle(request, response);

        assertEquals(handleResponse.get("type").getAsString(), "INFO", "Incorrect type Json Returned");
        assertEquals(handleResponse.get("text").getAsString(), "true", "Incorrect type Json Returned");

        GameReplayManager.endReplay(0, new Player("Bobby Billy Brown"));

    }
}
