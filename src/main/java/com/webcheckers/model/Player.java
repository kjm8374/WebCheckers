package com.webcheckers.model;

import java.util.Objects;

/**
 * The Player class represents a logged-in player.
 * <p>
 * This class is an Entity component with identity semantics. Its natural key is its name.
 *
 * @author Kushal, Piccoli, Halle
 */
public class Player {

    private String name;

    /**
     * Create a new Player with the given name.
     *
     * @param name the name of the Player
     */
    public Player(String name) { this.name = name; }

    /**
     * Accessor method for this Player's name.
     * <p>
     * TODO: This method may not need to be synchronized.
     *
     * @return this Player's name
     */
    public synchronized String getName() {
        return this.name;
    }

    /**
     * Determine the hash code for a Player.
     * <p>
     * The hash code for a Player is based on the Player's name.
     *
     * @return this Player's hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    /**
     * Determine whether two Players are equal.
     * <p>
     * Two Players are considered equal if their names are the same.
     *
     * @param obj the other object to compare
     * @return true if obj is a Player with the same name as this Player; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && ((Player) obj).getName().equals(this.getName());
    }
}
