package battle;

/**
 * Created by Tobias on 24/04/2017.
 */
public class SynchronizedCountdown {
    private int counter;
    private final Runnable action;

    SynchronizedCountdown(final int count, final Runnable action) {
        counter = count;
        this.action = action;
    }

    synchronized void step() {
        counter--;

        System.out.println("TOB: SynchronizedCountdown, step, counter: " + counter);

        if (counter == 0) {
            action.run();
        }
    }
}
