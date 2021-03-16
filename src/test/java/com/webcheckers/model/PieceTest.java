
package com.webcheckers.model;

import com.webcheckers.model.Piece.COLOR;
import com.webcheckers.model.Piece.TYPE;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests the functionality of piece
 * @author Halle Masaryk, Adam Densman
 */

@Tag("Model-tier")
public class PieceTest {
    Piece white = new Piece(COLOR.WHITE);
    Piece red = new Piece(COLOR.RED);

    /**
     * Tests the color of the pieces
     */
    @Test
    public void testColor() {
        assertSame(COLOR.WHITE, white.getColor());
        assertFalse(white.getColor() == COLOR.RED);
        assertSame(COLOR.RED, red.getColor());
        assertFalse(red.getColor() == COLOR.WHITE);
    }

    /**
     * Tests the functionality of tracking a piece as a single or a king
     */
    @Test
    public void testType() {
        assertSame(TYPE.SINGLE, white.getType());
        assertNotSame(white.getType(), TYPE.KING);
        assertSame(TYPE.SINGLE, red.getType());
        assertNotSame(red.getType(), TYPE.KING);

    }

    /**
     * Tests the hashcode function
     */
    @Test
    void testHashcode(){
        Piece piece = new Piece(COLOR.RED);
        int hash = Objects.hash(piece.getType(), piece.getColor());
        assertEquals(hash, piece.hashCode());
    }
}
