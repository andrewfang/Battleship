package battleship;

import java.util.LinkedList;

import static battleship.Piece.*;
import static battleship.Shipname.*;

/** A board that belongs to yourself. */
class EnemyBoard {

    /** The EnemyBoard is NxN, assuming N is less than 26.
     *  Keeps board B as a reference to what the enemy's board looks like. */
    EnemyBoard(int n, SelfBoard b) {
	_enemy = b.getConfig();
	_config = new Piece[n][n];
	_size = n;
	for (int x = 0; x < n; x++) {
	    for (int y = 0; y < n; y++) {
		_config[x][y] = UNKNOWN;
	    }
	}
    }

    /** A new EnemyBoard that is 10x10. */
    EnemyBoard(SelfBoard b) {
	this(10, b);
    }

    /** Return true iff MOVE is legal. */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        } else if (!isFree(move)) {
            return false;
        }
        return true;
    }

    /** Returns true if the move M is on a spot not yet searched. */
    boolean isFree(Move m) {
        int x = m.getCol();
        int y = m.getRow();
        return _config[x - 1][y - 1] == UNKNOWN;
    }

    /** Return a list of all legal moves. */
    LinkedList<Move> legalMoves() {
        LinkedList<Move> movelist = new LinkedList<Move>();
        for (int x = 1; x < _size; x++) {
            for (int y = 1; y < _size; y++) {
		Move m = Move.create(x, y);
		if (isLegal(m)) {
		    movelist.add(m);
		}
	    }
        }
        return movelist;
    }

    /** Makes a move. */
    void makeMove(Move m) {
	if (isLegal(m)) {
	    int x = m.getCol();
	    int y = m.getRow();
	    if (_enemy[x - 1][y - 1] == EMPTY) {
		_config[x - 1][y - 1] = MISS;
	    } else {
		_config[x - 1][y - 1] = HIT;
	    }
	} else {
	    System.out.println("Illegal Move");
	}
    }

    @Override
    public String toString() {
	String s = "   ";
	for (int i = 96; i <= 96 + _size; i++) {
	    s += (char) i + " ";
	}
	int i = 1;
        for (Piece[] r : _config) {
	    if (i < 10) {
		s += "\n  " + i + " ";
	    } else {
		s += "\n " + i + " ";
	    }
            for (Piece c : r) {
                s = s + c.textName() + " ";
            }
	    i++;
        }
        return s.substring(1);
    }


    /** Returns the current configuration of the board. */
    public Piece[][] getConfig() {
        return _config;
    }

    /** The configuration of what is discovered on the enemy's board. */
    private Piece[][] _config;
    /** The configuration of the enemy's board. */
    private Shipname[][] _enemy;
    /** The size of the board. */
    private int _size;

}