package com.webcheckers.application;

import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Position;


/**
 * The replay of a game played on the server
 *
 * @author Chris Piccoli
 */
public class GameReplay extends GameLobby {

    private int turn = 0;

    /**
     * Constructor for GameReplay
     *
     * @param game to turn into a replay
     */
    public GameReplay(GameLobby game) {
        super(game.getGameID(), game.getRedPlayer(), game.getWhitePlayer(), new MoveValidator());
        this.setMoves(game.getMoves());
    }

    /**
     * Makes a move that is sent to it
     *
     * @param move the move that is being made
     */
    @Override
    public void makeMove(Move move) {
        Move moveReflected = move.reflect();
        Position middle = move.getMiddle();

        // This monstrosity sets the active player to the one moving
        setActivePlayer(getRedBoard().getPiece(move.getStart()).getColor());

        getRedBoard().makeMove(move);
        getWhiteBoard().makeMove(moveReflected);

        if (middle != null) {
            if (getRedBoard().pieceAt(middle)) {
                // removes piece back on the board
                Position middleReflected = moveReflected.getMiddle();
                getRedBoard().placePiece(getInactiveColor(), middle.getRow(), middle.getCell());
                getWhiteBoard().placePiece(getInactiveColor(), middleReflected.getRow(), middleReflected.getCell());
            } else {
                // removes pieces for capture
                Position middleReflected = moveReflected.getMiddle();
                getRedBoard().removePiece(middle.getRow(), middle.getCell());
                getWhiteBoard().removePiece(middleReflected.getRow(), middleReflected.getCell());
            }
        }
        kingPieces();
    }

    /**
     * Backs up one move in the replay
     */
    public void lastMove() {
        turn--;
        makeMove(getMoves().get(turn).reciprocalMove());
    }

    /**
     * Makes the next move in the replay
     */
    public void nextMove() {
        makeMove(getMoves().get(turn));
        turn++;
    }

    /**
     * If the replay has a previous move
     *
     * @return true if the replay has a previous move
     */
    public boolean hasPrevious() {
        return turn != 0;
    }

    /**
     * If the replay has a next move
     *
     * @return true if there is a next move
     */
    public boolean hasNext() {
        return turn < getMoves().size();
    }
}
