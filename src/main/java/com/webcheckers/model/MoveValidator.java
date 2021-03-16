package com.webcheckers.model;

import com.google.gson.JsonObject;
import com.webcheckers.application.GameLobby;

import java.util.ArrayList;

/**
 * Checks if a move is valid
 *
 * @author Adam Densman, Chris Piccoli, Kushal Malhotra
 */
public class MoveValidator {
    private ArrayList<Move> moves = new ArrayList<>();


    /**
     * Determines if a move or capture being made by a piece is a valid move or not. Takes into account captures as well
     * as if the piece is a single or a king
     *
     * @param move        the move being validated
     * @param currentTurn The player whose turn it is
     * @param board       the board from that player's perspective
     * @return Client response to update the board according to the move made or error message sent
     */
    public JsonObject ValidateMove(Move move, Piece.COLOR currentTurn, BoardView board) {

        JsonObject isMoveValid = new JsonObject();


        // checks if the move isnt adjacent or a jump

        boolean is_capture = isCapture(move, currentTurn, board);
        boolean is_king = board.isKing(move.getStart().getRow(), move.getStart().getCell());

        // checks if the move is not forward and if the piece is not a king
        if (move.isBackward() && !is_king) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can only move backwards if your a king!");
            return isMoveValid;
        } else if (move.isHorizontal() || move.isVertical()) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can only move diagonally!");
            return isMoveValid;
        }//This also checks for forward on nonking pieces
        else if (!move.isAdjacentMove() && !is_capture) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can only move one space unless you take!");
            return isMoveValid;
        }// Checks that the last move was a capture if this move was a capture
        else if (moves.size() > 0 && move.isAdjacentMove()) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can only move once a turn without a capture!");
            return isMoveValid;
        }//makes sure the next move isnt a capture after a normal move
        else if (moves.size() > 0 && move.isAdjacentMove()) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can't move then capture!");
            return isMoveValid;
        }// Checks if next move isn't with the same piece
        else if (moves.size() > 0 && !move.getStart().equals(getLastMove().getEnd())) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You can only move one piece a turn!");
            return isMoveValid;
        }

        if (isAvailableCapture(board, currentTurn) && !is_capture) {
            isMoveValid.addProperty("type", "ERROR");
            isMoveValid.addProperty("text", "You must make available capture!");
            return isMoveValid;
        }

        moves.add(move);
        isMoveValid.addProperty("type", "INFO");
        isMoveValid.addProperty("text", "Yay a valid move.");

        return isMoveValid;
    }

    /**
     * checks if a move is a jump
     *
     * @param move         the move being made
     * @param currentColor the active users color
     * @param board        the board being checked
     * @return True if the jump is a valid capture of a piece, otherwise false
     */
    private static boolean isCapture(Move move, Piece.COLOR currentColor, BoardView board) {

        Position currentPosition = move.getStart();
        Position middle = move.getMiddle();
        Position end = move.getEnd();
        boolean validCapture = false;

        // this doesn't check all the cases but looks so much better
        // particularly that pieces can be captured 2 or more times

        if (move.manhattanDistance() == 4)
            // check for pieces being potentially stacked
            if (!board.pieceAt(end.getRow(), end.getCell()))
                // check to make sure a piece exists under the capture
                if (middle != null)
                    if (!(move.isBackward() && !board.isKing(currentPosition.getRow(), currentPosition.getCell()))) {
                        // check that the piece is the opposite color
                        if (board.pieceAt(middle.getRow(), middle.getCell())) {
                            Piece piece = board.getPiece(middle.getRow(), middle.getCell());
                            validCapture = piece.getColor() != currentColor;
                        }
                    }

        return validCapture;
    }



    /**
     * Validates the turn of a player
     *
     * @return The response sent to the client to update the board or sent a message
     */
    public JsonObject ValidateTurn(BoardView red, BoardView white, Piece.COLOR currentColor) {
        JsonObject validTurn = new JsonObject();
        Move lastmove = getLastMove();
        BoardView currentBoard;
        if (currentColor == Piece.COLOR.RED)
            currentBoard = red;
        else
            currentBoard = white;
        // check if the pieces last move was not a single move
        if (!lastmove.isAdjacentMove()
                //check if the piece can make a capture from their current position
                && pieceCanCapture(currentBoard, currentColor, lastmove.getEnd())) {
            validTurn.addProperty("type", "ERROR");
            validTurn.addProperty("text", "Invalid Turn Legal Capture available");
        } else {
            validTurn.addProperty("type", "INFO");
            validTurn.addProperty("text", "Valid Turn");
            //removePieces(red, white);
            turnDone();
        }
        return validTurn;
    }

    /**
     * Getter for the last move made
     *
     * @return the last move made
     */
    public Move getLastMove() {
        return moves.get(moves.size() - 1);
    }


    /**
     * Undoes the last move made by the user
     *
     * @param game the current game
     * @return the client response to update the board or send a message
     */
    public JsonObject undoMove(GameLobby game) {
        JsonObject status = new JsonObject();
        if (moves.size() > 0) {
            Move lastMove = getLastMove();
            // the opposite of the last move made
            Move reciprocalMove = lastMove.reciprocalMove();
            moves.remove(lastMove);
            game.makeMove(reciprocalMove);

            // was this a capture
            if (!lastMove.isAdjacentMove()) {
                Position middle = lastMove.getMiddle();
                Position middleReflect = middle.reflect();
                BoardView red = game.getRedBoard();
                BoardView white = game.getWhiteBoard();
                //put the piece back
                if (game.getActiveColor() == Piece.COLOR.RED) {
                    red.placePiece(game.getInactiveColor(), middle);
                    white.placePiece(game.getInactiveColor(), middleReflect);
                } else {
                    white.placePiece(game.getInactiveColor(), middle);
                    red.placePiece(game.getInactiveColor(), middleReflect);
                }

            }

            status.addProperty("type", "INFO");
            status.addProperty("text", "Yay you backed up your move!");
        } else {
            status.addProperty("type", "ERROR");
            status.addProperty("text", "You have no moves to back up?");
        }
        return status;
    }
    /**
     * Is called after each turn is over, clears the log of move(s) made on that turn
     */
    public void turnDone() {
        moves.clear();
    }

    /**
     * Checks if there are any available captures for the current user on the board
     *
     * @param board        the current boardstate
     * @param currentColor the color of the players turn
     * @return true if there is a legal capture, false otherwise
     */
    public boolean isAvailableCapture(BoardView board, Piece.COLOR currentColor) {
        // iterate through each row
        for (Row row : board) {
            // iterate through each space in a row
            for (Space space : row) {
                // get the pieces on each space
                Piece piece = space.getPiece();
                //Check to make sure piece actually exists
                if (piece != null)
                    // is this piece the same color as the users turn
                    if (piece.getColor() == currentColor)
                        // are there captures this piece can legally make
                        if (isAvailableCapture(board, currentColor, space, row))
                            return true;

            }
        }
        return false;
    }

    /**
     * Returns whether or not the piece on the space can make a legal capture
     * <p>
     * Must have a piece of color current users turn on the space
     *
     * @param board current board state
     * @param space the space in question
     * @return true if there is a legal capture at space
     */
    private boolean isAvailableCapture(BoardView board, Piece.COLOR currentColor, Space space, Row row) {
        Position currentPosition = new Position(row.getIndex(), space.getCellIdx());
        // check to see if any other moves have been made
        if (moves.size() > 0) {
            Position end = getLastMove().getEnd();
            // make sure that if a move has been made we check if this is the piece that has been moved
            if (currentPosition.equals(end)) {
                // can it capture any pieces legally
                return pieceCanCapture(board, currentColor, currentPosition);
            } else
                return false;
        }
        return pieceCanCapture(board, currentColor, currentPosition);
    }

    /**
     * Checks all available spaces a distance 2 away
     * to see if they have a legal jump
     *
     * @param board    The boardstate being checked
     * @param color    the color of the piece moving
     * @param position the position of the piece
     * @return true if there is a capture, false otherwise
     */
    private static boolean pieceCanCapture(BoardView board, Piece.COLOR color, Position position) {
        int col = position.getCell(), row = position.getRow();
        int left = col - 2, right = col + 2, up = row + 2, down = row - 2;

        Move rightUp = new Move(position, new Position(row + 2, col + 2));
        Move leftUp = new Move(position, new Position(row + 2, col - 2));
        Move rightDown = new Move(position, new Position(row - 2, col + 2));
        Move leftDown = new Move(position, new Position(row - 2, col - 2));

        if (up < 8) {
            if (right < 8 && isCapture(rightUp, color, board))
                return true;
            else if (left >= 0 && isCapture(leftUp, color, board))
                return true;
        }
        if (down >= 0) {
            if (right < 8 && isCapture(rightDown, color, board))
                return true;
            else return left >= 0 && isCapture(leftDown, color, board);
        }
        return false;
    }

}
