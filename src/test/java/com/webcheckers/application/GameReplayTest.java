package com.webcheckers.application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webcheckers.model.BoardView;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameReplayTest {
    private GameLobby gameLobby;
    private Gson gson = new Gson();
    private GameReplay emptyReplay;
    private GameReplay nonEmptyReplay;

    /// this is just a list of moves of a game I played
    Type listMoveType = new TypeToken<List<Move>>() {
    }.getType();
    /// it was a little painful to get
    private List<Move> moves = gson.fromJson(
            "[{\"start\":{\"row\":5,\"cell\":6},\"end\":{\"row\":4,\"cell\":7}},{\"start\"" +
                    ":{\"row\":2,\"cell\":1},\"end\":{\"row\":3,\"cell\":0}},{\"start\":{\"row\":5,\"cell\":0},\"end\"" +
                    ":{\"row\":4,\"cell\":1}},{\"start\":{\"row\":2,\"cell\":5},\"end\":{\"row\":3,\"cell\":4}},{\"start\"" +
                    ":{\"row\":6,\"cell\":5},\"end\":{\"row\":5,\"cell\":6}},{\"start\":{\"row\":1,\"cell\":4},\"end\"" +
                    ":{\"row\":2,\"cell\":5}},{\"start\":{\"row\":7,\"cell\":4},\"end\":{\"row\":6,\"cell\":5}},{\"start\"" +
                    ":{\"row\":0,\"cell\":3},\"end\":{\"row\":1,\"cell\":4}},{\"start\":{\"row\":6,\"cell\":1},\"end\"" +
                    ":{\"row\":5,\"cell\":0}},{\"start\":{\"row\":2,\"cell\":5},\"end\":{\"row\":3,\"cell\":6}},{\"start\"" +
                    ":{\"row\":4,\"cell\":7},\"end\":{\"row\":2,\"cell\":5}},{\"start\":{\"row\":2,\"cell\":5},\"end\"" +
                    ":{\"row\":0,\"cell\":3}},{\"start\":{\"row\":0,\"cell\":3},\"end\":{\"row\":2,\"cell\":1}}]",
            listMoveType);

    private void setup() {
        emptyReplay = new GameReplay(new GameLobby(42, new Player("Jeff"), new Player("Dunham"), new MoveValidator()));
        gameLobby = new GameLobby(42,
                new Player("Bobby"),
                new Player("Deep Thought"),
                new MoveValidator());
        gameLobby.makeMove(new Move(new Position(5, 0), new Position(4, 1)));
        nonEmptyReplay = new GameReplay(gameLobby);
    }

    /**
     * tests the following functions: usernameValid, addUsername, getUsers, usernameAvailable
     */
    @Test
    public void testPlayers() {
        setup();
        assertEquals(nonEmptyReplay.getRedPlayer(), gameLobby.getRedPlayer());
        assertEquals(nonEmptyReplay.getWhitePlayer(), gameLobby.getWhitePlayer());
    }

    /**
     * Tests the game id is correct
     */
    @Test
    public void testGameID() {
        setup();
        assertEquals(nonEmptyReplay.getGameID(), gameLobby.getGameID());
    }

    /**
     * Test that the lobby and replay are equal
     */
    @Test
    public void testEquals() {
        setup();
        assertEquals(nonEmptyReplay, gameLobby, "Game Replay is not equal to Game Lobby");
    }

    /**
     * Tests checking for next moves
     */
    @Test
    public void testhasNext() {
        setup();
        assertFalse(emptyReplay.hasNext(), "Returns true, but gamereplay is empty");
        assertTrue(nonEmptyReplay.hasNext(), "Returns false, but replay has next move");
    }

    /**
     * Test nextMove(), previousMove()
     * Checks if the board is updated correctly
     */
    @Test
    public void testBoardStates() {
        setup();
        assertNotEquals(nonEmptyReplay.getRedBoard(), gameLobby.getRedBoard());
        assertEquals(nonEmptyReplay.getRedBoard(), new BoardView(Piece.COLOR.RED));
        nonEmptyReplay.nextMove();
        assertEquals(nonEmptyReplay.getRedBoard(), gameLobby.getRedBoard());
        nonEmptyReplay.lastMove();
        assertEquals(nonEmptyReplay.getRedBoard(), new BoardView(Piece.COLOR.RED));
        assertNotEquals(nonEmptyReplay.getRedBoard(), gameLobby.getRedBoard());
    }

    /**
     * Test an entire game from json
     */
    @Test
    public void testGame() {
        GameLobby game = new GameLobby(24601, new Player("Jean ValJean"), new Player("Javier"), new MoveValidator());

        for (Move move : moves) {
            game.makeMove(move);
        }
        GameReplay gameReplay = new GameReplay(game);

        ArrayList<BoardView> boardViews = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            gameReplay.nextMove();
            boardViews.add(gameReplay.getRedBoard());
        }
        for (int i = moves.size() - 1; i >= 0; i--) {
            gameReplay.lastMove();
            assertEquals(gameReplay.getRedBoard(), boardViews.get(i));
        }
    }

    /**
     * Test checking for previous moves
     */
    @Test
    public void testhasPrevious() {
        setup();
        assertFalse(emptyReplay.hasPrevious(), "returns true but gamereplay is empty");
        assertFalse(nonEmptyReplay.hasPrevious(), "Returns true, but replay is at start");
        nonEmptyReplay.nextMove();
        assertTrue(nonEmptyReplay.hasPrevious(), "Returns false, but replay has previous");
    }
}
