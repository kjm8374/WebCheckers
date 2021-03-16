package com.webcheckers.ui;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameReplay;
import com.webcheckers.application.GameReplayManager;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

public class PostReplayNextRoute implements Route {

    public PostReplayNextRoute() {
    }

    /**
     * Handles a request when the user clicks next.
     *
     * @param request  the http request sent by ajax
     * @param response the response http request
     * @return A JSON object of type INFO or ERROR
     */
    @Override
    public Object handle(Request request, Response response) {

        Session session = request.session();

        int gameID = Integer.parseInt(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr()));
        Player currentUser = session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr());

        GameReplay game = GameReplayManager.getReplay(gameID, currentUser);
        JsonObject action = new JsonObject();

        action.addProperty("type", "INFO");
        if (game.hasNext()) {
            action.addProperty("text", "true");
            game.nextMove();
        } else {
            action.addProperty("text", "false");
        }


        return action;
    }
}
