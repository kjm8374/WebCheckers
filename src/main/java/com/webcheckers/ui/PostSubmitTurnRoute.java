package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * is called when Submit Turn button is pressed
 * Calls functions to check if the turn is valid.
 *
 * @author Chris Piccoli, Kushal
 */
public class PostSubmitTurnRoute implements Route {
    private static final String GAMEID_PARAM = "gameID";
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /submitTurn} route handler
     *
     * @param playerLobby current player lobby of the webserver
     */
    public PostSubmitTurnRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    /**
     * Handles a request when the resign button or when the submit button is clicked.
     *
     * @param request  ajax request
     * @param response ajax response
     * @return A JSON object of type INFO if the game is done or if the active user has taken it's turn.
     */
    @Override
    public Object handle(Request request, Response response) {


        int gameID = Integer.parseInt(request.queryParams(GAMEID_PARAM));
        GameLobby game = playerLobby.getGame(gameID);

        MoveValidator validator = game.getValidator();

        Gson gson = new Gson();

        if (game.isGameDone()) {
            // If the game is already over, just indicate the turn was successful to cause the page to refresh.
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type", "INFO");
            jsonObject.addProperty("text", "The game is already over.");
            return jsonObject;
        } else {         // if the move is valid change the current user to inactive and the opponent to active
            JsonObject isTurnValid = validator.ValidateTurn(game.getRedBoard(), game.getWhiteBoard(), game.getActiveColor());
            Message message = gson.fromJson(isTurnValid, Message.class);

            if (isTurnValid.get("type").getAsString().equals("INFO")) {
                game.endTurn();
            }

            if (game.checkForVictory())
                playerLobby.endGame(gameID, game.getGameOverReason());
            response.body(message.toString());

            return isTurnValid;
        }

    }
}
