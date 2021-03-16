package com.webcheckers.ui;

import com.webcheckers.application.GameLobby;
import com.webcheckers.application.GameReplayManager;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetStopReplayRouteTest {
    /**
     * Tests the handle function
     */
    @Test
    public void testHandle() {
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session session = mock(Session.class);
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Bill"));
        when(request.session()).thenReturn(session);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");

        GetStopReplayRoute route = new GetStopReplayRoute();

        GameReplayManager.saveReplay(new GameLobby(0, new Player("Jobe"), new Player("Jebidiah"), new MoveValidator()));
        GameReplayManager.getReplay(0, new Player("Bill"));

        assertThrows(HaltException.class, () -> {
            route.handle(request, response);
        });
    }
}
