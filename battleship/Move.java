package battleship;

import java.util.Hashtable;

/** A move in Battleship.
 *  @author Andrew Fang */
class Move {

    /** Return a move of the piece at COLUMN, ROW*/
    static Move create(int column, int row) {
        int scaledc = SCALE1 * column;
        int scaledr = SCALE2 * row;
        int hashcode = scaledc + scaledr;
        if (saved.containsKey(hashcode)) {
            return saved.get(hashcode);
        } else {
            Move m = new Move(column, row);
            saved.put(hashcode, m);
            return m;
        }
    }

    /** Return a move designated by the string S. */
    static Move create(String s) {
        int c = s.charAt(0) - CONVFACTOR;
        int r = Integer.parseInt(s.substring(1, 2));
        return create(c, r);
    }

    /** The conversion factor for column letter to number. */
    static final int CONVFACTOR = 96;

    /** A new Move of the piece at COL0, ROW0 to COL1, ROW1. */
    private Move(int col, int row) {
        _col = col;
        _row = row;

    }

    /** Return the column of this move. */
    int getCol() {
        return _col;
    }

    /** Return the row at which this move starts.*/
    int getRow() {
        return _row;
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        int scaledc = SCALE1 * _col;
        int scaledr = SCALE2 * _row;
        return scaledc + scaledr;
    }

    @Override
    public String toString() {
        String s = "%c%d";
        char charCol = (char) (_col + COL);
        return String.format(s, charCol, _row);
    }

    /** The conversion from Col# to letter. */
    private static final char COL = 96;

    /** Scaling factor 1 for hashing.
     *  These numbers are specifically chosen so that on a 8x8 grid,
     *  every single move maps to a different number. */
    private static final int SCALE1 = 17;

    /** Scaling factor 2 for hashing. */
    private static final int SCALE2 = 170887;

    /** A hashtable containing all the possible moves. */
    private static Hashtable<Integer, Move> saved
        = new Hashtable<Integer, Move>();

    /** Column and row numbers of starting and ending points. */
    private int _col, _row;

}
