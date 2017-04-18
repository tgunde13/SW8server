package task;

import com.google.firebase.database.DataSnapshot;

/**
 * A listener for a change in some Firebase data.
 */
public interface DataChangeListener {
    /**
     * To call on data change
     * @param dataSnapshot Firebase snapshot
     */
    void onDataChange(final DataSnapshot dataSnapshot);
}
