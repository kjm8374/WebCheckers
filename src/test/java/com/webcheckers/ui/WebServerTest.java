package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.AccountManager;
import com.webcheckers.application.PlayerLobby;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * The unit test suite for the {@link WebServer} component.
 *
 * @author Ben
 */
@Tag("Application-tier")
class WebServerTest {

    // Attributes holding friendly objects
    private Gson gson = new Gson();

    // Attributes holding mock objects
    private TemplateEngine templateEngine = mock(TemplateEngine.class);
    private PlayerLobby playerLobby = mock(PlayerLobby.class);
    private AccountManager accountManager = mock(AccountManager.class);
    /**
     * Test the WebServer constructor with valid arguments. This should succeed without throwing any exceptions and
     * return a reference to the WebServer.
     */
    @Test
    void testConstructorValidArguments() {
        WebServer webServer = new WebServer(templateEngine, gson, playerLobby, accountManager);
        assertNotNull(webServer, "WebServer constructor returned null");
    }

    /**
     * Test passing null arguments to the WebServer constructor. In all cases, this should be rejected with a
     * NullPointerException.
     */
    @Test
    void testConstructorNullArguments() {
        assertThrows(NullPointerException.class, () -> {
            new WebServer(null, gson, playerLobby, accountManager);
        }, "WebServer constructor accepts null templateEngine parameter");
        assertThrows(NullPointerException.class, () -> {
            new WebServer(templateEngine, null, playerLobby, accountManager);
        }, "WebServer constructor accepts null gson parameter");
        assertThrows(NullPointerException.class, () -> {
            new WebServer(templateEngine, gson, null, accountManager);
        }, "WebServer constructor accepts null playerServices parameter");
    }

    /**
     * Test the WebServer.initialize() method.
     */
    @Test
    void testInitialize() {
        WebServer webServer = new WebServer(templateEngine, gson, playerLobby, accountManager);
        // When the WebServer constructor is passed valid arguments, the initialize() method should complete without
        // throwing any exceptions.
        assertDoesNotThrow(webServer::initialize, "WebServer.initialize method threw exception");
    }
}