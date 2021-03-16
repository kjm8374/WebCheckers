package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.application.AccountManager;
import com.webcheckers.application.PlayerLobby;
import spark.TemplateEngine;

import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;


/**
 * The server that initializes the set of HTTP request handlers.
 * This defines the <em>web application interface</em> for this
 * WebCheckers application.
 *
 * <p>
 * There are multiple ways in which you can have the client issue a
 * request and the application generate responses to requests. If your team is
 * not careful when designing your approach, you can quickly create a mess
 * where no one can remember how a particular request is issued or the response
 * gets generated. Aim for consistency in your approach for similar
 * activities or requests.
 * </p>
 *
 * <p>Design choices for how the client makes a request include:
 * <ul>
 *     <li>Request URL</li>
 *     <li>HTTP verb for request (GET, POST, PUT, DELETE and so on)</li>
 *     <li><em>Optional:</em> Inclusion of request parameters</li>
 * </ul>
 * </p>
 *
 * <p>Design choices for generating a response to a request include:
 * <ul>
 *     <li>View templates with conditional elements</li>
 *     <li>Use different view templates based on results of executing the client request</li>
 *     <li>Redirecting to a different application URL</li>
 * </ul>
 * </p>
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class WebServer {
  private static final Logger LOG = Logger.getLogger(WebServer.class.getName());

  //
  // Constants
  //

  //
  // Attributes
  //

  private final TemplateEngine templateEngine;
  private final Gson gson;
    private final PlayerLobby playerLobby;
    private final AccountManager accountManager;

  //
  // Constructor
  //

    /**
     * The constructor for the Web Server.
     *
     * @param templateEngine The default {@link TemplateEngine} to render page-level HTML views.
     * @param gson           The Google JSON parser object used to render Ajax responses.
     * @param playerLobby    the lobby of players
     * @param accountManager manages the accounts of the players
     * @throws NullPointerException If any of the parameters are {@code null}.
     */
    public WebServer(final TemplateEngine templateEngine, final Gson gson, final PlayerLobby playerLobby, AccountManager accountManager) {
        this.accountManager = accountManager;
        // validation
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(gson, "gson must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        //
        this.templateEngine = templateEngine;
        this.gson = gson;
        this.playerLobby = playerLobby;
    }

  public Gson getGson() {
    return gson;
  }

  public PlayerLobby getPlayerLobby() {
    return playerLobby;
  }

  //
  // Public methods
  //

  /**
   * Initialize all of the HTTP routes that make up this web application.
   *
   * <p>
   * Initialization of the web server includes defining the location for static
   * files, and defining all routes for processing client requests. The method
   * returns after the web server finishes its initialization.
   * </p>
   */
  public void initialize() {

    // Configuration to serve static files
    staticFileLocation("/public");

    //// Setting any route (or filter) in Spark triggers initialization of the
    //// embedded Jetty web server.

    //// A route is set for a request verb by specifying the path for the
    //// request, and the function callback (request, response) -> {} to
    //// process the request. The order that the routes are defined is
    //// important. The first route (request-path combination) that matches
    //// is the one which is invoked. Additional documentation is at
    //// http://sparkjava.com/documentation.html and in Spark tutorials.

    //// Each route (processing function) will check if the request is valid
    //// from the client that made the request. If it is valid, the route
    //// will extract the relevant data from the request and pass it to the
    //// application object delegated with executing the request. When the
    //// delegate completes execution of the request, the route will create
    //// the parameter map that the response template needs. The data will
    //// either be in the value the delegate returns to the route after
    //// executing the request, or the route will query other application
    //// objects for the data needed.

    //// FreeMarker defines the HTML response using templates. Additional
    //// documentation is at
    //// http://freemarker.org/docs/dgui_quickstart_template.html.
    //// The Spark FreeMarkerEngine lets you pass variable values to the
    //// template via a map. Additional information is in online
    //// tutorials such as
    //// http://benjamindparrish.azurewebsites.net/adding-freemarker-to-java-spark/.

    //// These route definitions are examples. You will define the routes
    //// that are appropriate for the HTTP client interface that you define.
    //// Create separate Route classes to handle each route; this keeps your
    //// code clean; using small classes.

    // Shows the Checkers game Home page.
    get(URLS.HOME_URL.getURL(), new GetHomeRoute(templateEngine, playerLobby));

    //


    ///
    /// Game Play Routes
    ///

    get(URLS.GAME_URL.getURL(), new GetGameRoute(templateEngine, gson, playerLobby));

    // Handles waiting for opponents Move
    post(URLS.CHECK_TURN_URL.getURL(), new PostCheckTurnRoute(playerLobby));

    // Handles Move validation
    post(URLS.VALIDATE_MOVE_URL.getURL(), new PostValidateMoveRoute(playerLobby));

    // handles Submits a valid move
    post(URLS.SUBMIT_TURN_URL.getURL(), new PostSubmitTurnRoute(playerLobby));

    // handles backup move
    post(URLS.BACK_UP_MOVE_URL.getURL(), new PostBackUpMoveRoute(playerLobby));

    // Handles resignations
    post(URLS.RESIGN_GAME_URL.getURL(), new PostResignGameRoute(playerLobby));

    ///
    /// Replay Routes
    ///

    // Shows the replay game
    get(URLS.REPLAY_GAME_URL.getURL(), new GetReplayRoute(templateEngine, gson, playerLobby));

    // Ends the replay game
    get(URLS.STOP_REPLAY_GAME_URL.getURL(), new GetStopReplayRoute());

    // Shows the next move of the game
    post(URLS.REPLAY_NEXT_MOVE.getURL(), new PostReplayNextRoute());

    // Shows the next move of the game
    post(URLS.REPLAY_LAST_MOVE.getURL(), new PostReplayPreviousRoute());

    ///
    /// Sign in and account routes
    ///

    // Shows the Checkers Sign In Page.
    get(URLS.SIGN_IN_URL.getURL(), new GetSignInRoute(templateEngine));

    // Handles clicking the "Submit" button in the Sign In page.
    post(URLS.SIGN_IN_URL.getURL(), new PostSignInRoute(playerLobby, accountManager, templateEngine));
    post(URLS.SIGN_OUT_URL.getURL(), new PostSignOutRoute(playerLobby));

    //Handles clicking the "account details" button in the nav bar
    get(URLS.ACCOUNT_DETAILS_URL.getURL(), new GetAccountDetailsRoute(templateEngine));
    post(URLS.ACCOUNT_DETAILS_URL.getURL(), new PostAccountDetailsRoute(playerLobby, accountManager, templateEngine));

    LOG.config("WebServer is initialized.");
  }

}