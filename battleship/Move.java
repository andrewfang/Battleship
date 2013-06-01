package battleship;

import java.util.Hashtable;

/** A move in Battleship.
 *  @author Andrew Fang */
class Move {

    /** A new Move of the piece at COL0, ROW0 to COL1, ROW1. */
     Move(int col, int row) {
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
    public String toString() {
        String s = "%c%d";
        char charCol = (char) (_col + CONVFACTOR);
        return String.format(s, charCol, _row);
    }

    /** Column and row numbers of starting and ending points. */
    private int _col, _row;
    /** The conversion factor for column letter to number. */
    static final int CONVFACTOR = 96;

}
