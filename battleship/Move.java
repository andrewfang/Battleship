package loa;

import java.util.Hashtable;
import static loa.Direction.*;

/** A move in Lines of Action.
 *  @author Andrew Fang */
class Move {

    /* Implementation note: We create moves by means of static "factory
     * methods" all named create, which in turn use the single (private)
     * constructor.  Factory methods have certain advantages over constructors:
     * they allow you to produce results having an arbitrary subtype of Move,
     * and they don't require that you produce a new object each time.  This
     * second advantage is useful when you are trying to speed up the creation
     * of Moves for use in automated searching for moves.  You can (if you
     * want) create just one instance of the Move representing 1-5, for example
     * and return it whenever that move is requested. */

    /** Return a move of the piece at COLUMN0, ROW0 to COLUMN1, ROW1. */
    static Move create(int column0, int row0, int column1, int row1) {
        int scaledc0 = SCALE1 * column0;
        int scaledr0 = SCALE2 * row0;
        int scaledc1 = SCALE3 * column1;
        int scaledr1 = SCALE4 * row1;
        int hashcode = scaledc0 + scaledr0 + scaledc1 + scaledr1;
        if (saved.containsKey(hashcode)) {
            return saved.get(hashcode);
        } else {
            Move m = new Move(column0, row0, column1, row1);
            saved.put(hashcode, m);
            return m;
        }
    }

    /** Return a move designated by the string S. */
    static Move create(String s) {
        int c0 = s.charAt(0) - CONVFACTOR;
        int c1 = s.charAt(3) - CONVFACTOR;
        int r0 = Integer.parseInt(s.substring(1, 2));
        int r1 = Integer.parseInt(s.substring(4, 5));
        return create(c0, r0, c1, r1);
    }

    /** The conversion factor for column letter to number. */
    static final int CONVFACTOR = 96;

    /** A new Move of the piece at COL0, ROW0 to COL1, ROW1. */
    private Move(int col0, int row0, int col1, int row1) {
        _col0 = col0;
        _row0 = row0;
        _col1 = col1;
        _row1 = row1;

    }

    /** Return the column at which this move starts, as an index in 1--8. */
    int getCol0() {
        return _col0;
    }

    /** Return the row at which this move starts, as an index in 1--8. */
    int getRow0() {
        return _row0;
    }

    /** Return the column at which this move ends, as an index in 1--8. */
    int getCol1() {
        return _col1;
    }

    /** Return the row at which this move ends, as an index in 1--8. */
    int getRow1() {
        return _row1;
    }

    /** Return the length of this move (number of squares moved). */
    int length() {
        return Math.max(Math.abs(_row1 - _row0), Math.abs(_col1 - _col0));
    }

    /** Returns the direction in which the move is made. */
    Direction direction() {
        if (_col0 == _col1) {
            if (_row0 < _row1) {
                return UP;
            } else {
                return DOWN;
            }
        } else if (_row0 == _row1) {
            if (_col0 < _col1) {
                return RIGHT;
            } else {
                return LEFT;
            }
        } else if ((_row0 - Math.min(_col0, _row0))
                   == (_row1 - Math.min(_col1, _row1))
                   && (_col0 - Math.min(_col0, _row0))
                   == (_col1 - Math.min(_col1, _row1))) {
            if (_col0 < _col1) {
                return UPRIGHT;
            } else {
                return DOWNLEFT;
            }
        } else if (_col0 + _row0 == _col1 + _row1) {
            if (_col0 < _col1) {
                return DOWNRIGHT;
            } else {
                return UPLEFT;
            }
        } else {
            return INVALID;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) {
            return false;
        }
        Move move2 = (Move) obj;
        boolean c0eq = this.getCol0() == move2.getCol0();
        boolean r0eq = this.getRow0() == move2.getRow0();
        boolean c1eq = this.getCol1() == move2.getCol1();
        boolean r1eq = this.getRow1() == move2.getRow1();
        return c0eq && r0eq && c1eq && r1eq;
    }

    @Override
    public int hashCode() {
        int scaledc0 = SCALE1 * _col0;
        int scaledr0 = SCALE2 * _row0;
        int scaledc1 = SCALE3 * _col1;
        int scaledr1 = SCALE4 * _row1;
        return scaledc0 + scaledr0 + scaledc1 + scaledr1;
    }

    @Override
    public String toString() {
        String s = "%c%d-%c%d";
        char start = (char) (_col0 + COL);
        char end = (char) (_col1 + COL);
        return String.format(s, start, _row0, end, _row1);
    }

    /** The conversion from Col# to letter. */
    private static final char COL = 96;

    /** Scaling factor 1 for hashing.
     *  These numbers are specifically chosen so that on a 8x8 grid,
     *  every single move maps to a different number. */
    private static final int SCALE1 = 17;

    /** Scaling factor 2 for hashing. */
    private static final int SCALE2 = 170887;

    /** Scaling factor 3 for hashing. */
    private static final int SCALE3 = 997;

    /** Scaling factor 4 for hashing. */
    private static final int SCALE4 = 4123211;

    /** A hashtable containing all the possible moves. */
    private static Hashtable<Integer, Move> saved
        = new Hashtable<Integer, Move>();

    /** Column and row numbers of starting and ending points. */
    private int _col0, _row0, _col1, _row1;

}
