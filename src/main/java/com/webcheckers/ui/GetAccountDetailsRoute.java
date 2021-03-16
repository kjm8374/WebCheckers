package com.webcheckers.ui;

import com.webcheckers.model.Player;
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
 * The UI Controller to GET the Account Details page.
 *
 * @author Adam Densman
 */
public class GetAccountDetailsRoute implements Route {

    static final String VIEW_NAME = VIEWS.ACCOUNT_VIEW.getView();
    static final String INFO_ATR = ATTRIBUTES.INFO_ATR.getAtr();
    static final String TITLE_ATR = ATTRIBUTES.TITLE_ATR.getAtr();
    static final String CURRENT_USER_ATR = "currentUser";
    static final String CURRENT_USER_KEY = "currentUser";

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private static final String ACCOUNT_DETAILS_MSG = "Welcome to the account details page.";


    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetAccountDetailsRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetAccountDetailsRoute is initialized.");
    }

    /**
     * Render the WebCheckers account details page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the account details page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetAccountDetailsRoute is invoked.");

        final Session session = request.session();
        // If the user is not already signed in, redirect them back to the home page
        if (session.attribute(GetHomeRoute.CURRENT_USER_KEY) == null) {
            response.redirect(URLS.HOME_URL.getURL());
            halt();
            return null;
        } else {
            Player currentUser = session.attribute(CURRENT_USER_KEY);
            // Create the view-model for rendering the page
            Map<String, Object> vm = new HashMap<>();
            // display a page title
            vm.put(TITLE_ATR, ACCOUNT_DETAILS_MSG);

            vm.put(GetHomeRoute.CURRENT_USER_ATR, currentUser);
            // render the View
            return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
        }
    }
}
