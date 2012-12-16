package loa;

import java.util.Iterator;
import java.util.Scanner;
import static loa.Bio.*;
import static loa.Side.*;

/** An automated Player.
 *  @author Andrew Fang*/
class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Side side, Game game) {
        super(side, game);
        setBio(MACHINE);
    }

    @Override boolean decide(Scanner unused) {
        stopwatch().start();
        Move m = guessBestMove(side(), getBoard(), 9);
        stopwatch().stop();
        if (stopwatch().getAccum() / THOU > getGame().getTime() * SEC) {
            System.out.println("   You ran out of time.");
            if (side() == WHITE) {
                System.out.println("Black wins.");
            } else {
                System.out.println("White wins.");
            }
            System.exit(0);
        }
        getBoard().makeMove(m);
        if (side() == BLACK) {
            System.out.print("B::");
        } else {
            System.out.print("W::");
        }
        System.out.println(m);
        return true;
    }

    /** Returns a move for SIDE that has an estimated value >= CUTOFF
     *  or has the best estimated value for SIDE, starting from board
     *  START, and looking up to DEPTH moves ahead.
     *  Method adapted from the Data Structures Textbook. */
    Move findBestMove(Side side, Board start, int depth, double cutoff) {
        if (start.piecesContiguous(side)) {
            return guessBestMove(side, start, cutoff);
        }
        if (depth == 0) {
            return guessBestMove(side, start, cutoff);
        }
        Move bestMove = guessBestMove(side, start, cutoff);
        int bestScore;
        Iterator iter = start.legalMoves().listIterator();
        bestScore = Integer.MIN_VALUE;
        while (iter.hasNext()) {
            Move next = (Move) iter.next();
            int s1 = start.score(side, next);
            start.makeMove(next);
            Move response
                = findBestMove(side.opponent(), start, depth - 1, cutoff);
            int s2 = start.score(side.opponent(), response);
            if ((s1 - s2) > bestScore) {
                bestScore = s1 - s2;
                bestMove = next;
            }
            if (bestScore >= cutoff) {
                break;
            }
            start.retract();
        }
        return bestMove;
    }

    /** Auxillary function for findBestMove, that returns
     *  the best move for SIDE, starting from board START,
     *  Stops if a move is higher than CUTOFF.
     *  Adapted from the Data Structures Textbook. */
    Move guessBestMove(Side side, Board start, double cutoff) {
        Move bestMove;
        int bestScore;
        Iterator iter = start.legalMoves().listIterator();
        bestMove = (Move) iter.next();
        int s1 = start.score(side, bestMove);
        bestScore = s1;
        while (iter.hasNext()) {
            Move next = (Move) iter.next();
            s1 = start.score(side, next);
            boolean random = getGame().getRandomSource().nextBoolean();
            if (s1 > bestScore || (s1 == bestScore && random)) {
                bestMove = next;
                bestScore = s1;
                if (s1 >= cutoff) {
                    break;
                }
            }
        }
        return bestMove;
    }

    /** Returns a random move chosen from all possible moves. */
    Move randomMove() {
        Iterator moves = getBoard().legalMoves().listIterator();
        int size = getBoard().legalMoves().size();
        int index = (getGame().getRandomSource().nextInt(9 * 9) % size);
        return getBoard().legalMoves().get(index);
    }

    /** The number of seconds in a minute. */
    private static final int SEC = 60;

    /** Ten to the power of three. */
    private static final int THOU = 1000;

}
