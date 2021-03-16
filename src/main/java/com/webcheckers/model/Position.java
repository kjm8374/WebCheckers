package com.webcheckers.model;

/**
 * Holds the coordinates of a piece on the 8X8 checkers board, consisting of a row and a column
 * @author Chris Piccoli
 */
public class Position {
    private int row;
    private int cell;

    /**
     * Creates the coordinates of the piece
     * @param row the row of the piece
     * @param cell the column of the piece
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Gets the row of the piece
     * @return the row of the piece
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column of the piece
     *
     * @return the cell of the piece
     */
    public int getCell() {
        return cell;
    }

    /**
     * Checks to see if the row and cell are the same as another piece
     * @param O the piece being checked
     * @return true if they are both pieces and on the same space, otherwise false
     */
    @Override
    public boolean equals(Object O) {
        if (O.getClass() == Position.class) {
            Position otherPiece = (Position) O;
            return this.getCell() == otherPiece.getCell() && this.getRow() == otherPiece.getRow();
        } else
            return false;
    }

    /**
     * Reflects the position along the x axis
     *
     * @return the reflected position
     */
    public Position reflect() {
        return new Position(7 - this.getRow(), 7 - this.getCell());
    }
}
