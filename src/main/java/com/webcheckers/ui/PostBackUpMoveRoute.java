package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveValidator;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * Is used whenever backup move button is pressed
 * returns the board to its state one move prior if possible
 */
public class PostBackUpMoveRoute implements Route {

    private static final String GAMEID_PARAM = "gameID";
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /backupMove} route handler.
     *
     * @param playerLobby the current player lobby of the webserver
     */
    public PostBackUpMoveRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;

        LOG.config("PostBackUpMoveRoute is initialized.");
    }

    /**
     * Handles a request when the user clicks on back up.
     *
     * @param request  the http request sent by ajax
     * @param response the response http request
     * @return A JSON object of type INFO or ERROR
     */
    @Override
    public Object handle(Request request, Response response) {

        int gameID = Integer.parseInt(request.queryParams(GAMEID_PARAM));
        GameLobby game = playerLobby.getGame(gameID);
        MoveValidator validator = game.getValidator();

        if (game.isGameDone()) {
            JsonObject error = new JsonObject();
            error.addProperty("type", "ERROR");
            error.addProperty("text", " You can't backup a move because your opponent resigned. Click my home to enter a game.");
            return error;
        } else {
            return validator.undoMove(game);
        }
    }
}
