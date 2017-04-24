package firebase;

import com.google.firebase.database.DataSnapshot;

import java.util.function.Consumer;

/**
 * A ValueEventListener that does nothing when onCancelled is called.
 */
public class DataChangeListenerAdapter extends DataChangeListener {
    private final Consumer<DataSnapshot> listener;

    /**
     * Constructor with a listener to be called whenever onDataChange is to be fired.
     * @param listener the listener
     */
    public DataChangeListenerAdapter(final Consumer<DataSnapshot> listener) {
        this.listener = listener;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        listener.accept(dataSnapshot);
    }
}
