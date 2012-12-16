package loa;

import org.junit.Test;
import static org.junit.Assert.*;
import static loa.Side.*;
import static loa.Piece.*;

/** jUnit test for Board Class.
 *  @author Andrew Fang*/
public class BoardTest {

    @Test public void testBoardConstructors() {
        Board b1 = new Board();
        Board b2 = new Board(b1);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                assertEquals("Copy board fail", b1.get(i, j), b2.get(i, j));
            }
        }
    }

    @Test public void testBoardParam() {
        Board b1 = new Board();
        Move mb = Move.create(2, 1, 4, 3);
        Move mw = Move.create(1, 2, 3, 4);
        Move mb2 = Move.create(2, 1, 5, 6);
        assertEquals("Wrong turn", b1.turn(), BLACK);
        assertEquals("Not your turn", b1.isLegal(mw), false);
        assertEquals("Too many steps", b1.isLegal(mb2), false);
        assertEquals("Good move", b1.isLegal(mb), true);
    }

    @Test public void testContig() {
        Board b1 = new Board(CONTIGUOUS, BLACK);
        assertEquals("Error in contig", b1.piecesContiguous(BLACK), true);
        assertEquals("No error in contig", b1.piecesContiguous(WHITE), false);
        Board b2 = new Board(SINGLE, BLACK);
        assertEquals("Single unit is contig", b2.piecesContiguous(BLACK), true);
        Board b3 = new Board();
        assertEquals("Initial not contig", b3.piecesContiguous(BLACK), false);
        assertEquals("Initial not contig", b3.piecesContiguous(WHITE), false);
    }

    static final Piece[][] CONTIGUOUS = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { EMP,  EMP, EMP, BP, EMP, EMP, EMP, EMP  },
        { WP,  EMP, EMP, BP, BP, BP, EMP, WP  },
        { WP,  EMP, WP, BP, EMP, WP, EMP, EMP  },
        { WP,  EMP, BP, EMP, EMP, WP, EMP, EMP  },
        { WP,  BP, EMP, EMP, WP, EMP, EMP, WP  },
        { WP,  EMP, BP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  EMP,  BP,  WP,  EMP,  EMP,  EMP }
    };

    static final Piece[][] SINGLE = {
        { EMP, EMP,  EMP,  EMP,  EMP,  EMP,  EMP,  EMP },
        { EMP,  EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
        { WP,  EMP, EMP, EMP, EMP, BP, EMP, WP  },
        { WP,  EMP, WP, WP, EMP, WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP, EMP, WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP, WP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, WP,  EMP,  WP,  WP,  EMP,  EMP,  EMP }
    };
}
