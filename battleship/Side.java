package battleship;

/** Indicates a side.
 *  @author Andrew Fang */
enum Side {
    /** The names of the two sides. */
    PLAYER1, PLAYER2;

    /** Return the opposing color. */
    Side opponent() {
        return this == PLAYER1 ? PLAYER2 : PLAYER1;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
