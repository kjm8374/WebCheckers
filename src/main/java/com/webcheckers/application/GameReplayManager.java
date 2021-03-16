package com.webcheckers.application;

import com.webcheckers.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameReplayManager {
    private static HashMap<Integer, GameReplay> playedGames = new HashMap<>();
    private static HashMap<Player, HashMap<Integer, GameReplay>> activeReplays = new HashMap<>();

    private static void newReplay(int gameID, Player current) {
        HashMap<Integer, GameReplay> temp = new HashMap<>();
        temp.put(gameID, new GameReplay(playedGames.get(gameID)));
        activeReplays.put(current, temp);
    }

    /**
     * Gets the game in a replay form using its unique Identifier
     * This includes making a copy of it for simultaneous viewings
     *
     * @param gameID unique identifier for that game
     * @return the game that has the specified Game ID
     */
    public static GameReplay getReplay(int gameID, Player current) {
        // check if the user exists already, them make sure the game in question exists
        if (!activeReplays.containsKey(current) || !activeReplays.get(current).containsKey(gameID))
            newReplay(gameID, current);
        return activeReplays.get(current).get(gameID);
    }

    /**
     * Ends the replay by getting rid of the data of the replay
     *
     * @param gameID  the gameID of the replay
     * @param current the user watching the replay
     */
    public static void endReplay(int gameID, Player current) {
        activeReplays.get(current).remove(gameID);
        activeReplays.remove(current);
    }

    /**
     * Puts the game into a map of all games played on the server
     *
     * @param gamePlayed the game to store
     */
    public static void saveReplay(GameLobby gamePlayed) {
        playedGames.put(gamePlayed.getGameID(), new GameReplay(gamePlayed));
    }

    /**
     * Used to get an immutable List of the replays
     *
     * @return a List of playedGames that holds Game Replays
     */
    public static List<GameReplay> toList() {
        return new ArrayList<>(playedGames.values());
    }
}
