package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * View of the valid board
 *
 * @author Chris Piccoli, Adam Densman
 */
public class BoardView implements Iterable<Row>{
    private static final int NUM_ROWS = 8;
    ArrayList<Row> Rows = new ArrayList<>();

    /**
     * Makes a BoardView with the orientation of currentUserColor at the bottom
     *
     * @param currentUserColor the current player
     */
    public BoardView(Piece.COLOR currentUserColor) {
        if (currentUserColor != null) {
            for (int i = 0; i < NUM_ROWS; i++) {
                Rows.add(new Row(i, currentUserColor));
            }
        }
    }

    /**
     * Checks to see if a piece is on the board at row, col
     * indexing starts at 0 and goes to 7
     *
     * @param row the row of the piece
     * @param col the column of the piece
     * @return True if a piece is there, false otherwise
     */
    public boolean pieceAt(int row, int col) {
        return Rows.get(row).getSpace(col).getPiece() != null;
    }

    /**
     * Checks to see if a piece is on the board at row, col
     * indexing starts at 0 and goes to 7
     *
     * @param position where the space is located
     * @return True if a piece is there, false otherwise
     */
    public boolean pieceAt(Position position) {
        return pieceAt(position.getRow(), position.getCell());
    }


    /**
     * Get the piece at the row col of the board
     *
     * @param row the piece is in
     * @param col the piece is in
     * @return Piece at row col on board
     */
    public Piece getPiece(int row, int col) {
        return Rows.get(row).getSpace(col).getPiece();
    }

    /**
     * Get the piece at the row col of the board
     *
     * @param position to check the piece is in
     * @return Piece at row col on board
     */
    public Piece getPiece(Position position) {
        return getPiece(position.getRow(), position.getCell());
    }

    /**
     * The places a piece at row and col of COLOR color
     *
     * @param color color of piece to place
     * @param row   row of the board to place the piece
     * @param col   cell of the board to place the piece
     */
    public void placePiece(Piece.COLOR color, int row, int col) {
        Rows.get(row).getSpace(col).placePiece(color);
    }

    /**
     * The places a piece at row and col of COLOR color
     *
     * @param color    color of piece to place
     * @param position where on the board to place the piece
     */
    public void placePiece(Piece.COLOR color, Position position) {
        placePiece(color, position.getRow(), position.getCell());
    }

    /**
     * Get whether there is a king on the space
     *
     * @param row row of the board to check if king
     * @param col cell of the board to check if king
     * @return True if Piece is king at row, col
     */
    public boolean isKing(int row, int col) {
        Piece piece = getPiece(row, col);
        if (piece == null) {
            return false;
        }
        return piece.getType() == Piece.TYPE.KING;
    }

    /**
     * Make the piece on the board a king
     *
     * @param row the piece is at
     * @param col col the piece is at
     */
    public void kingPiece(int row, int col) {
        if (Rows.get(row).getSpace(col).getPiece() != null)
            Rows.get(row).getSpace(col).getPiece().kingPiece();
    }

    /**
     * Second constructor for testing purposes
     *
     * @param rowIterable the iterable to build the board
     */
    public BoardView(List<Row> rowIterable) {
        Rows = new ArrayList<>(rowIterable);
    }

    /**
     * Get the last row of the board
     *
     * @return the last row of the Board
     */
    public Row getLastRow() {
        return Rows.get(Rows.size() - 1);
    }

    /**
     * Makes a move on the board
     *
     * @param move the move to be made
     */
    public void makeMove(Move move) {
        Position start = move.getStart();
        // removes the piece from the starting square
        Piece piece = new Piece(Rows.get(start.getRow()).getSpace(start.getCell()).getPiece());
        Rows.get(start.getRow()).getSpace(start.getCell()).removePiece();

        Position end = move.getEnd();
        Rows.get(end.getRow()).getSpace(end.getCell()).placePiece(piece);
    }

    public void removePiece(int row, int col) {
        Rows.get(row).getSpace(col).removePiece();
    }

    /**
     * BoardView Iterator
     *
     * @return iterator of type Row
     */
    @Override
    public Iterator<Row> iterator() {
        return Rows.iterator();
    }

    /**
     * For each implementation for BoardView
     *
     * @param action the action being taken
     */
    @Override
    public void forEach(Consumer<? super Row> action) {
        Rows.forEach(action);
    }

    /**
     * BoardView spliterator
     *
     * @return spliterator of type Row
     */
    @Override
    public Spliterator<Row> spliterator() {
        return Rows.spliterator();
    }

    @Override
    public String toString() {
        String bd = "";
        for (Row r : Rows) {
            for (Space s : r) {
                bd = bd.concat("|").concat(s.toString()).concat("|");
            }
            bd = bd.concat("\n");
        }
        return bd;
    }

    /**
     * Compares the BoardViews. If the pieces are in the same spot in both BoardViews, then this returns true
     *
     * @param o the other object to compare to
     * @return True if the pieces are in the same places
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof BoardView) {
            BoardView other = (BoardView) o;
            return this.toString().equals(other.toString());
        } else {
            return false;
        }
    }
}
