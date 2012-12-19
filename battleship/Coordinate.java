package battleship;

import java.util.Scanner;
import java.util.regex.MatchResult;

/** Coordinate is used to place a ship. */
class Coordinate {
    
    /** A coordinate that stretches from (c0, r0) to (c1, r1). */
    Coordinate(String coord) {
	String regex = "([A-Za-z])(\\d)-([A-Za-z])(\\d)";
	Scanner inp = new Scanner(coord);
	if (inp.hasNext(regex)) {
	    MatchResult m = inp.match();
	    _c0 = m.group(1).charAt(0) - CONVFACTOR;
	    _r0 = Integer.parseInt(m.group(2));
	    _c1 = m.group(3).charAt(0) - CONVFACTOR;
	    _r1 = Integer.parseInt(m.group(4));
	}
    }

    /** Ships can only be placed horizontally or vertically.*/
    boolean legal() {
	return _c0 == _c1 || _r0 == _r1;
    }

    /** Returns the length of the placement. */
    int length() {
	return Math.abs(_c0 - _c1) + Math.abs(_r0 - _r1) + 1;
    }
    /** Returns c0. */
    int c0() {
	return _c0;
    }

    /** Returns r0. */
    int r0() {
	return _r0;
    }

    /** Returns c1. */
    int c1() {
	return _c1;
    }

    /** Returns r1. */
    int r1() {
	return _r1;
    }

    /** Conversion factor from char to int. */
    private static final int CONVFACTOR = 96;
    /** Column 0, 1, Row 0, 1. */
    private int _c0, _r0, _c1, _r1;
}