package battleship;

import static battleship.Shipname.*;

/** A board that belongs to yourself. */
class SelfBoard {

    /** The SelfBoard is NxN, assuming N is less than 26.
     *  Does nothing with the given board B. */
    SelfBoard(int n) {
	assert n > 7 && n < 27;
	_size = n;
	_config = new Shipname[n][n];
	for (int x = 0; x < n; x++) {
	    for (int y = 0; y < n; y++) {
		_config[x][y] = EMPTY;
	    }
	}
    }

    /** A new SelfBoard that is 10x10. */
    SelfBoard() {
	this(10);
    }
       
    @Override
    public String toString() {
	String s = "   ";
	for (int i = 96; i <= 96 + _size; i++) {
	    s += (char) i + " ";
	}
	int i = 1;
        for (Shipname[] r : _config) {
	    if (i < 10) {
		s += "\n  " + i + " ";
	    } else {
		s += "\n " + i + " ";
	    }
            for (Shipname c : r) {
                s = s + c.symbol() + " ";
            }
	    i++;
        }
        return s.substring(1);
    }

    /** Places SHIP from coordinates x1 to x2. */
    void place(Ship ship, Coordinate coord) {
	if (!coord.legal()) {
	    System.out.println("Ships can only be placed horizontally or vertically.");
	} else if (ship.size() != coord.length()) {
	    System.out.println("Coordinates are not the length of the ship.");
	} else {
	    int c0 = coord.c0();
	    int c1 = coord.c1();
	    int r0 = coord.r0();
	    int r1 = coord.r1();
	    int index1 = 0;
	    int index2 = 0;
	    int constant = 0;
	    if (c0 == c1) {
		constant = c0 - 1;
		index1 = Math.min(r0, r1);
		index2 = Math.max(r0, r1);
		while (index1 <= index2) {
		    _config[index1 - 1][constant] = ship.name();
		    index1++;
		}
	    } else {
		index1 = Math.min(c0, c1);
		index2 = Math.max(c0, c1);
		constant = r0 - 1;
		while (index1 <= index2) {
		    _config[constant][index1 - 1] = ship.name();
		    index1++;
		}
	    }
	}
    }

    /** Returns the current configuration of the board. */
    public Shipname[][] getConfig() {
        return _config;
    }
    /** The configuration of the board. */
    private Shipname[][] _config;
    /** The size of the board. */
    private int _size;

}