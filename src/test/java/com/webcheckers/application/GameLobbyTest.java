package com.webcheckers.application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the functionality of gameLobby
 * @author Chris Piccoli
 */

public class GameLobbyTest {
    private Gson gson = new Gson();

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
    private GameLobby gameLobby = new GameLobby(42,
            new Player("Bobby"),
            new Player("Deep Thought"),
            new MoveValidator());


    /**
     * tests the following functions: usernameValid, addUsername, getUsers, usernameAvailable
     */
    @Test
    public void testPlayers() {
        Player bobby = new Player("Bobby");
        assertEquals(gameLobby.getRedPlayer(), bobby);
        assertEquals(gameLobby.getWhitePlayer(), new Player("Deep Thought"));
        assertTrue(gameLobby.isPlayerInThisGame(bobby));
        Player adam = new Player("Adam");
        assertFalse(gameLobby.isPlayerInThisGame(adam));
    }

    /**
     * Tests the game id is correct
     */
    @Test
    public void testGameID() {
        assertEquals(gameLobby.getGameID(), 42);
    }

    /**
     * test the active player
     */
    @Test
    public void testActive() {
        assertEquals(gameLobby.getActiveColor(), Piece.COLOR.RED);
        assertEquals(gameLobby.getActiveBoard(), gameLobby.getRedBoard());
        assertEquals(gameLobby.getActivePlayer(), gameLobby.getRedPlayer());

        gameLobby.setActivePlayer(Piece.COLOR.WHITE);

        assertEquals(gameLobby.getActiveColor(), Piece.COLOR.WHITE);
        assertEquals(gameLobby.getActiveBoard(), gameLobby.getWhiteBoard());
        assertEquals(gameLobby.getActivePlayer(), gameLobby.getWhitePlayer());
    }

    /**
     * Tests a game
     */
    @Test
    public void testsAGame() {
        GameLobby gameBlob = new GameLobby(24601, new Player("Jean ValJean"), new Player("Javier"), new MoveValidator());
        assertDoesNotThrow(() -> {
            for (Move themove : moves) {
                gameBlob.makeMove(themove);
            }
        }, "Playing a game throws an error");
        //Doesn't check gameover route
    }

    /**
     * Tests kinging pieces
     */
    @Test
    void kingPiecesTest(){
        Player redPlayer = new Player("Adam");
        Player whitePlayer = new Player("Evil Adam");
        GameLobby game = new GameLobby(42, redPlayer, whitePlayer,new MoveValidator());
        assertTrue(game.getRedBoard().pieceAt(1,2));
        game.makeMove(new Move(new Position(1,2),new Position(2,3)));
        assertTrue(game.getRedBoard().pieceAt(2,3));
        assertFalse(game.getRedBoard().isKing(2,3));
        for(int i = 0; i < 2;i++){
            for(int j = 0; j < 8; j++){
                game.getWhiteBoard().removePiece(i,j);
                game.getRedBoard().removePiece(i,j);
            }
        }
        game.makeMove(new Move(new Position(2,3),new Position(3,4)));
        game.makeMove(new Move(new Position(3,4),new Position(4,5)));
        game.makeMove(new Move(new Position(4,5),new Position(5,6)));
        game.makeMove(new Move(new Position(5,6),new Position(6,5)));
        game.makeMove(new Move(new Position(6,5),new Position(7,4)));
        assertTrue(game.getRedBoard().isKing(7,4));
    }

    /**
     * Tests making a move
     */
    @Test
    void makeMoveTest(){
        Player redPlayer = new Player("Adam");
        Player whitePlayer = new Player("Evil Adam");
        GameLobby game = new GameLobby(42, redPlayer, whitePlayer,new MoveValidator());
        assertTrue(game.getRedBoard().pieceAt(1,2));
        game.makeMove(new Move(new Position(1,2),new Position(2,3)));
        assertFalse(game.getRedBoard().pieceAt(1,2));
        assertTrue(game.getRedBoard().pieceAt(2,3));
    }

    /**
     * Tests various game over conditions, including winning or losing the game
     */
    @Test
    void GameOverTest(){
        assertFalse(gameLobby.isGameDone());
        assertEquals("The game is still in progress.",gameLobby.getGameOverReason());
        String gameOverReason = "Bobby won the game";
        gameLobby.markGameAsDone(gameOverReason);
        assertTrue(gameLobby.isGameDone());
        assertEquals(gameOverReason,gameLobby.getGameOverReason());

        GameLobby game2 = new GameLobby(42, new Player("Adam"), new Player("Evil Adam"),new MoveValidator());
        assertFalse(game2.checkForVictory());
    }

    /**
     * Tests the functions related to switching player turns
     */
    @Test
    void turnTest(){
        Player redPlayer = new Player("Adam");
        Player whitePlayer = new Player("Evil Adam");
        GameLobby game = new GameLobby(42, redPlayer, whitePlayer,new MoveValidator());
        assertEquals(redPlayer,game.getRedPlayer());
        assertEquals(whitePlayer,game.getWhitePlayer());
        assertEquals(redPlayer,game.getActivePlayer());
        assertEquals(whitePlayer,game.getInactivePlayer());
        game.endTurn();
        assertEquals(whitePlayer,game.getActivePlayer());
        assertEquals(redPlayer,game.getInactivePlayer());
        game.setActivePlayer(Piece.COLOR.RED);
        assertEquals(redPlayer,game.getActivePlayer());
        assertEquals(Piece.COLOR.RED,game.getActiveColor());
        assertEquals(Piece.COLOR.WHITE,game.getInactiveColor());
    }

    /**
     * Tests the getValidator function
     */
    @Test
    void getValidatorTest(){
        assertNotNull(gameLobby.getValidator());
    }
}
