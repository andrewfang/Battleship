package battleship;

/** Denotes a ship.
 *  @author Andrew Fang */
enum Ship {
    /** The various ships. */
    CARRIER(5, false), BATTLESHIP(4, false), SUBMARINE(3, false), DESTROYER(3, false), PATROL(2, false);

    /** The size of this ship. */
    private int _size;

    /** Whether the ship has been destroyed already. */
    private int _destroyed;

    /** A Ship with the given SIZE. */
    Ship(int size, boolean destroyed) {
	_size = size;
    }

    /** Returns the size of the ship. */
    int size() {
	return _size;
    }

    /** Changes _destroyed to true. */
    void destroy() {
	_destroyed = true;
    }
}