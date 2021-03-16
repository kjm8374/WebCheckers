package com.webcheckers.application;

import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test the functions of GameReplay
 */
public class SaveReplayTest {

    @Test
    public void testConstructor() {
        GameLobby game = new GameLobby(42, new Player("Kirk"), new Player("Darth Vader"), new MoveValidator());
        GameReplay replay = new GameReplay(game);

        assertEquals(replay.getRedPlayer(), game.getRedPlayer());
        assertEquals(replay.getWhitePlayer(), game.getWhitePlayer());
        assertEquals(replay.getRedBoard().toString(), game.getRedBoard().toString());
        assertEquals(replay.getWhiteBoard().toString(), game.getWhiteBoard().toString());
        assertEquals(replay, game);
    }

    @Test
    public void testEmptyReplay() {
        GameLobby game = new GameLobby(42, new Player("Kirk"), new Player("Darth Vader"), new MoveValidator());
        GameReplay replay = new GameReplay(game);

        assertThrows(IndexOutOfBoundsException.class, replay::nextMove);
        assertThrows(IndexOutOfBoundsException.class, replay::lastMove);
    }

    @Test
    public void testMove() {
        GameLobby game = new GameLobby(15, new Player("Cinderella"), new Player("Jasmine"), new MoveValidator());

        game.makeMove(new Move(new Position(5, 2), new Position(4, 1)));
        GameReplay replay = new GameReplay(game);

        assertEquals(replay.getRedPlayer(), game.getRedPlayer());
        assertEquals(replay.getWhitePlayer(), game.getWhitePlayer());
        assertNotEquals(replay.getRedBoard().toString(), game.getRedBoard().toString());
        assertNotEquals(replay.getWhiteBoard().toString(), game.getWhiteBoard().toString());
        assertEquals(replay, game);

        //see if its right when we move forward
        replay.nextMove();

        assertEquals(replay.getRedBoard().toString(), game.getRedBoard().toString());
        assertEquals(replay.getWhiteBoard().toString(), game.getWhiteBoard().toString());

        // see if its right when we move back
        replay.lastMove();

        assertNotEquals(replay.getRedBoard().toString(), game.getRedBoard().toString());
        assertNotEquals(replay.getWhiteBoard().toString(), game.getWhiteBoard().toString());
    }

    @Test
    public void testSaveReplay() {
        GameLobby game = new GameLobby(42, new Player("Kirk"), new Player("Darth Vader"), new MoveValidator());
        game.markGameAsDone("This is a test");

        // make sure the replay is actually saved when a  game is finished
        assertDoesNotThrow(() -> {
            GameReplay replay = GameReplayManager.getReplay(game.getGameID(), new Player("Billy Bobby Brown"));
            assertNotNull(replay);
        }, "Threw an error while retriveing replay");
    }


}
