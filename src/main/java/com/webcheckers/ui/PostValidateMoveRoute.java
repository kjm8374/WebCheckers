package com.webcheckers.ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Position;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.logging.Logger;

/**
 * Is called when a player makes a move on their turn.
 *
 * @author Chris Piccoli, Kushal
 */
public class PostValidateMoveRoute implements Route {

    private static final String GAMEID_PARAM = "gameID";
    private static final String ACTION_DATA_PARAM = "actionData";

    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    private PlayerLobby playerLobby;

    /**
     * The constructor for the {@code POST /validateMove} route handler
     *
     * @param playerLobby the current playerlobby of the webserver
     */
    public PostValidateMoveRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;

        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * Handles a request when the resign button is clicked or when user makes a valid move.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return A JSON object of type ERROR when the game is done or of type INFO when it's a valid move
     **/
    @Override
    public Object handle(Request request, Response response) {

        int gameID = Integer.parseInt(request.queryParams(GAMEID_PARAM));
        GameLobby game = playerLobby.getGame(gameID);


        Gson gson = new Gson();

        // if game is done or if the resign button is clicked on, notify the user that resign is unsuccessful.
        if (game.isGameDone()) {
            JsonObject error = new JsonObject();
            error.addProperty("type", "ERROR");
            error.addProperty("text", " You can't make a move because your opponent already resigned. Click my home to enter a game.");
            return error;
        } else { // if the game is not resigned then validate the move the user makes.
            Move move = gson.fromJson(request.queryParams(ACTION_DATA_PARAM), Move.class);
            BoardView playerBoard = game.getActiveBoard();
            MoveValidator validator = game.getValidator();
            JsonObject isMoveValid = validator.ValidateMove(move, game.getActiveColor(), playerBoard);
            Message message = gson.fromJson(isMoveValid, Message.class);

            // it should have INFO if it is valid
            // if it is valid update the board
            if (move.getEnd().equals(new Position(0, 0)))
                return isMoveValid;
            if (isMoveValid.get("type").getAsString().equals("INFO"))
                game.makeMove(move);

            response.body(message.toString());

            return isMoveValid;
        }
    }
}
