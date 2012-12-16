package battleship;

/** Indicates a marker that is used to tell if the move
 *  actually hits something.
 *  @author Andrew Fang */

enum Marker {
    /** The names of the possibilities. */
    HIT, MISS;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
