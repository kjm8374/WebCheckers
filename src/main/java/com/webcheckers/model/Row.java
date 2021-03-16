package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;


/**
 * A Row on the board
 *
 * @author Chris Piccoli
 */
public class Row implements Iterable<Space>{
    private static final int NUM_SPACES = 8;
    private int index;
    private ArrayList<Space> Spaces = new ArrayList<>();

    public Row(int index, Piece.COLOR currentUserColor){
        this.index = index;

        Piece.COLOR otherUserColor;

        if(currentUserColor == Piece.COLOR.RED) {
            otherUserColor = Piece.COLOR.WHITE;
        } else {
            otherUserColor = Piece.COLOR.RED;
        }

        for (int i = 0; i < NUM_SPACES; i++) {
            //this just only populates black spaces
            if ((i + index) % 2 == 1) {
                if (index < 3)
                    Spaces.add(new Space(i, otherUserColor));
                else if (index > 4)
                    Spaces.add(new Space(i, currentUserColor));
                else
                    Spaces.add(new Space(i, index));
            } else
                Spaces.add(new Space(i, index));
        }
    }

    /**
     * Test constructor for move validation
     */
    public Row(List<Space> spaces) {
        this.Spaces = new ArrayList<>(spaces);
    }

    /**
     * Gets a specific spot on the board
     * @param col the column of the space to be returned
     * @return that space
     */
    public Space getSpace(int col) {
        return Spaces.get(col);
    }

    /**
     * Gets the row as a list of spaces
     * @return the row as a list of spaces
     */
    public List<Space> getSpaces(){
        return Spaces;
    }

    /**
     * @return iterator of type Space
     */
    @Override
    public Iterator<Space> iterator() {
        return Spaces.iterator();
    }

    /**
     * For each implementation
     */
    @Override
    public void forEach(Consumer<? super Space> action) {
        Spaces.forEach(action);
    }

    /**
     *
     * @return spliterator of type Row
     */
    @Override
    public Spliterator<Space> spliterator() {
        return Spaces.spliterator();
    }

    /**
     * Gets the index of the row
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * This method compares to see if a Object o is the same as this Row.
     *
     * @param o object being compared to
     * @return A boolean true if the objects are equal
     * false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row spaces = (Row) o;
        return index == spaces.index &&
                Objects.equals(Spaces, spaces.Spaces);
    }

    /**
     * @return A hashcode of type integer.
     */
    @Override
    public int hashCode() {
        return Objects.hash(index, Spaces);
    }
}
