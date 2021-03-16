package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.HaltException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetSignInRouteTest {

    private GetSignInRoute CuT;

    private PlayerLobby playerLobby;
    //mock objects
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);

        //Create a unique CuT for each test
        CuT = new GetSignInRoute(engine);
    }

    @Test
    public void testUserNotSignedIn() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetSignInRoute.TITLE_ATR, GetSignInRoute.SIGNIN_MSG);
    }

    @Test
    public void testUserSignedIn() {

        playerLobby = mock(PlayerLobby.class);
        playerLobby.addUsername("Adam");

        when(session.attribute(GetHomeRoute.CURRENT_USER_KEY)).thenReturn(playerLobby);
        assertThrows(HaltException.class, () -> {
            CuT.handle(request, response);
        }, "Redirects invoke halt exceptions.");

        verify(response).redirect(URLS.HOME_URL.getURL());
    }
}