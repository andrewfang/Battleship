package battleship;

import static battleship.Marker.*;

/** A Piece denotes the contents of a square.
 *  @author Andrew Fang */
enum Piece {
    /** The names of the pieces.  EMP indicates an empty square. */
    H(HIT, "x"), M(MISS, "-"),  EMP(null, "?");

    /** The category this piece belongs to. */
    private Marker _marker;
    /** The textual representation of this piece. */
    private String _textName;

    /** A Piece with the given MARKER that uses TEXTNAME as its printed
     *  contents. */
    Piece(Marker marker, String textName) {
        _marker = marker;
        _textName = textName;
    }

    /** Returns the marker specfication, or null
     *  for an empty square. */
    Marker marker() { return _marker; }

    /** Returns the one-character denotation of this piece on the board. */
    String textName() { return _textName; }
}
