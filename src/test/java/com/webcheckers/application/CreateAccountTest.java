package com.webcheckers.application;

import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * This class tests the functions related to user accounts in the PlayerLobby
 *
 * @author Adam Densman
 */
@Tag("Application-Tier")
public class CreateAccountTest {

    private AccountManager accountManager;
    private Player player;
    private Player player2;
    private Player player3;
    private String password;
    private String incorrectPassword;

    @BeforeEach
    public void setup() {
        Request request = mock(Request.class);
        Session session = mock(Session.class);
        when(request.session()).thenReturn(session);
        accountManager = new AccountManager();
        player = new Player("player");
        player2 = new Player("player2");
        player3 = new Player("player3");
        password = "password";
        incorrectPassword = "incorrectPassword";
    }

    /**
     * Tests the ability to create an account
     */
    @Test
    public void testCreateAccount() {
        accountManager.addAccount(player.getName(), null);
        accountManager.addAccount(player2.getName(), password);
        accountManager.addAccount(player3.getName(), "");

        assertTrue(accountManager.accountExists(player.getName()), "accountExists false for registered account");
        assertTrue(accountManager.accountExists(player2.getName()), "accountExists false for registered account");
        assertTrue(accountManager.accountExists(player3.getName()), "accountExists false for registered account");

        assertTrue(accountManager.passwordCheck(player.getName(), null),
                "Player with no password fails passwordCheck");
        assertTrue(accountManager.passwordCheck(player.getName(), incorrectPassword),
                "Player with no password fails passwordCheck when any password is entered");
        assertTrue(accountManager.passwordCheck(player.getName(), ""),
                "Player with no password fails passwordCheck when empty password is entered");

        assertTrue(accountManager.passwordCheck(player2.getName(), password),
                "Player with password fails passwordCheck with correct password");
        assertFalse(accountManager.passwordCheck(player2.getName(), incorrectPassword),
                "Player with password passes passwordCheck with incorrect password");
        assertFalse(accountManager.passwordCheck(player2.getName(), ""),
                "Player with password passes passwordCheck when empty password is entered");
        assertFalse(accountManager.passwordCheck(player2.getName(), null),
                "Player with password passes passwordCheck when null password is entered");

        assertTrue(accountManager.passwordCheck(player3.getName(), null),
                "Player with no password fails passwordCheck");
        assertTrue(accountManager.passwordCheck(player3.getName(), incorrectPassword),
                "Player with no password fails passwordCheck when any password is entered");
        assertTrue(accountManager.passwordCheck(player3.getName(), ""),
                "Player with no password fails passwordCheck when empty password is entered");
    }

    /**
     * Tests the ability to change the username
     */
    @Test
    public void testChangeUsername() {
        String newValidUsername = "Ben";
        accountManager.addAccount(player.getName(), null);
        assertNotEquals(player.getName(), newValidUsername);
        accountManager.changeAccountUsername(player.getName(), newValidUsername);
        assertFalse(accountManager.accountExists(player.getName()));
        assertTrue(accountManager.accountExists(newValidUsername));
    }

    /**
     * Tests ability to change the password
     */
    @Test
    public void testChangePassword() {
        String oldPassword = "myBirthday";
        String newPassword = "myName";
        accountManager.addAccount(player.getName(), oldPassword);
        assertTrue(accountManager.passwordCheck(player.getName(), oldPassword));
        accountManager.changeAccountPassword(player.getName(), newPassword);
        assertTrue(accountManager.passwordCheck(player.getName(), newPassword));
        assertFalse(accountManager.passwordCheck(player.getName(), oldPassword));
    }

    /**
     * Tests ability to remove a password
     */
    @Test
    public void removePassword() {
        String password = "goodPassword";
        accountManager.addAccount(player.getName(), password);
        assertTrue(accountManager.passwordCheck(player.getName(), password));
        accountManager.changeAccountPassword(player.getName(), null);
        assertTrue(accountManager.passwordCheck(player.getName(), null));
    }

    /**
     * Tests ability to add password
     */
    @Test
    public void addPassword() {
        String password = "I_would_make_a_great_csec_student";
        accountManager.addAccount(player.getName(), null);
        assertTrue(accountManager.passwordCheck(player.getName(), null));
        accountManager.changeAccountPassword(player.getName(), password);
        assertTrue(accountManager.passwordCheck(player.getName(), password));
    }

    /**
     * Tests ability to delete account
     */
    @Test
    public void deleteAccount() {
        accountManager.addAccount(player.getName(), null);
        assertTrue(accountManager.accountExists(player.getName()));
        accountManager.deleteAccount(player.getName());
        assertFalse(accountManager.accountExists(player.getName()));
    }

    /**
     * Tests that checking the password of a nonexistent account fails
     */
    @Test
    public void testNonexistentAccount() {
        String username = "nonExistentAccount";
        // No account by this name should exist
        assertFalse(accountManager.accountExists(username));

        // When checking the password of an account that does not exist, passwordCheck should throw an exception,
        // since this violates its precondition that the provided username must belong to an account
        assertThrows(IllegalArgumentException.class, () -> {
                    accountManager.passwordCheck(username, "somePassword");
                },
                "passwordCheck does not throw exception for unregistered account name");
    }
}
