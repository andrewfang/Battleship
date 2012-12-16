package loa;

/** Indicates the biology for a player.
 *  @author Andrew Fang */
enum Bio {
    /** The names of the bios. */
    HUMAN, MACHINE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
