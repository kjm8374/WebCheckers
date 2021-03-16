package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.GameReplayManager;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetReplayRouteTest {
    /**
     * Tests the constructor
     */
    @Test
    public void ConstructorTest() {
        GetReplayRoute route = new GetReplayRoute(mock(TemplateEngine.class), new Gson(), mock(PlayerLobby.class));

        assertNotNull(route, "Route is null Error");
    }

    /**
     * Tests The handle function
     */
    @Test
    public void handleTest() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");

        GameLobby game = new GameLobby(0, new Player("Fred"), new Player("Scooby"), new MoveValidator());
        GameReplayManager.saveReplay(game);

        PlayerLobby lobby = mock(PlayerLobby.class);
        Session mockSession = mock(Session.class);
        when(mockSession.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Bobby Hill"));

        when(request.session()).thenReturn(mockSession);

        GetReplayRoute route = new GetReplayRoute(new FreeMarkerEngine(), new Gson(), lobby);

        String htmlText = (String) route.handle(request, response);

        assertNotNull(htmlText, "Returned Null, Expected not Null");
    }
}
