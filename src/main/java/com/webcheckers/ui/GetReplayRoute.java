package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.GameReplay;
import com.webcheckers.application.GameReplayManager;
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

public class GetReplayRoute implements Route {

    protected static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    protected final TemplateEngine templateEngine;
    protected final Gson gson;
    protected PlayerLobby playerLobby;

    protected static final GetGameRoute.VIEW_MODES VIEW_MODE = GetGameRoute.VIEW_MODES.REPLAY;

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
    public GetReplayRoute(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.gson = Objects.requireNonNull(gson, "gson is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        LOG.config("GetReplayRoute is initialized: type ");
    }

    /**
     * Adds stuff to the vm put specifying the board
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
     * @param vm   the map
     * @param game the gamelobby
     */
    protected void initBoard(Map<String, Object> vm, GameLobby game) {
        // put the appropriate game board into the view-model
        vm.put(GAME_BOARD_ATR, game.getRedBoard());
    }

    /**
     * Handles the render for a replay route
     *
     * @param request  the server request
     * @param response the server response
     * @return the rendered page
     */
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayRoute is invoked.");
        final Session httpSession = request.session();
        Map<String, Object> vm = new HashMap<>();


        // If the user is not signed in, redirect them back to the home page,
        // since only signed-in users can watch replays.
        Player currentUser = httpSession.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr());
        if (currentUser == null) {
            response.redirect(URLS.HOME_URL.getURL());
            halt();
            return null;
        }

        int gameID;
        if (request.queryParams("gameID") == null) {
            // If they didn't specify a gameID query parameter, just redirect back home.
            response.redirect(URLS.HOME_URL.getURL());
            halt();
            return null;
        } else {
            try {
                gameID = Integer.parseInt(request.queryParams(GAME_ID_ATR));
            } catch (NumberFormatException e) {
                // If the gameID query parameter is not a number, just redirect back home.
                response.redirect(URLS.HOME_URL.getURL());
                halt();
                return null;
            }
        }
        // initialize the game
        GameReplay game = GameReplayManager.getReplay(gameID, currentUser);

        final Map<String, Object> modeOptions = new HashMap<>(2);
        // provide the appropriate game-over mode options depending on whether the game is done or not
        modeOptions.put("hasNext", game.hasNext());
        modeOptions.put("hasPrevious", game.hasPrevious());
        vm.put(MODE_OPTIONS_AS_JSON, gson.toJson(modeOptions));

        vm.put(VIEW_MODE_ATR, VIEW_MODE);

        initGameInfo(vm, game, currentUser);
        initBoard(vm, game);


        vm.put(TITLE_ATR, "The Game Has Ended, but here you are.");

        return templateEngine.render(new ModelAndView(vm, VIEW_NAME));
    }
}
