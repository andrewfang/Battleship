package loa;

import java.util.Scanner;
import static loa.Bio.*;
import static loa.Side.*;

/** A Player that prompts for moves and reads them from its Game.
 *  @author Andrew Fang*/
class HumanPlayer extends Player {

    /** A HumanPlayer that plays the SIDE pieces in GAME.  It uses
     *  GAME.getMove() as a source of moves.  */
    HumanPlayer(Side side, Game game) {
        super(side, game);
        setBio(HUMAN);
    }

    @Override boolean decide(Scanner inp) {
        boolean done = false;
        String command;
        String moveregex = "[A-Ha-h][1-8]-[A-Ha-h][1-8]";
        while (!done) {
            stopwatch().start();
            System.out.print("");
            command = inp.nextLine();
            stopwatch().stop();
            if (isOn()) {
                if (stopwatch().getAccum() / THOU > getGame().getTime() * SEC) {
                    System.out.println("   You ran out of time.");
                    if (side() == WHITE) {
                        System.out.println("Black wins.");
                    } else {
                        System.out.println("White wins.");
                    }
                    System.exit(0);
                }
            } else {
                stopwatch().reset();
            }
            if (command.length() < 1) {
                usage();
            } else if (command.charAt(0) == 's') {
                sCommand();
            } else if (command.charAt(0) == 'q') {
                System.out.println("  Thanks for playing!\n");
                System.exit(0);
            } else if (command.charAt(0) == 'p') {
                return true;
            } else if (command.charAt(0) == 't') {
                System.out.println(getGame().getTime()
                                   * SEC - stopwatch().getAccum() / THOU);
            } else if (command.charAt(0) == '#') {
                System.out.print("");
            } else if ((command.length() > 4)
                       && command.substring(0, 5).matches(moveregex)) {
                Move m = Move.create(command.substring(0, 5));
                getBoard().makeMove(m);
                done = true;
            } else {
                usage();
            }
            if (!done) {
                System.out.print(getBoard().turn() + "> ");
                System.out.flush();
            }
        }
        return false;
    }

    /** Performs the s command. */
    void sCommand() {
        System.out.println("===");
        System.out.println(getBoard());
        System.out.print("Next move: ");
        System.out.println(getBoard().turn());
        System.out.printf("Moves: %d%n", getBoard().movesMade());
        System.out.println("===");
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

    /** The number of seconds in a minute. */
    private static final int SEC = 60;

    /** Ten to the power of three. */
    private static final int THOU = 1000;

}
