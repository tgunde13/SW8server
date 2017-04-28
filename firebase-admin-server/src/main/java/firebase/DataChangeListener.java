package firebase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A ValueEventListener that does nothing when onCancelled is called.
 */
public abstract class DataChangeListener implements ValueEventListener {
    /**
     * Does nothing
     * @param databaseError snapshot
     */
    public void onCancelled(final DatabaseError databaseError) {
    }
}
