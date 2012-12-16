package loa;

import ucb.util.CommandArgs;
import static loa.Side.*;

/** Main class of the Lines of Action program.
 * @author Andrew Fang
 */
public class Main {

    /** The main Lines of Action.  ARGS are as described in the
     *  project 3 handout:
     *      [ --white ] [ --ai=N ] [ --seed=S ] [ --time=LIM ] \
     *      [ --debug=D ] [ --display ]
     */
    public static void main(String... args) {
        Side side = BLACK;
        boolean hasDisplay;
        int numPlayers = 1;
        long seedVal = 4;
        float timeVal = BIG;
        int debugVal = 0;
        String opt = "--white --ai=([012]) --seed=(\\d+)"
            + " --time=(\\d*\\.?\\d+) --debug=(\\d) --display";
        CommandArgs options = new CommandArgs(opt, args);
        if (!options.ok()) {
            usage();
        }
        if (options.containsKey("--white")) {
            side = WHITE;
        }
        hasDisplay = options.containsKey("--display");
        if (options.containsKey("--ai")) {
            numPlayers = 2 - options.getInt("--ai");
        }
        if (options.containsKey("--seed")) {
            seedVal = options.getLong("--seed");
        }
        if (options.containsKey("--time")) {
            timeVal = (float) options.getDouble("--time");
        }
        if (options.containsKey("--debug")) {
            debugVal = options.getInt("--debug");
        }
        Game game = new Game(numPlayers, side, seedVal, timeVal, debugVal);
        game.play();
    }

    /** A very big number. */
    static final float BIG = 9999;

    /** Print brief description of the command-line format. */
    static void usage() {
        String c = "Use these command arguments:\n";
        String w = "\"--white\" (will start you with white)\n";
        String a = "\"--ai=N\" (N=number of AIs playing)\n";
        String s = "\"--seed=S\" (a seed number for AI randomness)\n";
        String t = "\"--time=T\" (a max time limit (minutes) for each turn)\n";
        String d = "\"--debug=D\" (debugging paramters)\n";
        String di = "\"--display\" (turns on the GUI display)\n";
        System.out.println(c + w + a + s + t + d + di);
        System.exit(1);
    }
}
