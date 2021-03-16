package com.webcheckers.ui;


import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the GetHomeRouteTest
 *
 * @author Chris Piccoli
 */
public class GetHomeRouteTest {
    private GetHomeRoute CuT;

    private TemplateEngine templateEngine;
    private PlayerLobby playerLobby = new PlayerLobby();
    private Request request;
    private Session session;
    private Response response;

    /**
     * sets up the mocks for the other tests in this class
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);

        //Create a unique CuT for each test
        CuT = new GetHomeRoute(templateEngine, playerLobby);
    }

    /**
     * Test the constructor arguments, to see if it catches the null values
     */
    @Test
    void testConstructorNullArguments() {
        assertThrows(NullPointerException.class, () -> {
            new GetHomeRoute(null, playerLobby);
        }, "WebServer constructor accepts null templateEngine parameter");
        assertThrows(NullPointerException.class, () -> {
            new GetHomeRoute(templateEngine, null);
        }, "WebServer constructor accepts null gson parameter");
    }

    /**
     * Test if the route is properly created
     */
    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> {
            new GetHomeRoute(templateEngine, playerLobby);
        });

    }

    /**
     * Tests the handle function
     */
    @Test
    public void testHandleHome() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
    }
}

