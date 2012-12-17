package battleship;

import java.util.LinkedList;
import java.util.Queue;

import static battleship.Piece.*;
import static battleship.Side.*;

/** Represents the state of a board in battleship.
 *  @author Andrew Fang
 */
class Board {

    /** A Board whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is NxN.
     */
    Board(Piece[][] initialContents, Side player, int n) {
        assert player != null && initialContents.length == n;
        _whoseTurn = player;
        _movesMade = 0;
	_size = n;
        int index = 0;
        for (Piece[] x : initialContents) {
            System.arraycopy(x, 0, _config[index], 0, 8);
            index++;
        }
    }

    /** A new board in the standard initial position. */
    Board() {
        this(BLANK, PLAYER1, 10);
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= _size,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        assert 1 <= c && c <= _size && 1 <= r && r <= _size;
        return _config[r - 1][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter and r is a digit. */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the column number (a value in the range 1-_size) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int col(String sq) {
        return sq.charAt(0) - CONVFACTOR;
    }

    /** Return the row number (a value in the range 1-_size) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int row(String sq) {
        return Integer.parseInt(sq.substring(1));
    }

    /** Return the Side that is currently next to move. */
    Side turn() {
        return _whoseTurn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        } else if (isFree(move)) {
            return false;
        }
        return true;
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

    /** Returns true if the move M is on a spot not yet searched. */
    boolean isFree(Move m) {
        int x = m.getCol0();
        int y = m.getRow0();
        return get(x, y).marker() == EMP;
    }

    /** Return true iff the game is currently over.  A game is over if
     *  either player has all his ships found. */
    boolean gameOver() {
        return shipsFound(BLACK) || shipsFound(WHITE);
    }

    /** Return true iff PLAYER's pieces are continguous. */
    boolean piecesContiguous(Side player) {
        int numPieces = 0;
        PieceNode parent = null;
        int[][] n = { {-1, -1}, {-1, 0}, {-1, 1},
                      {0, 1}, {1, 1}, {1, 0}, {1, -1}, {-1, 0} };
        for (int r = 1; r < 9; r++) {
            for (int c = 1; c < 9; c++) {
                if (get(c, r).side() == player) {
                    if (numPieces == 0) {
                        parent = new PieceNode(c, r);
                    }
                    numPieces++;
                }
            }
        }
        Queue<PieceNode> queue = new LinkedList<PieceNode>();
        LinkedList<Integer> visited = new LinkedList<Integer>();
        queue.offer(parent);
        visited.add(parent.getRow() * SCALE1 + parent.getCol() * SCALE2);
        while (!queue.isEmpty()) {
            PieceNode p = queue.poll();
            int c1, r1;
            for (int[] x : n) {
                c1 = p.getCol() + x[0];
                r1 = p.getRow() + x[1];
                if (1 <= c1 && c1 <= 8 && 1 <= r1 && r1 <= 8) {
                    if (get(c1, r1).side() == player) {
                        int hash = r1 * SCALE1 + c1 * SCALE2;
                        if (!visited.contains(hash)) {
                            visited.add(hash);
                            queue.offer(new PieceNode(c1, r1));
                        }
                    }
                }
            }
        }
        return numPieces == visited.size();
    }


    /** Returns the score of a move M by PLAYER. */
    int score(Side player, Move m) {
        int c0 = m.getCol0();
        int r0 = m.getRow0();
        int c1 = m.getCol1();
        int r1 = m.getRow1();
        if (movesMade() < 10) {
            if (c1 < 3 || c1 > 6 || r1 < 3 || r1 > 6) {
                return -10;
            }
        }
        int scorebefore = findScore(player, c0, r0);
        makeMove(m);
        int scoreafter = findScore(player, c1, r1);
        retract();
        return scoreafter - scorebefore;
    }

    /** Return the number of contiguous pieces to a piece belonging to
     *  PLAYER at position (C, R). */
    int findScore(Side player, int c, int r) {
        int[][] n = { {-1, -1}, {-1, 0}, {-1, 1},
                      {0, 1}, {1, 1}, {1, 0}, {1, -1}, {-1, 0} };
        PieceNode piece = new PieceNode(c, r);
        Queue<PieceNode> queue = new LinkedList<PieceNode>();
        LinkedList<Integer> visited = new LinkedList<Integer>();
        queue.offer(piece);
        visited.add(piece.getRow() * SCALE1 + piece.getCol() * SCALE2);
        while (!queue.isEmpty()) {
            PieceNode p = queue.poll();
            int c1, r1;
            for (int[] x : n) {
                c1 = p.getCol() + x[0];
                r1 = p.getRow() + x[1];
                if (1 <= c1 && c1 <= 8 && 1 <= r1 && r1 <= 8) {
                    if (get(c1, r1).side() == player) {
                        int hash = r1 * SCALE1 + c1 * SCALE2;
                        if (!visited.contains(hash)) {
                            visited.add(hash);
                            queue.offer(new PieceNode(c1, r1));
                        }
                    }
                }
            }
        }
        return visited.size();
    }

    /** Scaling factor 1 for hashing.
     *  These numbers are specifically chosen so that on a 8x8 grid,
     *  every single spot maps to a different number. */
    private static final int SCALE1 = 17;

    /** Scaling factor 2 for hashing. */
    private static final int SCALE2 = 170887;

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _movesMade;
    }

    /** The total number of moves made. */
    private int _movesMade;

    /** Increments _movesMade by X. */
    void incrMoves(int x) {
        _movesMade += x;
    }

    /** Returns move #K used to reach the current position, where
     *  0 <= K < movesMade().  Does not include retracted moves. */
    Move getMove(int k) {
        return _moves.get(k);
    }

    /** Returns if move #K ate a enemy piece. */
    boolean getAtePiece(int k) {
        return _atePiece.get(k);
    }

    /** The list of moves made thus far. */
    private LinkedList<Move> _moves;

    /** An add-on to _moves saying if the move ate a enemy piece. */
    private LinkedList<Boolean> _atePiece;

    /** Adds a move M to _moves. */
    void addMove(Move m) {
        _moves.add(m);
        if (get(m.getCol1(), m.getRow1()) == EMP) {
            _atePiece.add(false);
        } else {
            _atePiece.add(true);
        }
    }

    /** Removes a move M from _moves. */
    void remMove() {
        _moves.removeLast();
        _atePiece.removeLast();
    }

    /** Sets the board to another state based on M. */
    void setBoard(Move m) {
        int c0 = m.getCol0();
        int c1 = m.getCol1();
        int r0 = m.getRow0();
        int r1 = m.getRow1();
        if (turn() == BLACK) {
            _config[8 - r1][c1 - 1] = BP;
        } else {
            _config[8 - r1][c1 - 1] = WP;
        }
        _config[8 - r0][c0 - 1] = EMP;
    }

    /** Returns a board to a previous state. */
    void backBoard() {
        Move m = _moves.removeLast();
        boolean ate = _atePiece.removeLast();
        int c0 = m.getCol0();
        int c1 = m.getCol1();
        int r0 = m.getRow0();
        int r1 = m.getRow1();
        if (turn() == BLACK) {
            _config[8 - r0][c0 - 1] = BP;
        } else {
            _config[8 - r0][c0 - 1] = WP;
        }
        if (ate) {
            if (turn() == BLACK) {
                _config[8 - r1][c1 - 1] = WP;
            } else {
                _config[8 - r1][c1 - 1] = BP;
            }
        } else {
            _config[8 - r1][c1 - 1] = EMP;
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (Piece[] r : _config) {
            s += "\n ";
            for (Piece c : r) {
                s = s + c.textName() + " ";
            }
        }
        return s.substring(1);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        if (isLegal(move)) {
            incrMoves(1);
            addMove(move);
            setBoard(move);
            turnDone();
        } else {
            System.out.println("Invalid Move");
        }
    }

    /** Passes the turn onto the next player. */
    void turnDone() {
        _whoseTurn = _whoseTurn.opponent();
    }

    /** The player who holds the current turn. */
    private Side _whoseTurn;

    /** The configuration of the board. */
    private Piece[][] _config = new Piece[8][8];

    /** Returns the current configuration of the board. */
    public Piece[][] getConfig() {
        return _config;
    }

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** The conversion factor for column letter to number. */
    static final int CONVFACTOR = 96;

}
