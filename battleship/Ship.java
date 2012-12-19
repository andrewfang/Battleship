package battleship;

/** Denotes a ship.
 *  @author Andrew Fang */
class Ship {

    /** A Ship with the given NAME, and not destroyed. */
    Ship(Shipname name) {
	_name = name;
	_destroyed = false;
	_size = name.size();
    }

    /** Returns the size of the ship. */
    int size() {
	return _size;
    }

    /** A hit on the ship decreases size by 1.
     *  When the size is 0, it is destroyed. */
    void hit() {
	_size -= 1;
	if (_size <= 0) {
	    destroy();
	}
    }

    /** Changes _destroyed to true. */
    void destroy() {
	_destroyed = true;
    }

    /** Returns if the ship is destroyed. */
    boolean destroyed() {
	return _destroyed;
    }

    /** Returns the shipname. */
    Shipname name() {
	return _name;
    }

    /** The name of the ship. */
    private Shipname _name;
    /** The size of this ship. */
    private int _size;
    /** Whether the ship has been destroyed already. */
    private boolean _destroyed;
}