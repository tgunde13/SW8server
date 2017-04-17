package task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A ValueEventListener that does nothing when onCancelled is called.
 */
public class UnhandledValueEventListener implements ValueEventListener {
    private final DataChangeListener listener;

    /**
     * Stores listener to call on data change.
     * @param listener listener
     */
    public UnhandledValueEventListener(final DataChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Calls listener.
     * @param dataSnapshot snapshot
     */
    public void onDataChange(final DataSnapshot dataSnapshot) {
        listener.onDataChange(dataSnapshot);
    }

    /**
     * Does nothing
     * @param databaseError snapshot
     */
    public void onCancelled(final DatabaseError databaseError) {
    }
}
