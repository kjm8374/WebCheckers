package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
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
 * Route to the Game Page
 *
 * @author Chris Piccoli, Ben Coffta
 */
public class GetGameRoute implements Route {


    //@TODO Figure out where this should actually go
    public enum VIEW_MODES {
        PLAY, SPECTATOR, REPLAY
    }

    protected static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    protected final TemplateEngine templateEngine;
    protected final Gson gson;
    protected PlayerLobby playerLobby;

    protected static VIEW_MODES view_mode = VIEW_MODES.PLAY;

    static final String TITLE_ATR = ATTRIBUTES.TITLE_ATR.getAtr();
    static final String GAME_ID_ATR = ATTRIBUTES.GAME_ID_ATR.getAtr();
    static final String RED_PLAYER_ID_ATR = ATTRIBUTES.RED_PLAYER_ID_ATR.getAtr();
    static final String WHITE_PLAYER_ID_ATR = ATTRIBUTES.WHITE_PLAYER_ID_ATR.getAtr();
    static final String ACTIVE_COLOR_ATR = ATTRIBUTES.ACTIVE_COLOR_ATR.getAtr();
    static final String VIEW_MODE_ATR = ATTRIBUTES.VIEW_MODE_ATR.getAtr();
    static final String GAME_BOARD_ATR = ATTRIBUTES.GAME_BOARD_ATR.getAtr();
    static final String CURRENT_USER_ATR = ATTRIBUTES.CURRENT_USER_ATR.getAtr();

    static final String VIEW_NAME = VIEWS.GAME_VIEW.getView();
    static final String MODE_OPTIONS_AS_JSON = "modeOptionsAsJSON";


    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     * @param gson           the gson for parsing objects
     * @param playerLobby    the playerlobby for keeping track of players
     */
    public GetGameRoute(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        LOG.config("GetGameRoute is initialized: type " + view_mode.toString());
    }

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    protected GetGameRoute(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby, VIEW_MODES view_mode) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        GetGameRoute.view_mode = view_mode;
        LOG.config("GetGameRoute is initialized: type " + view_mode.toString());
    }

    /**
     * Adds stuff to the vm put specifying the ftl arguments
     *
     * @param vm          the map
     * @param game        the gamelobby
     * @param currentUser the current user
     */
    protected void initGameInfo(Map<String, Object> vm, GameLobby game, Player currentUser) {
        // put the rest of the relevant objects into the view-model
        vm.put(CURRENT_USER_ATR, currentUser);
        vm.put(GAME_ID_ATR, game.getGameID());
        vm.put(RED_PLAYER_ID_ATR, game.getRedPlayer());
        vm.put(WHITE_PLAYER_ID_ATR, game.getWhitePlayer());
        vm.put(ACTIVE_COLOR_ATR, game.getActiveColor());
    }

    /**
     * Adds stuff to the vm put specifying the board
     *
     * @param vm          the map
     * @param game        the gamelobby
     * @param currentUser the current user
     */
    protected void initBoard(Map<String, Object> vm, GameLobby game, Player currentUser) {
        // put the appropriate game board into the view-model
        if (game.getRedPlayer().equals(currentUser)) {
            vm.put(GAME_BOARD_ATR, game.getRedBoard());
        } else if (game.getWhitePlayer().equals(currentUser)) {
            vm.put(GAME_BOARD_ATR, game.getWhiteBoard());
        } else {
            // This should never occur, since if isPlayerInThisGame returns true, then currentUser must be
            // equal to either the red or white player.
            throw new IllegalStateException("isPlayerInThisGame returns true, but currentUser is not equal to" +
                    " either red or white player");
        }
    }

    /**
     * Render the WebCheckers Game page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");
        final Session httpSession = request.session();
        Map<String, Object> vm = new HashMap<>();


        // If the user is not signed in, redirect them back to the home page, since only signed-in users can play.
        Player currentUser = httpSession.attribute(GetHomeRoute.CURRENT_USER_KEY);
        if (currentUser == null) {
            response.redirect("/");
            halt();
            return null;
        }

        // Do we have a "opponent" query parameter?
        if (request.queryParams("opponent") != null) {
            // If so, we are starting a new game.
            final Player opponent = new Player(request.queryParams("opponent"));
            // check to make sure this name is actually a real username belonging to a user
            if (playerLobby.getUsers().contains(opponent)) {
                // check to make sure the person they are starting a game with isn't already in a game
                if (!playerLobby.userInGame(opponent)) {
                    int gameID = playerLobby.createGame(currentUser, opponent);
                    response.redirect("game?" + GAME_ID_ATR + "=" + gameID);
                } else {
                    // they requested to start a game with someone who is already in a game
                    // use the session variable to indicate an appropriate error message should be shown
                    httpSession.attribute(ATTRIBUTES.MESSAGE_ATR.getAtr(), opponent.getName() +
                            " is already in a game.");
                    httpSession.attribute(ATTRIBUTES.MESSAGE_TYPE_ATR.getAtr(),
                            ATTRIBUTES.MESSAGE_TYPE_ERROR_ATR.getAtr());
                    // redirect back to the home page to display the error
                    response.redirect(URLS.HOME_URL.getURL());
                }
            } else {
                // they requested to start a game with an invalid username.
                // There's nothing we can do here. Just redirect them home.
                response.redirect("/");
            }
            halt();
            return null;

        } else if (request.queryParams(GAME_ID_ATR) != null) {
            // Otherwise, do we have a "gameID" query parameter?
            // If so, we are continuing an existing game.

            // get the game in progress so we can determine whether or not to show the "game over" view
            int gameID;
            try {
                gameID = Integer.parseInt(request.queryParams(GAME_ID_ATR));
            } catch (NumberFormatException e) {
                // If the gameID query parameter is not a number, just redirect back home.
                response.redirect("/");
                halt();
                return null;
            }
            GameLobby game;

            game = playerLobby.getGame(gameID);
            if (game == null) {
                // If no game with this ID exists, just redirect them back home.
                response.redirect(URLS.HOME_URL.getURL());
                halt();
                return null;
            }

            // determine whether this player is actually one of the players of this game
            if (game.isPlayerInThisGame(currentUser)) {
                // This handles whether the game client should go into the "game over" view or not

                final Map<String, Object> modeOptions = new HashMap<>(2);
                // provide the appropriate game-over mode options depending on whether the game is done or not
                modeOptions.put("isGameOver", playerLobby.getGame(gameID).isGameDone());
                modeOptions.put("gameOverMessage", game.getGameOverReason());
                vm.put(MODE_OPTIONS_AS_JSON, gson.toJson(modeOptions));

                initGameInfo(vm, game, currentUser);

                vm.put(TITLE_ATR, "The Game Begins");

                // Until we implement other modes, the client is always in "PLAY" mode.
                vm.put(VIEW_MODE_ATR, view_mode);

                initBoard(vm, game, currentUser);

                return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
            } else {
                // if this player isn't one of the players of this game, just redirect them home
                response.redirect("/");
                halt();
                return null;
            }
        } else {
            // If we have neither an "opponent" nor a "gameID" query parameter, that's bad. We should just redirect
            // them back home.
            response.redirect("/");
            halt();
            return null;
        }
    }
}
