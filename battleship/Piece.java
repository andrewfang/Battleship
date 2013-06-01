package battleship;

/** A Piece denotes the contents of a square.
 *  @author Andrew Fang */
enum Piece {
    /** The names of the pieces.  UNKNOWN indicates an unvisited square. */
    HIT("x"), MISS("~"),  UNKNOWN("?");

    /** The textual representation of this piece. */
    private String _textName;

    /** A Piece with the given MARKER that uses TEXTNAME as its printed
     *  contents. */
    Piece(String textName) {
        _textName = textName;
    }

    /** Returns the one-character denotation of this piece on the board. */
    String textName() {
	return _textName;
    }
}
