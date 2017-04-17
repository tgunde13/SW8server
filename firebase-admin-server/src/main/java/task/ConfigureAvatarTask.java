package task;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import model.Player;
import model.PlayerMinion;

import java.util.ArrayList;

/**
 * Configures an avatar.
 * Fields: REQUEST_DATA: Avatar name.
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
    void perform() {
        // Get desired avatar name
        final String name;
        try {
            name = (String) snapshot.child(FirebaseNodes.REQUEST_DATA).getValue();
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

                // Create minion for player
                final PlayerMinion minion = new PlayerMinion();
                minion.type = "Starter";
                minion.maxHealth = 10;
                minion.currentHealth = minion.maxHealth;
                minion.level = 1;
                minion.xp = 0;

                // Create player and add minion
                final Player player = new Player();
                player.id = 1;
                player.name = name;
                player.minions = new ArrayList<>();
                player.minions.add(minion);

                // Upload player to Firebase
                FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(userId)
                .setValue(player).addOnFailureListener(new TaskFailureListener(userId))
                .addOnSuccessListener(aVoid -> ResponseHandler.respond(userId, HttpCodes.OK));
            }
        });
    }
}
