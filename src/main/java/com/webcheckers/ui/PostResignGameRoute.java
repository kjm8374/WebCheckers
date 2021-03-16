package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.logging.Logger;

/**
 * Is used when Resign game is pressed
 * Finds whether the game was resigned or not
 *
 * @author Ben, Kushal
 */
public class PostResignGameRoute implements Route {

    private static final String GAMEID_PARAM = "gameID";
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /resignGame} route handler
     *
     * @param playerLobby the webservers player lobby
     */
    public PostResignGameRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;

        LOG.config("PostResignGameRoute is initialized.");
    }

    /**
     * Handles a request when the resign button is clicked.
     *
     * @param request  http request from ajax
     * @param response http response from ajax
     * @return A JSON object of type ERROR if the user has already resigned and type info
     * if the user resigned successfully.
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");
        final Session session = request.session();

        int gameID = Integer.parseInt(request.queryParams(GAMEID_PARAM));

        Gson gson = new Gson();
        JsonObject resignGame = new JsonObject();
        Message message = gson.fromJson(resignGame, Message.class);
        response.body(message.toString());

        //Check if the game is done or resigned and notify the user that resign was unsuccessful.
        if (playerLobby.getGame(gameID).isGameDone()) {
            resignGame.addProperty("type", "ERROR");
            resignGame.addProperty("text", "This game is already over, because your opponent resigned.Click my home to enter a game. Click my home to enter a game.");
        } else {
            // read the username out of the session
            Player currentUser = session.attribute("currentUser");
            playerLobby.endGame(gameID, currentUser.getName() + " has resigned.");
            resignGame.addProperty("type", "INFO");
            resignGame.addProperty("text", "false");
        }

        return resignGame;
    }
}
