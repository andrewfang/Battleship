package loa;

import static loa.Side.*;
import java.util.Scanner;
import ucb.util.Stopwatch;

/** Represents a player.  Extensions of this class do the actual playing.
 *  @author Andrew Fang
 */
public abstract class Player {

    /** A player that plays the SIDE pieces in GAME. */
    Player(Side side, Game game) {
        _side = side;
        _game = game;
        _stopwatch = new Stopwatch();
        _stopwatchOn = false;
    }

    /** Makes a decision about what to do using INP.
     *  Returns true iff we want to instantiate an AI.*/
    abstract boolean decide(Scanner inp);

    /** Return which side I'm playing. */
    Side side() {
        return _side;
    }

    /** Return the board I am using. */
    Board getBoard() {
        return _game.getBoard();
    }

    /** Return the game I am playing. */
    Game getGame() {
        return _game;
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
    /** The game this player is part of. */
    private Game _game;

}
