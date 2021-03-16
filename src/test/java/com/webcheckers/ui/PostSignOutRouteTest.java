package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Tag("UI-tier")
public class PostSignOutRouteTest {
    private PostSignOutRoute CuT;
    private PlayerLobby playerLobby;
    private final String player1 = "Halle";
    private final String player2 = "Grace";

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private GetHomeRoute getHomeRoute;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        getHomeRoute = mock(GetHomeRoute.class);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        playerLobby.addUsername(player1);
        playerLobby.addUsername(player2);


    }

    @Test
    public void test1() {
        Player firstPlayer = new Player(player1);
        Player secondPlayer = new Player(player2);
        List<Player> players = playerLobby.getUsers();
        assertTrue( players.contains( firstPlayer));
        assertTrue( players.contains( secondPlayer));
        playerLobby.signOutUser(player1);
        players = playerLobby.getUsers();
        assertFalse(players.contains(firstPlayer));
        assertTrue(players.contains(secondPlayer));
        playerLobby.signOutUser(player2);
        players = playerLobby.getUsers();
        assertFalse(players.contains(secondPlayer));
    }
}
