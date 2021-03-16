package com.webcheckers.application;

import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The PlayerLobby class keeps track of currently signed-in players.
 *
 * @author Chris Piccoli, Halle Masaryk, Kushal Malhotra, Ben Coffta, Adam Densman
 */
public class PlayerLobby {

    private final HashMap<String, Player> users = new HashMap<>();
    private final HashMap<Player, Boolean> usersInGame = new HashMap<>();
    // List must be of only 2 players, the first is red, the second is white
    private final HashMap<Player, GameLobby> playersToGames = new HashMap<>();

    private final HashMap<Integer, GameLobby> gameIDtoGames = new HashMap<>();

    // Set it to negative one so gameIds start at 0, weird but go with it
    private int gameCounter = -1;

    /**
     * Returns whether or not a username is available.
     * <p>
     * A username is considered "available" if there is nobody else signed in with that username.
     *
     * @param userName the name to check
     * @return true if the given username is available; false if the given username is already in use
     */
    public boolean usernameAvailable(String userName) {
        return !users.containsKey(userName);
    }

    /**
     * Accessor method for the list of signed in players.
     *
     * @return List of signed in players.
     */
    public List<Player> getUsers() {
        return new ArrayList<>(users.values());
    }

    public Player getSingleUser(String username) {
        return users.get(username);
    }

    /**
     * Adds a username to the set of signed-in usernames.
     * <p>
     * This makes the username considered in-use and thus "unavailable" for use when signing in.
     *
     * @param name the name to add
     */
    public void addUsername(String name) {
        users.put(name, new Player(name));
        usersInGame.put(new Player(name), false);
    }

    /**
     * Checks if the user is in a Game
     *
     * @param user the person being checked
     * @return True if user is in a game, False otherwise
     */
    public boolean userInGame(Player user) {
        return usersInGame.getOrDefault(user, false);
    }

    /**
     * Add the players to a game
     *
     * @param user1 the challenger
     * @param user2 the challenged
     */
    public void addUserToGame(Player user1, Player user2) {
        usersInGame.put(user1, true);
        usersInGame.put(user2, true);
    }

    /**
     * Creates a game and assigns two players to the game
     *
     * @param redPlayer   the player that initiated the game, will be the red checkers
     * @param whitePlayer the player who recieved the game invitation, will be the white checkers
     * @return the game ID
     */
    public int createGame(Player redPlayer, Player whitePlayer) {

        gameCounter++;
        GameLobby game = new GameLobby(gameCounter, redPlayer, whitePlayer, new MoveValidator());
        gameIDtoGames.put(gameCounter, game);
        playersToGames.put(redPlayer, game);
        playersToGames.put(whitePlayer, game);

        addUserToGame(redPlayer, whitePlayer);

        return gameCounter;
    }

    /**
     * Updates all the hashmaps associated with the gameId
     *
     * @param gameId         the game ID of the game to end
     * @param gameOverReason String to display on the "game over" screen explaining why the game has ended.
     */
    public void endGame(int gameId, String gameOverReason) {
        GameLobby game = gameIDtoGames.get(gameId);
        game.markGameAsDone(gameOverReason);

        playersToGames.remove(game.getRedPlayer());
        usersInGame.remove(game.getRedPlayer());

        playersToGames.remove(game.getWhitePlayer());
        usersInGame.remove(game.getWhitePlayer());
    }

    /**
     * Gets the game a user is in
     *
     * @param user the player in question
     * @return the game the player is in
     */
    public GameLobby getGame(Player user) {
        return playersToGames.get(user);
    }

    /**
     * Gets the game through the game ID
     *
     * @param gameID the game ID
     * @return the game
     */
    public GameLobby getGame(int gameID) {
        return gameIDtoGames.get(gameID);
    }

    /**
     * Removes a user from the game
     *
     * @param user the player  to be removed
     */
    public void removeUserFromGame(String user) {
        Player player = users.get(user);
        usersInGame.put(player, false);
    }

    /**
     * Sign out a user, removing them from any games in progress and removing their name from the map of signed in
     * users.
     *
     * @param user the username to sign out
     */
    public void signOutUser(String user) {
        removeUserFromGame(user);
        users.remove(user);
    }

    /**
     * Gets a player's opponent
     *
     * @param user the player
     * @return the opponent
     */
    public Player getOpponent(Player user) {
        GameLobby game = playersToGames.get(user);
        if (user != game.getRedPlayer())
            return game.getRedPlayer();
        else
            return game.getWhitePlayer();
    }

}
