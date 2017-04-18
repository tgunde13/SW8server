import com.google.firebase.database.DatabaseReference;

/**
 * Models a minions that is in the server queue. And thus only contains needed information to save space.
 */
class QueueMinion {
    private final DatabaseReference ref;

    /**
     * Constructs a queue minion.
     * @param ref Firebase reference to the minion.
     */
    QueueMinion(final DatabaseReference ref) {
        this.ref = ref;
    }

    /**
     * Gets the Firebase reference to the minion.
     * @return the reference
     */
    DatabaseReference getRef() {
        return ref;
    }
}
