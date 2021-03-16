package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The unit test suite for the {@link Space} component.
 *
 * @author Ben coffta, Adam Densman
 */
@Tag("Model-tier")
class SpaceTest {
    private static final int FIRST_SPACE_INDEX = 5;
    private static final int SECOND_SPACE_INDEX = 2;
    private static final int NEGATIVE_SPACE_INDEX = -1;
    private static final Piece RED_PIECE = new Piece(Piece.COLOR.RED);
    private static final Piece WHITE_PIECE = new Piece(Piece.COLOR.WHITE);

    /**
     * Test creation and operation of a Space with no Piece on it.
     */
    @Test
    void testEmptySpace() {
        // Test a space with a valid (positive) index.
        Space CuT = new Space(FIRST_SPACE_INDEX, 0);
        assertEquals(FIRST_SPACE_INDEX, CuT.getCellIdx(), "Empty Space did not return correct column index");
        assertNull(CuT.getPiece(), "Empty Space's getPiece() is not null");
        assertTrue(CuT.isValid(), "Empty Space is not valid");

        // Test an empty space with a negative index. This should throw an IllegalArgumentException, since the cellIdx
        // parameter in the Space constructor must not be negative.
        assertThrows(IllegalArgumentException.class, () -> {
            new Space(NEGATIVE_SPACE_INDEX, 0);
        }, "Empty Space accepts negative cellIdx");
    }


    /**
     * Test creation and operation of a Space with a Piece on it.
     * <p>
     * This method tests Spaces with both red and white Pieces.
     */
    @Test
    void testSpaceWithPiece() {
        // Test a Space with a red Piece
        Space redSpace = new Space(FIRST_SPACE_INDEX, Piece.COLOR.RED);
        assertEquals(FIRST_SPACE_INDEX, redSpace.getCellIdx(), "Space with Piece did not return correct column index");
        assertEquals(RED_PIECE.getColor(), redSpace.getPiece().getColor(), "Space with Piece did not return correct " +
                "Piece from getPiece()");
        assertFalse(redSpace.isValid(), "Space with Piece is not valid");

        // Test a Space with a white Piece
        Space whiteSpace = new Space(SECOND_SPACE_INDEX, Piece.COLOR.WHITE);
        assertEquals(SECOND_SPACE_INDEX, whiteSpace.getCellIdx(), "Space with Piece did not return correct column " +
                "index");
        assertEquals(WHITE_PIECE.getColor(), whiteSpace.getPiece().getColor(), "Space with Piece did not return " +
                "correct Piece from getPiece()");
        assertFalse(whiteSpace.isValid(), "Space with Piece is not valid");

        // Test a space with a piece and a negative index. This should throw an IllegalArgumentException, since the
        // cellIdx parameter in the Space constructor must not be negative.
        assertThrows(IllegalArgumentException.class, () -> {
            new Space(NEGATIVE_SPACE_INDEX, Piece.COLOR.RED);
        }, "Space with Piece accepts negative cellIdx");
    }


    /**
     * Test the Space.getCellIdx() method. This method should return the same column index passed into the constructor.
     */
    @Test
    void testGetCellIdx() {
        // Test empty Spaces
        Space firstEmptySpace = new Space(FIRST_SPACE_INDEX, 0);
        assertEquals(FIRST_SPACE_INDEX, firstEmptySpace.getCellIdx(), "Empty Space did not return correct column " +
                "index");
        Space secondEmptySpace = new Space(SECOND_SPACE_INDEX, 0);
        assertEquals(SECOND_SPACE_INDEX, secondEmptySpace.getCellIdx(), "Empty Space did not return correct column " +
                "index");

        // Test Spaces with Pieces
        Space firstSpaceWithPiece = new Space(FIRST_SPACE_INDEX, Piece.COLOR.RED);
        assertEquals(FIRST_SPACE_INDEX, firstSpaceWithPiece.getCellIdx(), "Space with Piece did not return correct " +
                "column index");
        Space secondSpaceWithPiece = new Space(SECOND_SPACE_INDEX, Piece.COLOR.WHITE);
        assertEquals(SECOND_SPACE_INDEX, secondSpaceWithPiece.getCellIdx(), "Space with Piece did not return correct " +
                "column index");
    }

    /**
     * Test the Space.getPiece() method. This should return null for an empty Space, and return the Piece on a Space
     * for a Space with a Piece on it.
     */
    @Test
    void testGetPiece() {
        Space empty = new Space(FIRST_SPACE_INDEX, 0);
        assertNull(empty.getPiece(), "Empty Space's getPiece() is not null");
        Space redSpace = new Space(FIRST_SPACE_INDEX, Piece.COLOR.RED);
        assertEquals(RED_PIECE.getColor(), redSpace.getPiece().getColor(), "Space with red Piece on it did not " +
                "return correct Piece in getPiece()");
        Space whiteSpace = new Space(FIRST_SPACE_INDEX, Piece.COLOR.WHITE);
        assertEquals(WHITE_PIECE.getColor(), whiteSpace.getPiece().getColor(), "Space with red Piece on it did not " +
                "return correct Piece in getPiece()");
    }

    /**
     * Test the Space.isValid() method. This method is likely unnecessary; it is present only because it is required
     * by game.ftl, and this requirement is also likely unnecessary. This method should always return true.
     */
    @Test
    void isValid() {
        // Test empty Spaces
        Space emptySpace = new Space(FIRST_SPACE_INDEX, 0);
        assertTrue(emptySpace.isValid(), "Empty space is not valid");

        // Test Spaces with Pieces
        Space firstSpaceWithPiece = new Space(FIRST_SPACE_INDEX, Piece.COLOR.RED);
        assertFalse(firstSpaceWithPiece.isValid(), "Space with piece is not valid");
        Space secondSpaceWithPiece = new Space(SECOND_SPACE_INDEX, Piece.COLOR.WHITE);
        assertFalse(secondSpaceWithPiece.isValid(), "Space with piece is not valid");
    }

    /**
     * Tests the hashcode function
     */
    @Test
    void testHashcode() {
        Space space = new Space(0, Piece.COLOR.RED);
        int hash = Objects.hash(space.getCellIdx(), space.getPiece());
        assertEquals(hash, space.hashCode());
        Row row = new Row(0, space.getPiece().getColor());
        hash = Objects.hash(row.getIndex(), row.getSpaces());
        assertEquals(hash, row.hashCode());
    }

    /**
     * Tests that the equals() method behaves as intended.
     */
    @Test
    void testEquals() {
        Space first = new Space(0, Piece.COLOR.RED);
        Space second = new Space(0, Piece.COLOR.RED); // should be equal to first
        assertEquals(first, first, "Piece.equals() returns false for two of the same Spaces");
        assertEquals(first, second, "Piece.equals() returns false for Pieces with same color and cellIdx");

        assertNotEquals(first, null, "Piece.equals() returns true for null");
        // make some object of a different class and assert it is not equal to the Space
        assertNotEquals(first, "some other object", "Piece.equals() returns true for different classes");

        // test spaces with different cellIdx but the same piece color
        assertNotEquals(first, new Space(1, Piece.COLOR.RED));

        // test space with same cellIdx but different piece colors
        assertNotEquals(first, new Space(0, Piece.COLOR.WHITE));

        // test spaces with different cellIdx and piece colors
        assertNotEquals(first, new Space(1, Piece.COLOR.WHITE));

    }
}