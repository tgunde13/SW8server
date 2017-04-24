package battle;

/**
 * Counts down by calls to step().
 * When counter reaches 0, an action is called.
 * Steps and the action call is done with a lock.
 */
class SynchronizedCountdown {
    private int counter;
    private final Runnable action;

    /**
     * Constructs with a specified count.
     * @param count the count
     * @param action the action to call when this is fired
     */
    SynchronizedCountdown(final int count, final Runnable action) {
        counter = count;
        this.action = action;
    }

    /**
     * Decrements the count.
     */
    synchronized void step() {
        counter--;

        System.out.println("TOB: SynchronizedCountdown, step, counter: " + counter);

        if (counter == 0) {
            action.run();
        }
    }
}
