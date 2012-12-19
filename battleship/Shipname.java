package battleship;

/** Denotes a name for the ship.
 *  @author Andrew Fang */
enum Shipname {

    /** The various ship names. */
    CARRIER(5, "C"), BATTLESHIP(4, "B"), SUBMARINE(3, "S"),
	DESTROYER(3, "D"), PATROL(2, "P"), EMPTY(0, "~");

    /** Constructor for the ship, with the given SIZE, and given SYMBOL. */
    Shipname(int size, String symbol) {
	_size = size;
	_symbol = symbol;
    }

    /** Returns the size of the ship. */
    int size() {
	return _size;
    }

    /** The ship's symbol. */
    String symbol() {
	return _symbol;
    }

    /** The size of the ship. */
    private int _size;
    /** The ship's symbol. */
    private String _symbol;

}