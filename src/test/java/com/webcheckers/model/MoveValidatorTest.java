package com.webcheckers.model;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Chris Piccoli, Adam Densman
 */
public class MoveValidatorTest {
    private static JsonObject valid = new JsonObject();
    private static JsonObject invalid = new JsonObject();

    public MoveValidatorTest() {
        valid.addProperty("type", "INFO");
        invalid.addProperty("type", "ERROR");
    }

    // I should change this implementation of MoveValidator but Ill do that after this is done
    // Sorry snap decision was bad decision also this class is not well defined yet so we should get on that.

    /**
     * Test if it accepts valid forwards move
     */
    @Test
    public void testMoveForward() {
        Move validMove = new Move(new Position(1, 3), new Position(0, 2));
        MoveValidator moveValidator = new MoveValidator();

        assertEquals(valid.get("type"), (moveValidator.ValidateMove(validMove, Piece.COLOR.RED, new BoardView(Piece.COLOR.RED)).get("type")),
                "MoveValidator returned the Error instead of INFO, for valid move");
    }

    /**
     * Test if it rejects invalid backwards move for non king piece
     */
    @Test
    public void testMoveBackward() {
        Move invalidMove = new Move(new Position(0, 2), new Position(1, 3));
        MoveValidator moveValidator = new MoveValidator();

        assertEquals(invalid.get("type"), moveValidator.ValidateMove(invalidMove, Piece.COLOR.RED, new BoardView(Piece.COLOR.RED)).get("type"),
                "MoveValidator returned Info instead of Error, for backward move");
    }



    /**
     * Test if Kings can move Backward, and that it cannot move vertically or horizontally
     */
    @Test
    public void testingKingMoves() {

        ArrayList<Position> positions = new ArrayList<>();

        Move invalidMoveOne = new Move(new Position(7, 1), new Position(7, 2));

        Move invalidMoveTwo = new Move(new Position(7, 1), new Position(6, 1));

        Move validMoveOne = new Move(new Position(7, 1), new Position(6, 2));

        Move validMoveTwo = new Move(new Position(7, 1), new Position(6, 0));

        Move backwardValidMoveOne = new Move(new Position(6, 0), new Position(7, 1));

        Move backwardValidMoveTwo = new Move(new Position(6, 2), new Position(7, 1));
        MoveValidator moveValidator = new MoveValidator();

        assertEquals(valid.get("type"), (moveValidator.ValidateMove(validMoveOne, Piece.COLOR.RED, createKingOnlyBoard()).get("type")),
                "MoveValidator returned Error instead of info, when King moves right back");
        MoveValidator moveValidator2 = new MoveValidator();

        assertEquals(valid.get("type"), (moveValidator2.ValidateMove(validMoveTwo, Piece.COLOR.RED, createKingOnlyBoard()).get("type")),
                "MoveValidator returned Error instead of info, when King moves left back");
    }

    /**
     * Tests the jump criteria
     */
    @Test
    public void testingSingleJump() {


        Move validJumpOne = new Move(new Position(7, 1), new Position(6, 0));
        Move invalidJumpOne = new Move(new Position(7, 1), new Position(5, 3));

        System.out.print(createJumpOnly());
        MoveValidator moveValidator = new MoveValidator();
        assertEquals(valid.get("type"), (moveValidator.ValidateMove(validJumpOne, Piece.COLOR.WHITE, createJumpOnly()).get("type")),
                "MoveValidator returned info instead of Error, jump not mandatory");
        assertEquals(invalid.get("type"), (moveValidator.ValidateMove(invalidJumpOne, Piece.COLOR.WHITE, createJumpOnly()).get("type")),
                "MoveValidator returned Error instead of info, jump is legal");
    }

    /**
     * Testing double jump criteria
     */
    @Test
    public void testingDoubleJump() {


        Move validMoveOne = new Move(new Position(7, 1), new Position(5, 3));
        Move validMoveTwo = new Move(new Position(5, 3), new Position(3, 1));

        BoardView doubleJumpBoard = createDoubleJump();
        System.out.print(doubleJumpBoard);

        MoveValidator moveValidator = new MoveValidator();
        assertEquals(valid.get("type"), (moveValidator.ValidateMove(validMoveOne, Piece.COLOR.WHITE, doubleJumpBoard).get("type")),
                "MoveValidator returned ERROR instead of INFO, jump is legal");
        doubleJumpBoard.makeMove(validMoveOne);
        assertEquals(valid.get("type"), (moveValidator.ValidateMove(validMoveTwo, Piece.COLOR.WHITE, createDoubleJump()).get("type")),
                "MoveValidator returned Error instead of info, double jump should be legal");
    }

    /**
     * Creates a specific board state with one king on the back row for the tester
     *
     * @return board with 1 king
     */
    private BoardView createKingOnlyBoard() {
        List<Row> Rows = new ArrayList<>();
        ArrayList<Space> empty = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            empty.add(new Space(i, 0));
        }
        for (int i = 0; i < 7; i++)
            Rows.add(new Row(empty));

        ArrayList<Space> backRank = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i != 1)
                backRank.add(new Space(i, 0));
            else {
                backRank.add(new Space(i, Piece.COLOR.RED));
                backRank.get(i).getPiece().kingPiece();
            }
        }

        Rows.add(new Row(backRank));
        return new BoardView(Rows);
    }

    /**
     * Creates a specific board state with two pieces for the jump
     *
     * @return board with mandatory jump
     */
    private BoardView createJumpOnly() {
        List<Row> Rows = new ArrayList<>();
        ArrayList<Space> empty = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            empty.add(new Space(i, 0));
        }
        for (int i = 0; i < 6; i++)
            Rows.add(new Row(empty));

        ArrayList<Space> secondBack = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i != 2)
                secondBack.add(new Space(i, 0));
            else {
                secondBack.add(new Space(i, Piece.COLOR.RED));
            }
        }

        Rows.add(new Row(secondBack));

        ArrayList<Space> backRank = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i != 1)
                backRank.add(new Space(i, 0));
            else {
                backRank.add(new Space(i, Piece.COLOR.WHITE));
            }
        }

        Rows.add(new Row(backRank));
        return new BoardView(Rows);
    }

    /**
     * Creates a specific board state with three pieces with mandatory triple jump
     *
     * @return board with mandatory double jump
     */
    private BoardView createDoubleJump() {
        List<Row> Rows = new ArrayList<>();
        ArrayList<Space> empty = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            empty.add(new Space(i, 0));
        }
        for (int i = 0; i < 4; i++)
            Rows.add(new Row(empty));

        ArrayList<Space> secondBack = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i != 2)
                secondBack.add(new Space(i, 0));
            else {
                secondBack.add(new Space(i, Piece.COLOR.RED));
            }
        }

        Rows.add(new Row(secondBack));
        Rows.add(new Row(empty));
        Rows.add(new Row(secondBack));

        ArrayList<Space> backRank = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (i != 1)
                backRank.add(new Space(i, 0));
            else {
                backRank.add(new Space(i, Piece.COLOR.WHITE));
            }
        }

        Rows.add(new Row(backRank));
        return new BoardView(Rows);
    }

    /**
     * Tests the moveValidator undoMove function
     * Note: gameLobby's board is never updated if a move is made and then undone, so
     * this cannot be tested by simply checking if the piece is no longer at the spot
     * the move was made to
     */
    @Test
    void undoMoveTest(){
        Player redPlayer = new Player("Adam");
        Player whitePlayer = new Player("Evil Adam");
        MoveValidator validator = new MoveValidator();
        GameLobby game = new GameLobby(10,redPlayer,whitePlayer,validator);
        assertTrue(game.getRedBoard().pieceAt(5,0));
        Position start = new Position(5,0);
        Position end = new Position(4,1);
        game.makeMove(new Move(start,end));
        assertFalse(game.getRedBoard().pieceAt(5,0));
        assertTrue(game.getRedBoard().pieceAt(4,1));
        validator.undoMove(game);
    }

    /**
     * Tests the validateMove and validateTurn functions
     */
    @Test
    void validateTest(){
        Player redPlayer = new Player("Adam");
        Player whitePlayer = new Player("Evil Adam");
        MoveValidator validator = new MoveValidator();
        GameLobby game = new GameLobby(10,redPlayer,whitePlayer, validator);
        assertTrue(game.getRedBoard().pieceAt(5,0));
        Position start = new Position(5,0);
        Position end = new Position(4,1);
        Move validMove = new Move(start,end);
        game.makeMove(validMove);
        JsonObject isMoveValid = validator.ValidateMove(validMove, Piece.COLOR.RED,game.getRedBoard());
        String expectedOutput = "Yay a valid move.";
        assertEquals(expectedOutput,isMoveValid.get("text").getAsString());
        JsonObject validTurn = validator.ValidateTurn(game.getRedBoard(),game.getWhiteBoard(), Piece.COLOR.RED);
        expectedOutput = "Valid Turn";
        assertEquals(expectedOutput,validTurn.get("text").getAsString());
    }
}
