package battleship;

import static battleship.Shipname.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import ucb.util.Stopwatch;

/** Represents a player.  Extensions of this class do the actual playing.
 *  @author Andrew Fang
 */
public abstract class Player {

    /** A player that plays the SIDE pieces in GAME. */
    Player(Side side, SelfBoard self, EnemyBoard opponent) {
        _side = side;
	_myBoard = self;
	_enemyBoard = opponent;
        _stopwatch = new Stopwatch();
        _stopwatchOn = false;
	_ships = new LinkedList<Ship>();
	_ships.add(new Ship(CARRIER));
	_ships.add(new Ship(BATTLESHIP));
	_ships.add(new Ship(SUBMARINE));
	_ships.add(new Ship(DESTROYER));
	_ships.add(new Ship(PATROL));
    }

    /** Sets up a game based on the given INP. */
    abstract void gameSetup(Scanner inp);

    /** Checks to see if all ships are destroyed. */
    void checkShips() {
	Iterator iter = _ships.iterator();
	while (iter.hasNext()) {
	    Ship next = (Ship) iter.next();
	    if (next.destroyed()) {
		iter.remove();
	    }
	}
    }

    /** Returns the number of ships left. */
    int shipsleft() {
	return _ships.size();
    }

    /** Returns whether player has been finished. */
    boolean finished() {
	return _ships.size() == 0;
    }

    /** Sets the enemy board to E. */
    void setEnemyBoard(EnemyBoard e) {
	_enemyBoard = e;
    }

    /** Returns myboard. */
    SelfBoard myBoard() {
	return _myBoard;
    }

    /** Return which side I'm playing. */
    Side side() {
        return _side;
    }

    /** Return the biology of the player. */
    Bio getBio() {
        return _biology;
    }

    /** Set the biology of the player to B. */
    void setBio(Bio b) {
        _biology = b;
    }

    /** Returns my stopwatch. */
    Stopwatch stopwatch() {
        return _stopwatch;
    }

    /** Returns true iff stopwatch is running. */
    boolean isOn() {
        return _stopwatchOn;
    }

    /** Starts the stopwatch. */
    void startStopwatch() {
        _stopwatch.reset();
        _stopwatchOn = true;
    }

    /** My stopwatch. */
    private Stopwatch _stopwatch;
    /** Is my stopwatch on. */
    private boolean _stopwatchOn;
    /** The biology of the player (AI or HUMAN). */
    private Bio _biology;
    /** This player's side. */
    private final Side _side;
    /** The ships this player has. */
    private LinkedList<Ship> _ships;
    /** The opponent's board. */
    private EnemyBoard _enemyBoard;
    /** My board. */
    private SelfBoard _myBoard;
}
