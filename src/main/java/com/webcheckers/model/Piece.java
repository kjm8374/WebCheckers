package com.webcheckers.model;

import java.util.Objects;

/**
 * A Piece on a Space on the BoardView
 *
 * @author Chris Piccoli
 */
public class Piece {
    public enum TYPE {
        SINGLE, KING
    }

    public enum COLOR{
        RED, WHITE
    }

    private TYPE type;
    private COLOR color;

    /**
     * Creates a Piece of color: color
     *
     * @param color The color of the piece
     */
    public Piece(COLOR color) {
        this.color = color;
        this.type = TYPE.SINGLE;
    }

    public Piece(Piece piece) {
        this.type = piece.type;
        this.color = piece.color;
    }

    /**
     * Kings a piece on the back rank
     */
    public void kingPiece() {
        type = TYPE.KING;
    }

    /**
     * @return enum of Type {SINGLE or KING}
     */
    public TYPE getType() {
        return type;
    }

    /**
     * @return enum of Type {RED or WHITE}
     */
    public COLOR getColor() {
        return color;
    }

    /**
     * This method compares to see if a Object o is the same as this piece.
     *
     * @param o object being compared to
     * @return A boolean true if the objects are equal
     * false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type &&
                color == piece.color;
    }

    /**
     * @return A hashcode of type integer.
     */

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
