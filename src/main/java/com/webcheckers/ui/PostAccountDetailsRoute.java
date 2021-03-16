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
 * The UI controller to handle a user submitting a username to sign in with.
 *
 * @author Adam Densman
 */
public class PostAccountDetailsRoute implements Route {

    //
    // Constants
    //

    static final String NAME_PARAM = "myName";
    static final String PASSWORD_PARAM = "myPassword";
    static final String DELETE_ACCOUNT = "deleteAccount";
    static final String VIEW_NAME = VIEWS.ACCOUNT_VIEW.getView();
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
    static final String INCORRECT_USERNAME = "Incorrect username, cannot confirm account deletion";
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /account-details} route handler
     *
     * @param playerLobby    {@link PlayerLobby} that holds players
     * @param templateEngine template engine to use for rendering HTML page
     * @throws NullPointerException when the {@code playerLobby} or {@code templateEngine} parameter is null
     */

    PostAccountDetailsRoute(PlayerLobby playerLobby, AccountManager accountManager, TemplateEngine templateEngine) {
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        this.templateEngine = templateEngine;
        this.playerLobby = playerLobby;
        this.accountManager = accountManager;
    }

    /**
     * Handle the submission of the account-details form.
     * <p>
     * Depending on whether the username is valid and not in use, this may render the account-details page with an error
     * message, or it may redirect back to the home page.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for either the home page or the Sign In page
     */
    @Override
    public String handle(Request request, Response response) {
        LOG.finer("PostAccountDetailsRoute is invoked.");
        final Map<String, Object> vm = new HashMap<>();
        vm.put(GetHomeRoute.TITLE_ATR, "Welcome!");

        final Session session = request.session();

        //If the user is not already logged in, redirect back to the home page
        if (session.attribute(GetHomeRoute.CURRENT_USER_KEY) == null) {
            response.redirect("/");
            halt();
            return null;
        } else { // The user is logged in
            // Extract the name and password they entered into the form
            final Player currentPlayer = (session.attribute(GetHomeRoute.CURRENT_USER_KEY));
            final String currentName = currentPlayer.getName();
            String name = request.queryParams(NAME_PARAM);
            if (name == null || name.equals("")) {
                name = currentName;
            }
            final String password = request.queryParams(PASSWORD_PARAM);
            final String deleteAccount = request.queryParams(DELETE_ACCOUNT);
            if (deleteAccount == null || deleteAccount.equals("")) {
                boolean valid = true;
                boolean available = true;
                if (name != null) {
                    if (!(name.equals(currentName))) {
                        valid = UsernameValidator.usernameValid(name);
                        available = playerLobby.usernameAvailable(name);
                    }
                }
                if (valid && available) {
                    // The name they entered is valid and available, so register the player in the PlayerLobby and add
                    // the Player object to the session
                    Player player = null;
                    if (name != null && !(name.equals(currentName))) {
                        playerLobby.signOutUser(currentName);
                        accountManager.changeAccountUsername(currentName, name);
                        player = new Player(name);
                        playerLobby.addUsername(name);
                    }
                    if (password != null && !password.equals("")) {
                        accountManager.changeAccountPassword(name, password);
                    }
                    if (player == null) {
                        session.attribute(GetHomeRoute.CURRENT_USER_KEY, currentPlayer);
                    } else {
                        session.attribute(GetHomeRoute.CURRENT_USER_KEY, player);
                    }
                    LOG.info(name + " changed their account information in.");

                    // Redirect to the home page after a successful signin
                    response.redirect("/");
                    halt();
                    return null;
                } else if (!valid) {
                    // The name they entered is invalid
                    LOG.info(name + " failed to change username because that name is invalid.");
                    vm.put(GetAccountDetailsRoute.TITLE_ATR, "Welcome Back!");
                    vm.put(GetAccountDetailsRoute.INFO_ATR, name);
                    ModelAndView mv = signInError(vm, UsernameValidator.USERNAME_REQUIREMENTS);
                    return templateEngine.render(mv);
                } else {
                    // The name they entered is already in use
                    LOG.info(name + " failed to change username because that name is already in use.");
                    vm.put(GetSignInRoute.TITLE_ATR, "Welcome Back!");
                    ModelAndView mv = signInError(vm, UNAVAILABLE_USERNAME);
                    return templateEngine.render(mv);
                }
            } else {
                if(!(deleteAccount.equals(currentName))){
                    LOG.info("Incorrect username, cannot confirm account deletion");
                    vm.put(GetAccountDetailsRoute.TITLE_ATR, "Welcome Back!");
                    vm.put(GetAccountDetailsRoute.INFO_ATR, name);
                    ModelAndView mv = signInError(vm, INCORRECT_USERNAME);
                    return templateEngine.render(mv);
                } else {
                    playerLobby.signOutUser(currentName);
                    accountManager.deleteAccount(currentName);
                    session.removeAttribute(GetHomeRoute.CURRENT_USER_KEY);
                    LOG.info(name + " just signed out.");
                    LOG.info(currentName + " deleted their account");
                    response.redirect("/");
                    halt();
                    return null;
                }
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
