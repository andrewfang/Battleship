package loa;

import static loa.Piece.*;
import static loa.Side.*;

/** Represents the state of a game of Lines of Action, and allows making moves.
 *  @author Andrew Fang */
class MutableBoard extends Board {

    /** A MutableBoard whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     */
    MutableBoard(Piece[][] initialContents, Side player) {
        super(initialContents, player);
    }

    /** A new board in the standard initial position. */
    MutableBoard() {
        super();
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    MutableBoard(Board board) {
        super(board);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    @Override void makeMove(Move move) {
        if (isLegal(move)) {
            incrMoves(1);
            addMove(move);
            setBoard(move);
            turnDone();
        } else {
            System.out.println("Invalid Move");
        }
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    @Override void retract() {
        if (movesMade() > 0) {
            turnDone();
            backBoard();
            incrMoves(-1);
        }
    }


}
