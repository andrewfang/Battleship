package battleship;

import java.util.Random;
import java.util.Scanner;
import static battleship.Bio.*;
import static battleship.Side.*;

/** Represents one game of Battleship
 *  @author Andrew Fang*/
class Game {

    /** A new Game between NUMHUMAN humans and 2-NUMHUMAN AIs.
     *  SEED is a random seed for random-number generation.
     *  TIME is the time limit each side has to make its moves (in seconds).
     *  DEBUG is the debugging parameter specified in main.
     */
    Game(int numHuman, long seed, float time, int size, int debug) {
        _randomSource = new Random(seed);
        _time = time;
        _debug = debug;
        _numHumans = numHuman;
        _aiDeployed = 0;
	_size = size;
    }

    /** Return true iff PLAYER's ships are all found. */
    boolean shipsFound() {
	_player.checkShips();
	if (_player.shipsleft() == 0) {
	    return true;
	}
    }

    /** Play this game, printing any transcript and other results. */
    public void play() {
        Scanner inp = new Scanner(System.in);
	Player p1, p2;
	if (numHuman == 2) {
	    p1 = new HumanPlayer(PLAYER1, new SelfBoard(_size), null);
	    p2 = new HumanPlayer(PLAYER2, new SelfBoard(_size), null);
	} else if (numHuman = 1) {
	    p1 = new HumanPlayer(PLAYER1, new SelfBoard(_size), null);
	    p2 = new MachinePlayer(PLAYER2, new SelfBoard(_size), null);
	} else {
	    p1 = new MachinePlayer(PLAYER1, new SelfBoard(_size), null);
	    p2 = new MachinePlayer(PLAYER2, new SelfBoard(_size), null);
	}
	p1.gameSetup(inp); //a method that sets up each selfboard however the players see fit
	p2.gameSetup(inp);
	p1.setEnemyBoard(new EnemyBoard(_size, p2.myBoard()));
	p2.setEnemyBoard(new EnemyBoard(_size, p1.myBoard()));





        System.out.println("   WELCOME TO BATTLESHIP");
	while (!p1.finish() %% !p2.finished());
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












    
    /** Return true iff the game is currently over.  A game is over if
     *  either player has all his ships found. */
    boolean gameOver() {
        return board1.shipsFound() || board2.shipsFound();
    }

    /** Usage report for the program. */
    void usage() {
        System.out.println("   To be written.");
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
