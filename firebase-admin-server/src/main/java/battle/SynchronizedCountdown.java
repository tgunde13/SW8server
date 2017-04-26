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
     * If count is 0, the action is called.
     * @param count the count
     * @param action the action to call when this is fired
     */
    SynchronizedCountdown(final int count, final Runnable action) {
        System.out.println("TOB: SynchronizedCountdown, count: " + count);

        counter = count;
        this.action = action;

        checkCounter();
    }

    /**
     * Decrements the count by 1.
     */
    synchronized void step() {
        counter--;

        System.out.println("TOB: SynchronizedCountdown, step, counter: " + counter);

        checkCounter();
    }

    /**
     * Decrements the count by a given amount.
     * @param amount the amount
     */
    synchronized void step(final int amount) {
        for (int i = 0; i < amount; i++) {
            // It is okay, to call the synchronized step,
            // since Java used reentrant synchronization
            step();
        }
    }

    /**
     * If counter is 0, runs action.
     */
    private void checkCounter() {
        if (counter == 0) {
            action.run();
        }
    }
}
