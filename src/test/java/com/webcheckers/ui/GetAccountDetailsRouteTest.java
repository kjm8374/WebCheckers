package com.webcheckers.ui;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


/**
 * Tests the functionality of GetAccountDetailsRoute
 * @author Adam Densman
 */
@Tag("UI-Tier")
public class GetAccountDetailsRouteTest {
    private TemplateEngine templateEngine = mock(TemplateEngine.class);

    /**
     * Test if the route is properly created
     */
    @Test
    void testConstructor() {
        GetAccountDetailsRoute route = new GetAccountDetailsRoute(templateEngine);

        assertNotNull(route, "GetAccountDetailsRoute is not initialized with " +
                "valid constructor arguments");
    }
}
