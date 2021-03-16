package com.webcheckers.ui;

import com.webcheckers.application.AccountManager;
import com.webcheckers.application.PlayerLobby;
import com.webcheckers.application.UsernameValidator;
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

/**
 * The UI controller to handle a user submitting a usernameAND password to sign in with.
 *
 * @author Kushal, Piccoli, Halle, Ben, Adam
 */
public class PostSignInRoute implements Route {

    //
    // Constants
    //

    static final String NAME_PARAM = "myName";
    static final String PASSWORD_PARAM = "myPassword";
    static final String VIEW_NAME = VIEWS.SIGNIN_VIEW.getView();
    static final String MESSAGE_ATR = ATTRIBUTES.MESSAGE_ATR.getAtr();
    static final String MESSAGE_TYPE_ATR = ATTRIBUTES.MESSAGE_TYPE_ATR.getAtr();

    //
    // Attributes
    //

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final AccountManager accountManager;
    static final String ERROR_TYPE = "error";
    static final String UNAVAILABLE_USERNAME = "Username is already taken.";
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /signin} route handler
     *
     * @param playerLobby    {@link PlayerLobby} that holds players
     * @param templateEngine template engine to use for rendering HTML page
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */

    PostSignInRoute(PlayerLobby playerLobby, AccountManager accountManager, TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.accountManager = accountManager;
    }

    /**
     * Handle the submission of the sign-in form.
     * <p>
     * Depending on whether the username is valid and not in use, this may render the sign-in page with an error
     * message, or it may redirect back to the home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for either the home page or the Sign In page
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.finer("PostSignInRoute is invoked.");
        final Map<String, Object> vm = new HashMap<>();
        vm.put(ATTRIBUTES.TITLE_ATR.getAtr(), "Welcome!");

        final Session session = request.session();

        //If the user is already logged in, redirect back to the home page
        if (session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr()) != null) {
            response.redirect(URLS.HOME_URL.getURL());
            halt();
            return null;
        } else { // The user is not logged in
            // Extract the name and password they entered into the form
            final String name = request.queryParams(NAME_PARAM);
            final String password = request.queryParams(PASSWORD_PARAM);
            // Check if the name they entered is valid and available
            boolean valid = UsernameValidator.usernameValid(name);
            boolean available = playerLobby.usernameAvailable(name);

            if (valid && available) {
                // The name they entered is valid and available, so register the player in the PlayerLobby and add
                // the Player object to the session

                if(!(accountManager.accountExists(name))){
                    accountManager.addAccount(name, password);
                } else {
                    if( !(accountManager.passwordCheck( name, password ) ) ){
                        // The name they entered is invalid
                        LOG.info(name + " failed to sign in because they entered the wrong password.");
                        vm.put(ATTRIBUTES.TITLE_ATR.getAtr(), GetSignInRoute.SIGNIN_MSG);
                        ModelAndView mv = signInError(vm, AccountManager.WRONG_PASSWORD_MESSAGE);
                        return templateEngine.render(mv);
                    }
                }
                playerLobby.addUsername(name);
                Player player = new Player(name);
                session.attribute(ATTRIBUTES.CURRENT_USER_ATR.getAtr(), player);

                LOG.info(name + " just signed in.");

                // Redirect to the home page after a successful signin
                response.redirect(URLS.HOME_URL.getURL());
                halt();
                return null;
            } else if (!valid) {
                // The name they entered is invalid
                LOG.info(name + " failed to sign in because that name is invalid.");
                vm.put(ATTRIBUTES.TITLE_ATR.getAtr(), GetSignInRoute.SIGNIN_MSG);
                ModelAndView mv = signInError(vm, UsernameValidator.USERNAME_REQUIREMENTS);
                return templateEngine.render(mv);
            } else {
                // The name they entered is already in use
                LOG.info(name + " failed to sign in because that name is already in use.");
                vm.put(ATTRIBUTES.TITLE_ATR.getAtr(), GetSignInRoute.SIGNIN_MSG);
                ModelAndView mv = signInError(vm, UNAVAILABLE_USERNAME);
                return templateEngine.render(mv);
            }
        }
    }

    /**
     * Private helper method to create a ModelAndView containing an error message.
     * <p>
     * TODO: Use the Message class for this, instead of just a string. This allows it to work with message.ftl.
     *
     * @param vm      the base view-model to add the error message to
     * @param message the error message
     * @return a ModelAndView containing the contents of the provided base view-model and the provided error message
     */
    private ModelAndView signInError(final Map<String, Object> vm, final String message) {
        vm.put(MESSAGE_ATR, message);
        vm.put(MESSAGE_TYPE_ATR, ERROR_TYPE);
        return new ModelAndView(vm, VIEW_NAME);
    }
}
