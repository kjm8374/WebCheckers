package com.webcheckers.model;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Tag("Model-tier")
public class BoardViewTest {


    static ArrayList<Row> currentRow = new ArrayList<>();
    static ArrayList<Row> currentRow2 = new ArrayList<>();

    @BeforeAll
    static void createRows() {
        for (int i = 0; i < 8; i++) {
            currentRow.add(new Row(i, Piece.COLOR.RED));
        }
        for (int i = 0; i < 8; i++) {
            currentRow2.add(new Row(i, Piece.COLOR.WHITE));
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void TestBoardView() {
        BoardView board1 = new BoardView(Piece.COLOR.RED);
        for (int i = 0; i < 8; i++) {
            assertEquals(currentRow.get(i), board1.Rows.get(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board1.Rows.get(9);
        }, "Board only has 8 rows.");
        BoardView board2 = new BoardView(Piece.COLOR.WHITE);
        for (int i = 0; i < 8; i++) {
            assertEquals(currentRow2.get(i), board2.Rows.get(i));
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            board2.Rows.get(9);
        }, "Board only has 8 rows.");
    }

    @Test
    public void TestIterator() {
        BoardView nullboard = new BoardView((Piece.COLOR) null);
        assertFalse(nullboard.iterator().hasNext());
        assertThrows(NoSuchElementException.class, () -> {
            nullboard.iterator().next();
        }, "Board with a null color type has a next row");

        BoardView board = new BoardView(Piece.COLOR.WHITE);
        assertTrue(board.iterator().hasNext());
        assertNotNull(board.iterator().next(), "Board should contain rows.");

        BoardView board1 = new BoardView(Piece.COLOR.RED);
        assertTrue(board1.iterator().hasNext());
        assertNotNull(board1.iterator().next(), "Board should contain rows.");

    }

}
