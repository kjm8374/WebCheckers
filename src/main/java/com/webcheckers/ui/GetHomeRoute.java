package com.webcheckers.ui;


import com.webcheckers.application.GameReplay;
import com.webcheckers.application.GameReplayManager;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {

    static final String TITLE_ATR = ATTRIBUTES.TITLE_ATR.getAtr();
    static final String MESSAGE_ATR = ATTRIBUTES.MESSAGE_ATR.getAtr();
    static final String MESSAGE_TYPE_ATR = ATTRIBUTES.MESSAGE_TYPE_ATR.getAtr();
    static final String MESSAGE_TYPE_ERROR_ATR = ATTRIBUTES.MESSAGE_TYPE_ERROR_ATR.getAtr();
    static final String MESSAGE_TYPE_INFO_ATR = ATTRIBUTES.MESSAGE_TYPE_INFO_ATR.getAtr();
    static final String USERS_LIST_ATR = ATTRIBUTES.USERS_LIST_ATR.getAtr();
    static final String CURRENT_USER_ATR = ATTRIBUTES.CURRENT_USER_ATR.getAtr();
    static final String CURRENT_USER_KEY = "currentUser";
    static final String REPLAYS_LIST_KEY = "replaysList";

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;


    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetHomeRoute(final TemplateEngine templateEngine, PlayerLobby playerLobby) {
        Objects.requireNonNull(templateEngine, "templateEngine is required");
        Objects.requireNonNull(playerLobby, "playerLobby is required");
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;

        //
        LOG.config("GetHomeRoute is initialized.");
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetHomeRoute is invoked.");

        final Session httpSession = request.session();

        // Create the view-model for rendering the page
        Map<String, Object> vm = new HashMap<>();

        // store the Player object from the session into the view-model so their username can be accessed in nav-bar.ftl
        Player currentUser = httpSession.attribute(CURRENT_USER_KEY);

        if (currentUser != null && playerLobby.userInGame(currentUser)) {
            int gameID = playerLobby.getGame(currentUser).getGameID();
            response.redirect("/game?gameID=" + gameID);
            halt();
            return null;
        }

        vm.put(GetHomeRoute.CURRENT_USER_ATR, currentUser);

        // display a page title
        vm.put(TITLE_ATR, "Welcome!");

        // if there is a message to display, display it
        if (MESSAGE_TYPE_ERROR_ATR.equals(httpSession.attribute(MESSAGE_TYPE_ATR)) &&
                httpSession.attribute(MESSAGE_ATR) != null) {
            // there is an error message to display
            vm.put(MESSAGE_ATR, Message.error(httpSession.attribute(MESSAGE_ATR)));
            // once the message has been displayed, remove the session attributes indicating to display it
            httpSession.removeAttribute(MESSAGE_TYPE_ATR);
            httpSession.removeAttribute(MESSAGE_ATR);
        } else if (MESSAGE_TYPE_INFO_ATR.equals(httpSession.attribute(MESSAGE_TYPE_ATR)) &&
                httpSession.attribute(MESSAGE_ATR) != null) {
            // there is an info message to display
            vm.put(MESSAGE_ATR, Message.info(httpSession.attribute(MESSAGE_ATR)));
            // once the message has been displayed, remove the session attributes indicating to display it
            httpSession.removeAttribute(MESSAGE_TYPE_ATR);
            httpSession.removeAttribute(MESSAGE_ATR);
        } else {
            // if there is no message to display, then display a generic welcome message
            vm.put(MESSAGE_ATR, WELCOME_MSG);
        }

        //gets the list of players.
        List<Player> list = playerLobby.getUsers();

        //remove the current user. So that the user doesn't see themselves on the list of users.
        list.remove(currentUser);

        //display the list of users except the current user.
        vm.put(USERS_LIST_ATR, list);

        // get the list of replays
        List<GameReplay> replays = GameReplayManager.toList();

        // put the list of replays into the view-model so it can be inserted into the template during rendering
        vm.put(REPLAYS_LIST_KEY, replays);


        // render the View
        return templateEngine.render(new ModelAndView(vm, VIEWS.HOME_VIEW.getView()));
    }

}
