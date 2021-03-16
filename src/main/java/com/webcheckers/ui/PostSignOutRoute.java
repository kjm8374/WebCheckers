package com.webcheckers.ui;

import com.webcheckers.application.PlayerLobby;
import com.webcheckers.model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;
/**
 * GameLobby represents an individual game's data
 * <p>
 * A conglomeration of all the pertinent game information
 * </p>
 *
 * @author Halle Masaryk and Ben Coffta
 */

public class PostSignOutRoute implements Route {

    private final PlayerLobby playerLobby;
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    /**
     * This is the constructor that sets the private variable playerLobby to the
     * PlayerLobby being used
     *
     * @param playerLobby the PlayerLobby for the webcheckers game
     */
    PostSignOutRoute(PlayerLobby playerLobby) {
        this.playerLobby = playerLobby;
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
    }

    /**
     * This takes the user that wants to sign out ans removes tham from the hash map
     * of available players
     *
     * @param request  the request
     * @param response the response
     * @return null
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.finer("PostSignOutRoute is invoked");

        final Session session = request.session();

        // get the username that is signing out
        final Player player = session.attribute(GetHomeRoute.CURRENT_USER_KEY);
        final String name = player.getName();
        // remove the username from the set of signed-in users in the PlayerLobby
        playerLobby.signOutUser(name);

        // remove the username from the browser session, signing the user out
        session.removeAttribute(GetHomeRoute.CURRENT_USER_KEY);

        LOG.info(name + " just signed out.");

        // after signing out, redirect back to the home page
        response.redirect(URLS.HOME_URL.getURL());
        halt();
        return null;
    }

}
