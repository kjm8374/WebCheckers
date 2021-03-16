package com.webcheckers.ui;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Sign In page.
 *
 * @author Kushal
 */
public class GetSignInRoute implements Route {

    static final String VIEW_NAME = VIEWS.SIGNIN_VIEW.getView();
    static final String TITLE_ATR = ATTRIBUTES.TITLE_ATR.getAtr();
    static final String SIGNIN_MSG = "Welcome to the sign in page.";

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());


    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetSignInRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");

        final Session session = request.session();
        // If the user is already signed in, redirect them back to the home page
        if (session.attribute(GetHomeRoute.CURRENT_USER_KEY) != null) {
            response.redirect(URLS.HOME_URL.getURL());
            halt();
            return null;
        } else {
            // Create the view-model for rendering the page
            Map<String, Object> vm = new HashMap<>();
            // display a page title
            vm.put(TITLE_ATR, SIGNIN_MSG);

            // render the View
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
    }
}
