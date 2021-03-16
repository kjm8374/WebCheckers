package com.webcheckers.model;

import static java.lang.Math.abs;

/**
 * Holds a single turn made during a turn of a game of checkers, consisting of a starting position and ending position
 *
 * @author Chris Piccoli
 */
public class Move {
    private Position start;
    private Position end;

    /**
     * Creates a move
     *
     * @param start the starting coordinates of the piece
     * @param end   the ending coordinates of the piece
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the middle position of a capture move
     *
     * @return the middle position
     * @precondition can only be called on capture moves
     */
    public Position getMiddle() {
        Position start = getStart(), end = getEnd();
        // I simplified it but basically the midpoint formula
        Position middle = new Position((end.getRow() + start.getRow()) / 2,
                (end.getCell() + start.getCell()) / 2);
        if (middle.equals(getStart()) || middle.equals(getEnd()))
            return null;

        return middle;
    }

    /**
     * Returns the Manhattan or city block distance of the move (see
     * <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">the Wikipedia article</a>).
     */
    public double manhattanDistance() {
        Position end = getEnd(), start = getStart();
        return abs(end.getRow() - start.getRow()) + abs(end.getCell() - start.getCell());
    }

    /**
     * Get the starting position of the move
     *
     * @return the starting position of the move
     */
    public Position getStart() {
        return start;
    }

    /**
     * Makes a move that is the exact opposite of previous move
     *
     * @return a move the opposite of the previous
     */
    public Move reciprocalMove() {
        return new Move(this.getEnd(), this.getStart());
    }

    /**
     * Checks if the pieces is trying to move backward
     *
     * @return True if the move is backward false otherwise
     */
    public boolean isBackward() {
        return this.getStart().getRow() - this.getEnd().getRow() < 0;
    }

    /**
     * Checks if the pieces is trying to move vertical
     *
     * @return True if the move is vertical false otherwise
     */
    public boolean isVertical() {
        return this.getEnd().getRow() == this.getStart().getRow();
    }

    /**
     * Checks if the pieces is trying to move horizontal
     *
     * @return True if the move is horizontal false otherwise
     */
    public boolean isHorizontal() {
        return this.getEnd().getCell() == this.getStart().getCell();
    }

    /**
     * Get the ending position of the move
     *
     * @return the ending position of the move
     */
    public Position getEnd() {
        return end;
    }

    /**
     * Tests if two moves are equivalent.
     * <p>
     * Two moves are equivalent if their start positions are equal and if their end positions are equal.
     *
     * @param o The other object to compare to
     * @return true if the other object is a Move that has the same start and end positions as this move; false
     * otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Move) {
            Move other = (Move) o;
            return this.getStart().equals(other.getStart()) && this.getEnd().equals(other.getEnd());
        } else {
            return false;
        }
    }

    /**
     * Makes the move that an opponent made on the player's board. Used to make both player's boards accurate, updates
     * after each turn
     *
     * @return the move the opponent made, reflected to the current board
     */
    public Move reflect() {
        Position start = this.getStart(), end = this.getEnd();
        Position startReflected, endReflected;
        startReflected = start.reflect();
        endReflected = end.reflect();

        return new Move(startReflected, endReflected);
    }

    /**
     * Check if the move is on an adjacent square
     *
     * @return boolean true if the move is in an adjacent square on the board
     * precondition piece must be at move.getStart()
     */
    public boolean isAdjacentMove() {
        return this.manhattanDistance() <= 2;
    }
}
