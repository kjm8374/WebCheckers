package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetGameRouteTest {

    /**
     * The component-under-test (CuT).
     */
    private GetGameRoute CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;
    private Player player;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = new PlayerLobby();
        // create a unique CuT for each test
        CuT = new GetGameRoute(engine, new Gson(), playerLobby);
    }

    /**
     * Test that the Game view will create a new game if none exists.
     */
    @Test
    public void new_game_no_players() {
        player = new Player("Adam");
        Player opponent = new Player("evil Adam");
        playerLobby.createGame(player, opponent);
        // Arrange the test scenario: The session holds no game.
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(player);

        // To analyze what the Route created in the View-Model map you need
        // to be able to extract the argument to the TemplateEngine.render method.
        // Mock up the 'render' method by supplying a Mockito 'Answer' object
        // that captures the ModelAndView data passed to the template engine
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Test that a redirect occurs (a HaltException will be thrown)
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

    }

    /**
     * Test that CuT redirects to the Home view when a @Linkplain(PlayerServices) object does
     * not exist, i.e. the session timed out or an illegal request on this URL was received.
     */
    @Test
    public void faulty_session() {
        // Arrange the test scenario: There is an existing session with a PlayerServices object
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(null);

        // Test that a redirect occurs (a HaltException will be thrown)
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Test if player is not in this game
     */
    @Test
    public void test_not_in_game() {

        when(request.queryParams("opponent")).thenReturn("Billy");
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");

        playerLobby.addUsername("Billy");
        playerLobby.addUsername("Ugandan Knuckles");
        playerLobby.createGame(new Player("Billy"), new Player("Ugandan Knuckles"));

        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Jeff"));

        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Test if the opponent does not exist
     */
    @Test
    public void test_no_opponent() {

        when(request.queryParams("opponent")).thenReturn(null);
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn(null);

        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Jeff"));

        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Game does not exit
     */
    @Test
    public void text_game_doesnt_exist() {
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("999");

        playerLobby.addUsername("Billy");
        playerLobby.addUsername("Ugandan Knuckles");
        playerLobby.createGame(new Player("Billy"), new Player("Ugandan Knuckles"));

        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Ugandan Knuckles"));


        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Test if player is signed in
     */
    @Test
    public void test_user_null() {
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(null);

        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Test if GameID is broke
     */
    @Test
    public void invalid_GameID() {
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("A");

        playerLobby.addUsername("Billy");
        playerLobby.addUsername("Ugandan Knuckles");
        playerLobby.createGame(new Player("Billy"), new Player("Ugandan Knuckles"));

        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Ugandan Knuckles"));

        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }

    /**
     * Test Constructor
     */
    @Test
    public void test_constructor() {

        assertDoesNotThrow(() -> {
                    new GetGameRoute(new FreeMarkerEngine(), new Gson(), playerLobby, GetGameRoute.VIEW_MODES.PLAY);
                },
                "Threw an exception during constructor");
    }

    /**
     * Game does not exit
     */
    @Test
    public void text_game_exists() {
        when(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr())).thenReturn("0");

        playerLobby.addUsername("Billy");
        playerLobby.addUsername("Ugandan Knuckles");
        playerLobby.createGame(new Player("Billy"), new Player("Ugandan Knuckles"));
        when(session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr())).thenReturn(new Player("Ugandan Knuckles"));


        assertDoesNotThrow(() -> {
            CuT.handle(request, response);
        }, "No halt exception during redirect");

    }

}
