package com.webcheckers.ui;

import com.webcheckers.application.GameReplayManager;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import static spark.Spark.halt;

public class GetStopReplayRoute implements Route {
    /**
     * Handles a request when the user clicks exit.
     * Redirects home
     *
     * @param request  the http request sent by ajax
     * @param response the response http request
     * @return null
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        final Session session = request.session();

        Integer gameID = Integer.valueOf(request.queryParams(ATTRIBUTES.GAME_ID_ATR.getAtr()));
        Player currentUser = session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr());

        GameReplayManager.endReplay(gameID, currentUser);

        response.redirect(URLS.HOME_URL.getURL());
        halt("Redirected Home");
        return null;
    }
}
