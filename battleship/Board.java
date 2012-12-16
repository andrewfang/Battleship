package battleship;

import java.util.LinkedList;
import java.util.Queue;

import static loa.Piece.*;
import static loa.Side.*;

/** Represents the state of a game of Lines of Action. A Board is immutable.
 *  Its MutableBoard subclass allows moves to be made.
 *  @author Andrew Fang
 */
class Board {

    /** A Board whose initial contents are taken from
     *  INITIALCONTENTS and in which it is PLAYER's move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row-1][col-1]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     */
    Board(Piece[][] initialContents, Side player) {
        assert player != null && initialContents.length == 8;
        _whoseTurn = player;
        _movesMade = 0;
        _moves = new LinkedList<Move>();
        _atePiece = new LinkedList<Boolean>();
        int index = 0;
        for (Piece[] x : initialContents) {
            System.arraycopy(x, 0, _config[index], 0, 8);
            index++;
        }
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BLACK);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        _whoseTurn = board.turn().opponent();
        _movesMade = board.movesMade();
        Piece[][] oldconfig = board.getConfig();
        int index = 0;
        for (Piece[] x : oldconfig) {
            System.arraycopy(x, 0, _config[index], 0, 8);
            index++;
        }
    }

    /** Return the contents of column C, row R, where 1 <= C,R <= 8,
     *  where column 1 corresponds to column 'a' in the standard
     *  notation. */
    Piece get(int c, int r) {
        assert 1 <= c && c <= 8 && 1 <= r && r <= 8;
        return _config[8 - r][c - 1];
    }

    /** Return the contents of the square SQ.  SQ must be the
     *  standard printed designation of a square (having the form cr,
     *  where c is a letter from a-h and r is a digit from 1-8). */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /** Return the column number (a value in the range 1-8) for SQ.
     *  SQ is as for {@link get(String)}. */
    static int col(String sq) {
        return sq.charAt(0) - CONVFACTOR;
    }

    /** Return the row number (a value in the range 1-8) for SQ.
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
        } else if (get(move.getCol0(), move.getRow0()).side() != turn()) {
            return false;
        } else if (isBlocked(move)) {
            return false;
        }  else if (get(move.getCol1(), move.getRow1()).side() == turn()) {
            return false;
        } else if (move.length() != moveSpaces(move)) {
            return false;
        }
        return true;
    }

    /** Return a list of all legal moves. */
    LinkedList<Move> legalMoves() {
        LinkedList<Move> movelist = new LinkedList<Move>();
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                for (int i = 1; i < 9; i++) {
                    for (int j = 1; j < 9; j++) {
                        Move m = Move.create(x, y, i, j);
                        if (isLegal(m)) {
                            movelist.add(m);
                        }
                    }
                }
            }
        }
        return movelist;
    }

    /** Returns the number of steps move M can go in move's direction. */
    int moveSpaces(Move m) {
        int x = m.getCol0();
        int y = m.getRow0();
        Direction d = m.direction();
        int spaces = 0;
        switch (d) {
        case UP:
        case DOWN:
            for (int i = 1; i < 9; i++) {
                if (get(x, i).side() != null) {
                    spaces++;
                }
            }
            break;
        case RIGHT:
        case LEFT:
            for (int j = 1; j < 9; j++) {
                if (get(j, y).side() != null) {
                    spaces++;
                }
            }
            break;
        case UPRIGHT:
        case DOWNLEFT:
            int x1 = x - Math.min(x, y) + 1;
            int y1 = y - Math.min(x, y) + 1;
            while (x1 < 9 && y1 < 9) {
                if (get(x1, y1).side() != null) {
                    spaces++;
                }
                x1++;
                y1++;
            }
            break;
        case UPLEFT:
        case DOWNRIGHT:
            int x2 = x;
            int y2 = y;
            while (x2 < 8 && y2 > 1) {
                x2++;
                y2--;
            }
            while (x2 >= 1 && y2 <= 8) {
                if (get(x2, y2).side() != null) {
                    spaces++;
                }
                x2--;
                y2++;
            }
            break;
        default:
            break;
        }
        return spaces;
    }

    /** Returns true if there is an opponent piece in M's path. */
    boolean isBlocked(Move m) {
        int x = m.getCol0();
        int y = m.getRow0();
        Direction d = m.direction();
        int len = m.length();
        while (len > 1) {
            switch (d) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case RIGHT:
                x++;
                break;
            case LEFT:
                x--;
                break;
            case UPRIGHT:
                x++;
                y++;
                break;
            case DOWNLEFT:
                x--;
                y--;
                break;
            case UPLEFT:
                x--;
                y++;
                break;
            case DOWNRIGHT:
                x++;
                y--;
                break;
            default:
                break;
            }
            if (get(x, y).side() == turn().opponent()) {
                return true;
            }
            len--;
        }
        return false;
    }

    /** Return true iff the game is currently over.  A game is over if
     *  either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BLACK) || piecesContiguous(WHITE);
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
        System.out.println("Not allowed");
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        System.out.println("Not allowed");
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
