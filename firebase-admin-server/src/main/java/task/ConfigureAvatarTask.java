package task;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import model.Player;
import model.PlayerMinion;

/**
 * Configures an avatar.
 *
 * Fields: TASK_DATA: Avatar name.
 *
 * Expected response codes:
 * OK: Avatar is configured
 * CONFLICT: Name already in use
 */
class ConfigureAvatarTask extends Task {
    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * @param snapshot Firebase snapshot of the request
     */
    ConfigureAvatarTask(final DataSnapshot snapshot) {
        super(snapshot);
    }

    /**
     * Performs this task.
     */
    @Override
    void run() {
        // Get desired avatar name
        final String name;
        try {
            name = (String) snapshot.child(FirebaseNodes.TASK_DATA).getValue();
        } catch (final ClassCastException e) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        // Check for existing name
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.NAME_INDEX).child(name)
        .runTransaction(new Transaction.Handler() {
            public Transaction.Result doTransaction(final MutableData mutableData) {
                // If name is already in use
                if (mutableData.getValue(String.class) != null) {
                    return Transaction.abort();
                } else {
                    // Update index
                    mutableData.setValue(userId);

                    return Transaction.success(mutableData);
                }
            }

            public void onComplete(final DatabaseError databaseError, final boolean committed, final DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    ResponseHandler.respond(userId, HttpCodes.INTERNAL_SERVER_ERROR);
                    return;
                }

                // if aborted because name was already in use
                if (!committed) {
                    ResponseHandler.respond(userId, HttpCodes.CONFLICT);
                    return;
                }

                // Create player
                final Player player = new Player(name, 1);
                FirebaseDatabase.getInstance().getReference().push().getKey();

                // Upload player to Firebase
                final DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(userId);
                playerRef.setValue(player).addOnFailureListener(failureListener)
                .addOnSuccessListener(aVoid -> {
                    // Create minion for player
                    final PlayerMinion minion = new PlayerMinion("Swordman", 100, 20, 40, 1, "Melee");

                    playerRef.child(FirebaseNodes.PLAYER_MINIONS).push().setValue(minion).addOnFailureListener(failureListener)
                    .addOnSuccessListener(aVoid1 -> ResponseHandler.respond(userId, HttpCodes.OK));
                });
            }
        });
    }
}
