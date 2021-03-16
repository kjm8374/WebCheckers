package com.webcheckers.application;

import com.webcheckers.model.BoardView;
import com.webcheckers.model.Move;
import com.webcheckers.model.MoveValidator;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Row;
import com.webcheckers.model.Space;

import java.util.ArrayList;
import java.util.List;

/**
 * GameLobby represents the data for an individual game, including its players, its board, and its completion status.
 * <p>
 * A conglomeration of all the pertinent game information
 * </p>
 *
 * @author Chris Piccoli
 */
public class GameLobby {
    private Player redPlayer;
    private Player whitePlayer;
    private int GameID;
    private Piece.COLOR activePlayer;
    private boolean isGameDone = false;
    private String gameOverReason;

    // This is a memory inefficient way to do this, but its also very easy
    // for making move purposes use the indices of the redBoard serverSide
    private BoardView redBoard;
    private BoardView whiteBoard;

    private MoveValidator validator;
    // list of moves from reds perspective
    private List<Move> moves = new ArrayList<>();

    /**
     * Constructor. Creates a GameLobby with the given GameID and the two Players.
     *
     * @param GameID      the game ID of the new GameLobby to create
     * @param redPlayer   the Player who controls the red pieces
     * @param whitePlayer the Player who controls the white pieces
     * @param validator   the MoveValidator used to validate moves for this game
     */
    public GameLobby(int GameID, Player redPlayer, Player whitePlayer, MoveValidator validator) {
        this.validator = validator;
        activePlayer = Piece.COLOR.RED;
        this.GameID = GameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        redBoard = new BoardView(Piece.COLOR.RED);
        whiteBoard = new BoardView(Piece.COLOR.WHITE);
    }

    /**
     * makes the move of a single piece on the checkers board
     *
     * @param move the move that is being made
     */
    public void makeMove(Move move) {
        Move moveReflected = move.reflect();
        Position middle = move.getMiddle();

        if (getActiveColor() == Piece.COLOR.RED) {
            redBoard.makeMove(move);
            whiteBoard.makeMove(moveReflected);
            // removes pieces for capture
            if (middle != null) {
                Position middleReflected = moveReflected.getMiddle();
                getRedBoard().removePiece(middle.getRow(), middle.getCell());
                getWhiteBoard().removePiece(middleReflected.getRow(), middleReflected.getCell());
            }

            moves.add(move);
        } else {
            whiteBoard.makeMove(move);
            redBoard.makeMove(moveReflected);
            // removes pieces for capture
            if (middle != null) {
                Position middleReflected = moveReflected.getMiddle();
                getWhiteBoard().removePiece(middle.getRow(), middle.getCell());
                getRedBoard().removePiece(middleReflected.getRow(), middleReflected.getCell());
            }

            moves.add(moveReflected);
        }
        kingPieces();
    }

    /**
     * Makes pieces that have reached the opposite end of the board kings
     */
    protected void kingPieces() {
        Row firstRowRed = redBoard.getLastRow();
        Row firstRowWhite = whiteBoard.getLastRow();

        for (Space space : firstRowRed) {
            Piece piece = space.getPiece();
            if (piece != null)
                if (piece.getColor() == Piece.COLOR.WHITE) {
                    if (piece.getType() == Piece.TYPE.SINGLE) {
                        redBoard.kingPiece(firstRowRed.getIndex(), space.getCellIdx());
                        whiteBoard.kingPiece(7 - firstRowRed.getIndex(), 7 - space.getCellIdx());
                    }
                }
        }
        for (Space space : firstRowWhite) {
            Piece piece = space.getPiece();
            if (piece != null)
                if (piece.getColor() == Piece.COLOR.RED) {
                    if (piece.getType() == Piece.TYPE.SINGLE) {
                        redBoard.kingPiece(7 - firstRowWhite.getIndex(), 7 - space.getCellIdx());
                        whiteBoard.kingPiece(firstRowWhite.getIndex(), space.getCellIdx());
                    }
                }
        }
    }


    /**
     * Gets the red player
     *
     * @return the red player
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    /**
     * Gets the white player
     *
     * @return the white player
     */
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    /**
     * Return whether or not the given user is one of the two players of this game.
     *
     * @param player the player to check for membership in this game
     * @return true if the given user is one of the two players of this game; false if they are not.
     */
    public boolean isPlayerInThisGame(Player player) {
        return player.equals(getRedPlayer()) || player.equals(getWhitePlayer());
    }

    /**
     * Gets the game ID
     *
     * @return the game ID
     */
    public int getGameID() {
        return GameID;
    }

    /**
     * Gets the current color of the player
     *
     * @return the player's color for that game
     */
    public Piece.COLOR getActiveColor() {
        return activePlayer;
    }

    /**
     * Gets the player who's turn it currently is
     *
     * @return the player who's turn it currently is
     */
    public Player getActivePlayer() {
        if (getActiveColor() == Piece.COLOR.RED)
            return getRedPlayer();
        else
            return getWhitePlayer();
    }

    public boolean isGameDone() {
        return isGameDone;
    }

    public void markGameAsDone(String gameOverReason) {
        isGameDone = true;
        // save the replay of this game
        GameReplayManager.saveReplay(this);
        this.gameOverReason = gameOverReason;
    }


    /**
     * Checks through this GameLobby's board to see if a victory condition has been reached for either side.
     * <p>
     * If either player has won, marks this GameLobby as "complete", setting an appropriate "game over" message.
     * <p>
     * TODO: Currently, this only checks for capturing all the pieces. We also need to check for no more possible moves.
     */
    public boolean checkForVictory() {
        // keep track of whether we have seen any pieces of either color.
        boolean anyWhitePieces = false;
        boolean anyRedPieces = false;
        // iterate through each row
        for (Row row : this.redBoard) {
            // whether we iterate through the red or white board does not matter; they contain the same pieces, just
            // in a different layout
            // iterate through each space in a row
            for (Space space : row) {
                // if there is a piece on this space
                if (space.getPiece() != null) {
                    if (space.getPiece().getColor() == Piece.COLOR.RED) {
                        // and if it's red, we have now seen at least one red piece
                        anyRedPieces = true;
                    } else if (space.getPiece().getColor() == Piece.COLOR.WHITE) {
                        // and if it's white, we have now seen at least one white piece
                        anyWhitePieces = true;
                    }
                }
            }
        }
        // if we haven't seen any pieces of a color, then the other player has won
        if (!anyRedPieces) {
            // white player has won
            markGameAsDone(getWhitePlayer().getName() + " has captured all the pieces.");
            return true;
        } else if (!anyWhitePieces) {
            // red player has won
            markGameAsDone(getRedPlayer().getName() + " has captured all the pieces.");
            return true;
        }
        return false;
    }

    /**
     * call to get the game over reason
     *
     * @return the reason the game ended as String
     */
    public String getGameOverReason() {
        if (isGameDone()) {
            return this.gameOverReason;
        } else {
            return "The game is still in progress.";
        }
    }

    /**
     * Get the player taking their turn
     *
     * @param activePlayer the one taking their turn
     */
    public void setActivePlayer(Piece.COLOR activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Gets the board from the red player's perspective
     *
     * @return the board from the red player's perspective
     */
    public BoardView getRedBoard() {
        return redBoard;
    }

    /**
     * Gets the board from the white player's perspective
     *
     * @return the board from the white player's perspective
     */
    public BoardView getWhiteBoard() {
        return whiteBoard;
    }

    /**
     * Gets the board from the perspective of the current player
     *
     * @return the board from the perspective of the current player
     */
    public BoardView getActiveBoard() {
        if (activePlayer == Piece.COLOR.RED)
            return getRedBoard();
        else
            return getWhiteBoard();
    }

    /**
     * Get the MoveValidator
     *
     * @return MoveValidator of this gameLobby
     */
    public MoveValidator getValidator() {
        return validator;
    }

    /**
     * Ends the current players turn
     */
    public void endTurn() {
        if (getActiveColor() == Piece.COLOR.RED) {
            setActivePlayer(Piece.COLOR.WHITE);
        } else {
            setActivePlayer(Piece.COLOR.RED);
        }
    }

    /**
     * Gets the player whose turn it is not
     *
     * @return the player waiting for their turn
     */
    public Player getInactivePlayer() {
        if (redPlayer.equals(getActivePlayer()))
            return whitePlayer;
        else
            return redPlayer;
    }

    /**
     * Gets the color that is not playing
     *
     * @return Piece.TYPE.Color of player not playing
     */
    public Piece.COLOR getInactiveColor() {
        if (Piece.COLOR.RED == getActiveColor())
            return Piece.COLOR.WHITE;
        else
            return Piece.COLOR.RED;
    }

    /**
     * Get the list of moves made in this game so far
     * <p>
     * This is used in the children functions.
     *
     * @return a List of moves made in this game so far
     */
    protected List<Move> getMoves() {
        return moves;
    }

    /**
     * Sets the List moves to a prior set of moves
     * <\p> used in the children functions </\p>
     *
     * @param moves set to override the prior value
     */
    protected void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    /**
     * Equals function for Game Lobby
     * Two gamelobbys are the same if they have the same gameID
     *
     * @param o the other object to compare to
     * @return if the GameLobbys are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof GameLobby) {
            GameLobby other = (GameLobby) o;
            return this.getGameID() == other.getGameID();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getGameID() + ": " + getRedPlayer().getName() + " vs " + getWhitePlayer().getName();
    }
}
