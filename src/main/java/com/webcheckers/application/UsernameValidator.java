package com.webcheckers.application;

import java.util.Objects;

/**
 * The UsernameValidator class contains static methods used to determine whether a username is valid or not.
 * <p>
 * A username is valid if:
 * * It begins with a capital letter
 * * Its length is at least twelve characters
 * * It does not end in a space
 * * It contains only alphanumeric characters and spaces
 *
 * @author Ben Coffta
 */
public class UsernameValidator {
    /**
     * An informational message containing the requirements for a valid username.
     * <p>
     * This is intended to be used in error messages that occur when the user enters an invalid username.
     */
    public static final String USERNAME_REQUIREMENTS = "Invalid username. Usernames must begin with a capital letter," +
            " be at least twelve characters in length, contain only alphanumeric characters and spaces, and end in a " +
            "character other than a space.";

    /**
     * Prevent instantiation of this class; it contains only static utility methods
     */
    private UsernameValidator() {
    }

    /**
     * Returns whether or not a username is valid.
     * <p>
     * A username is valid if:
     * * It begins with a capital letter
     * * Its length is at least twelve characters
     * * It does not end in a space
     * * It contains only alphanumeric characters and spaces
     *
     * @param userName the username to check
     * @return true if the given username is valid; false if the given username is not valid
     */
    public static boolean usernameValid(String userName) {
        Objects.requireNonNull(userName, "userName is required");
        // The username must consist of a capital letter, then any number of capital letters, lowercase letters,
        // digits, and spaces
        return userName.matches("^[A-Z][A-Za-z0-9 ]*$") &&
                // the username must be at least 12 characters long
                userName.length() >= 12 &&
                // the username must not end with a space
                !userName.endsWith(" ");
    }
}
