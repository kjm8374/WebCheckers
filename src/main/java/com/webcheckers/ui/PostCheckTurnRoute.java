package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.logging.Logger;

/**
 * Is used preiodically to check which
 * players turn it is
 *
 * @author Chris Piccoli , Kushal
 */
public class PostCheckTurnRoute implements Route {
    private static final String GAMEID_ATR = ATTRIBUTES.GAME_ID_ATR.getAtr();
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /checkTurn} route handler.
     *
     * @param playerLobby webservers current player lobby
     */
    public PostCheckTurnRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
        LOG.config("PostCheckTurnRoute is initialized.");
    }

    /**
     * @param request  an ajax request
     * @param response an ajax response
     * @return A JSON object of type INFO when the game is done or when the active player
     * is equal to the current player.
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostCheckTurnRoute is invoked.");
        final Session session = request.session();

        int gameID = Integer.parseInt(request.queryParams(GAMEID_ATR));

        GameLobby game = playerLobby.getGame(gameID);

        Player currentUser = session.attribute(GetHomeRoute.CURRENT_USER_KEY);

        //@TODO breaks the server if someone not logged in is at the game page
        JsonObject myTurn = new JsonObject();
        //if the opponent resigns then the game is done.
        if (game.isGameDone()) {
            myTurn.addProperty("type", "INFO");
            myTurn.addProperty("text", "true");
            // if the active player is the current user.
        } else if (game.getActivePlayer().equals(currentUser)) {
            myTurn.addProperty("type", "INFO");
            myTurn.addProperty("text", "true");
        } else {
            myTurn.addProperty("type", "INFO");
            myTurn.addProperty("text", "false");
        }

        return myTurn; // returns the JSON object.
        //return templateEngine.render(new ModelAndView(vm, GetGameRoute.VIEW_NAME));
    }
}
