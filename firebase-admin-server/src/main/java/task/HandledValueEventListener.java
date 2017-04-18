package task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A ValueEventListener that handles a cancelled event by responding an internal server error
 */
class HandledValueEventListener implements ValueEventListener {
    private final String userId;
    private final DataChangeListener listener;

    /**
     * Stores user id and listener to call on data change.
     * @param userId user id
     * @param listener listener
     */
    HandledValueEventListener(final String userId, final DataChangeListener listener) {
        this.userId = userId;
        this.listener = listener;
    }

    /**
     * Implements the interface.
     * @param dataSnapshot snapshot
     */
    public void onDataChange(final DataSnapshot dataSnapshot) {
        listener.onDataChange(dataSnapshot);
    }

    /**
     * Implements the interface.
     * @param databaseError snapshot
     */
    public void onCancelled(final DatabaseError databaseError) {
        ResponseHandler.respond(userId, HttpCodes.INTERNAL_SERVER_ERROR);
    }
}
