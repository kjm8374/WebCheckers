package com.webcheckers.ui;

import com.webcheckers.application.AccountManager;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the PostAccountDetailsRoute
 * @author Adam Densman
 */

@Tag("UI-Tier")
public class PostAccountDetailsRouteTest {


    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private GetHomeRoute getHomeRoute;
    private PlayerLobby playerLobby;
    private AccountManager accountManager;
    private Player player;
    private Player player2;
    private String password;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        getHomeRoute = mock(GetHomeRoute.class);
        playerLobby = new PlayerLobby();
        accountManager = new AccountManager();
        player = new Player("player");
        player2 = new Player("player2");
        password = "password";
    }
}
