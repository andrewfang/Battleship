package loa;

import java.util.Random;
import java.util.Scanner;
import static loa.Bio.*;
import static loa.Side.*;

/** Represents one game of Lines of Action.
 *  @author Andrew Fang*/
class Game {

    /** A new Game between NUMHUMAN humans and 2-NUMHUMAN AIs.  SIDE0
     *  indicates which side the first player (known as ``you'') is
     *  playing.  SEED is a random seed for random-number generation.
     *  TIME is the time limit each side has to make its moves (in seconds).
     *  DEBUG is the debugging parameter specified in main.
     */
    Game(int numHuman, Side side0, long seed, float time, int debug) {
        _randomSource = new Random(seed);
        _time = time;
        _debug = debug;
        _side = side0;
        _numHumans = numHuman;
        _aiDeployed = 0;
        _board = new MutableBoard();
    }

    /** Return the current board. */
    Board getBoard() {
        return _board;
    }

    /** Play this game, printing any transcript and other results. */
    public void play() {
        Scanner inp = new Scanner(System.in);
        Player p1 = new HumanPlayer(Side.BLACK, this);
        Player p2 = new HumanPlayer(Side.WHITE, this);
        if (_numHumans == 2) {
            p1.startStopwatch();
            p2.startStopwatch();
        }
        System.out.println("   WELCOME TO THE GAME OF LOA");
        usage();
        while (!_board.gameOver()) {
            if (_board.turn() == p1.side()) {
                if (p1.getBio() == HUMAN) {
                    System.out.print(_board.turn() + "> ");
                    System.out.flush();
                    if (p1.decide(inp)) {
                        if (_numHumans == 0) {
                            p1 = setAI(p1);
                            p2 = setAI(p2);
                        } else if (_side == BLACK) {
                            p2 = setAI(p2);
                        } else {
                            p1 = setAI(p1);
                        }
                        p1.startStopwatch();
                        p2.startStopwatch();
                    }
                } else {
                    p1.decide(inp);
                }
            } else {
                if (p2.getBio() == HUMAN) {
                    System.out.print(_board.turn() + "> ");
                    System.out.flush();
                    if (p2.decide(inp)) {
                        if (_numHumans == 0) {
                            p1 = setAI(p1);
                            p2 = setAI(p2);
                        } else if (_side == WHITE) {
                            p1 = setAI(p1);
                        } else {
                            p2 = setAI(p2);
                        }
                        p1.startStopwatch();
                        p2.startStopwatch();
                    }
                } else {
                    p2.decide(inp);
                }
            }
        }
        if (_board.piecesContiguous(Side.BLACK)) {
            System.out.println("Black wins.");
        } else if (_board.piecesContiguous(Side.WHITE)) {
            System.out.println("White wins.");
        }
        System.exit(0);
    }

    /** Usage report for the program. */
    void usage() {
        System.out.println("   Commands:\tEffects:");
        System.out.println("   s\t\tShows the board, side, # of moves.");
        System.out.println("   p\t\tStarts any AI if specified.");
        System.out.println("   t\t\tShows your remaining time");
        System.out.println("   q\t\tQuits the program. Ends game.");
        System.out.println("   c1r1-c2r2\tMove piece c1r1 to c2r2 (eg, b8-b6)");
        String a = "   #\t\tAnything following this is a comment.";
        System.out.println(a + " Ignored.");
    }

    /** Returns a MachinePlayer AI if needed, else returns P. */
    Player setAI(Player p) {
        if (_numHumans == 2) {
            System.out.println("   There are two humans playing.");
            return p;
        } else if (_numHumans == 1) {
            if (_aiDeployed == 0) {
                _aiDeployed++;
                return new MachinePlayer(p.side(), p.getGame());
            } else {
                System.out.println("All AIs have been deployed.");
                return p;
            }
        } else {
            if (_aiDeployed < 2) {
                _aiDeployed++;
                return new MachinePlayer(p.side(), p.getGame());
            } else {
                return p;
            }
        }
    }

    /** Return the random number generator for this game. */
    Random getRandomSource() {
        return _randomSource;
    }

    /** The official game board. */
    private MutableBoard _board;

    /** A source of random numbers, primed to deliver the same sequence in
     *  any Game with the same seed value. */
    private Random _randomSource;

    /** Returns _time. */
    float getTime() {
        return _time;
    }

    /** The max time limit for each turn, in minutes. */
    private float _time;

    /** The debugging parameter specified in Main. */
    private int _debug;

    /** The side "you" are on. */
    private Side _side;

    /** Number of humans playing. */
    private int _numHumans;

    /** Number of AIs deployed. */
    private int _aiDeployed;
}
