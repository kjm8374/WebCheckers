package com.webcheckers.application;

import java.util.HashMap;

/**
 * Keeps track of the accounts that have ever logged into the application. This is seperate from the users in
 * PlayerLobby, which are only the users that are currently logged in
 *
 * @author Adam Densman
 */
public class AccountManager {
    /**
     * A message stating that the password the user entered was incorrect.
     * <p>
     * This is intended to be used in error messages that occur when the user the wrong password for a given username.
     */
    public static final String WRONG_PASSWORD_MESSAGE = "Incorrect password. Please try again.";

    /**
     * all of the accounts, contains the username and the password if there is one
     */
    private HashMap<String, String> accounts;

    /**
     * Creates a new instance of AccountManager
     */
    public AccountManager() {
        this.accounts = new HashMap<>();
    }

    /**
     * Adds an account to accounts. Is called when a username is entered in the sign in page for the first time
     *
     * @param username the username of the account
     * @param password the password of the account
     */
    public void addAccount(String username, String password) {
        accounts.put(username, password);
    }

    /**
     * Changes the username of the account
     *
     * @param oldUsername the current username
     * @param newUsername the new username
     */
    public void changeAccountUsername(String oldUsername, String newUsername) {
        String password = accounts.get(oldUsername);
        addAccount(newUsername, password);
        deleteAccount(oldUsername);
    }

    /**
     * Changes the password to log into the account
     *
     * @param username    the current username
     * @param newPassword the new password
     */
    public void changeAccountPassword(String username, String newPassword) {
        accounts.remove(username);
        accounts.put(username, newPassword);
    }

    /**
     * Deletes the account from the accountManager
     *
     * @param username the username of the account to be deleted
     */
    public void deleteAccount(String username) {
        accounts.remove(username);
    }

    /**
     * Tests if the password entered into an existing account is correct
     *
     * @param username the username of the account
     * @param password the password being tested
     * @return true if the password is correct or the account has no password, otherwise false
     * @precondition An account with the specified username exists in the accounts HashMap
     */
    public boolean passwordCheck(String username, String password) {
        // An account with the specified username must exist in accounts
        if (accounts.containsKey(username)) {
            String correctPassword = accounts.get(username);
            // Does this account have a password?
            if (correctPassword != null && !correctPassword.isEmpty()) {
                // If this account has a password, compare it with the provided password.
                return correctPassword.equals(password);
            } else {
                // If this account doesn't have a password, then any password is correct.
                return true;
            }
        } else {
            // No account with the given username exists: this violates the precondition.
            throw new IllegalArgumentException("Username " + username + " is not registered in accounts HashMap");
        }
    }

    /**
     * Checks to see if the username is attached to an account in AccountManager
     *
     * @param username the name entered
     * @return true if the account already exists false otherwise
     */
    public boolean accountExists(String username) {
        return accounts.containsKey(username);
    }
}
