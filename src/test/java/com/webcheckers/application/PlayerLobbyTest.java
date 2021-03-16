package com.webcheckers.application;


import com.webcheckers.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the PlayerLobby in the application tier
 * @author Adam Densman ard7246
 */
public class PlayerLobbyTest {

    /**
     * tests the following functions: usernameValid, addUsername, getUsers, usernameAvailable
     */
    @Test
    public void testUsernames(){
        PlayerLobby playerLobby = new PlayerLobby();
        Player validUsername = new Player("Adam Densman");
        String invalidUsername = "!Adam?Densman*";
        String takenUsername = validUsername.getName();
        Player notTakenUsername = new Player("Not Adam Densman");
        assertTrue(UsernameValidator.usernameValid(validUsername.getName()));
        assertFalse(UsernameValidator.usernameValid(invalidUsername));
        playerLobby.addUsername(validUsername.getName());
        List<Player> list = playerLobby.getUsers();
        assertTrue(list.contains(validUsername));
        assertFalse(playerLobby.usernameAvailable(takenUsername));
        playerLobby.addUsername(notTakenUsername.getName());
        list = playerLobby.getUsers();
        assertTrue(list.contains(notTakenUsername) && list.contains(validUsername));
    }

    /**
     * Tests the following functions: createGame, userInGame, getGame, getOpponent
     */
    @Test
    public void testGame(){
        PlayerLobby playerLobby = new PlayerLobby();
        Player user = new Player( "Adam Densman" );
        Player opponent = new Player( "Evil Adam" );
        playerLobby.addUsername( user.getName() );
        playerLobby.addUsername(opponent.getName());
        assertEquals(0, playerLobby.createGame(user, opponent), "First game ID isn't zero");
        assertTrue(playerLobby.userInGame(user));
        assertTrue( playerLobby.userInGame( opponent ) );
        assertNotNull( playerLobby.getGame( user ) );
        assertEquals( playerLobby.getOpponent( user), opponent );


    }
}

