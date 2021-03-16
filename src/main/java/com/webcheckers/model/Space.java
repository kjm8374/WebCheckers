package com.webcheckers.model;

import java.util.Objects;

/**
 * A space on the boardView
 *
 * @author Chris Piccoli
 */
public class Space {
    /**
     * The index of this Space in its Row, i.e. its column on the board.
     */
    private int cellIdx;
    /**
     * The Piece on this Space. A value of null represents no Piece.
     */
    private Piece piece = null;
    private boolean isWhiteSpace = false;

    /**
     * Creates an empty Space, with no piece on it.
     *
     * @param cellIdx the index of this Space in its row, i.e. its column on the board. Must be at least 0.
     */
    public Space(int cellIdx, int row) {
        // A cellIdx must be at least zero. A negative cellIdx is invalid.
        if (cellIdx < 0) {
            throw new IllegalArgumentException("cellIdx must not be negative");
        }
        this.cellIdx = cellIdx;
        isWhiteSpace = (cellIdx % 2 == 1 && row % 2 == 1) || (cellIdx % 2 == 0 && row % 2 == 0);
    }


    /**
     * Creates a Space with a Piece on it.
     *
     * @param cellIdx the index of this Space in its row, i.e. its column on the board. Must be at least 0.
     * @param color   the color of the Piece on this space
     */
    public Space(int cellIdx, Piece.COLOR color) {
        // A cellIdx must be at least zero. A negative cellIdx is invalid.
        if (cellIdx < 0) {
            throw new IllegalArgumentException("cellIdx must not be negative");
        }
        this.cellIdx = cellIdx;
        // TODO: This is a good place to use Dependency Injection instead of instantiating the Piece here.
        piece = new Piece(color);
    }

    /**
     * Remove a piece from the current space
     */
    public void removePiece() {
        piece = null;
    }

    /**
     * places a new piece of color color on the space
     *
     * @param color color of piece placed
     */
    public void placePiece(Piece.COLOR color) {
        piece = new Piece(color);
    }

    /**
     * places a the piece on the space
     *
     * @param piece the piece placed
     */
    public void placePiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Accessor method for the index of this Space in its Row, i.e. its column on the board.
     *
     * @return this Space's index in its Row, i.e. its column on the board
     */
    public int getCellIdx() {
        return cellIdx;
    }

    /**
     * Accessor method for the piece on this space. Returns null if there is no piece on this space.
     *
     * @return the Piece that is on this space, or null if there is no piece on this space
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Check whether or not this space is valid.
     * <p>
     * This is required by game.ftl.
     * <p>
     * TODO: A validity check for Spaces may not be necessary. If the reference in game.ftl is removed, this method can be removed too.
     *
     * @return always true
     */
    public boolean isValid() {
        return piece == null && !isWhiteSpace;
    }

    /**
     * This method compares to see if a Object o is the same as this Space.
     *
     * @param o the object being compared to
     * @return A boolean true if the objects are equal
     * false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return cellIdx == space.cellIdx &&
                Objects.equals(piece, space.piece);
    }

    /**
     * @return A hashcode of type integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cellIdx, piece);
    }


    @Override
    public String toString() {
        if (piece == null)
            return " ";
        else if (piece.getColor() == Piece.COLOR.RED)
            return "R";
        else
            return "W";
    }
}
