package battle;

/**
 * A move that is available and thus not yet chosen.
 */
class AvailableMove extends ChosenMove {
    /**
     * Constructor
     */
    AvailableMove() {
        super(null, null, true);
    }
}
