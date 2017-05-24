package task;

import com.google.firebase.database.*;
import firebase.DataChangeListenerAdapter;
import firebase.FirebaseNodes;
import model.Zone;

import java.util.function.BiConsumer;

/**
 * Sets the zone of a player.
 *
 * Fields: TASK_DATA: zone of the player (of type Zone).
 *
 * Expected response codes:
 * OK
 */
class SetZoneTask extends Task {
    private final boolean visible;

    private final DatabaseReference playerZoneRef;
    private final DatabaseReference playerVisibleRef;

    /**
     * Extracts the user id of the client.
     * Stores the snapshot and index node.
     * @param snapshot Firebase snapshot of the request
     * @param visible whether the player should be visible or hidden from other players in the zone index
     */
    SetZoneTask(final DataSnapshot snapshot, final boolean visible) {
        super(snapshot);

        this.visible = visible;

        final DatabaseReference playerRef = FirebaseDatabase.getInstance()
                    .getReference(FirebaseNodes.PLAYERS)
                    .child(userId);

        playerZoneRef = playerRef.child(FirebaseNodes.PLAYER_ZONE);
        playerVisibleRef = playerRef.child(FirebaseNodes.PLAYER_VISIBLE);
    }

    /**
     * Performs this task.
     */
    @Override
    void run() {
        // Get new zone
        final Zone newZone = snapshot.child(FirebaseNodes.TASK_DATA).getValue(Zone.class);

        if (newZone == null) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        // Get old zone (if any) and if player was visible or not from Firebase
        // Then set the next zone
        read((oldZone, oldVisible) -> setNew(newZone, () -> {
            // If player had no old zone, respond
            if (oldZone == null) {
                ResponseHandler.respond(userId, HttpCodes.OK);
                return;
            }

            // Remove old zone and then respond
            removeFromIndex(oldZone, oldVisible, () -> ResponseHandler.respond(userId, HttpCodes.OK));
        }));
    }

    /**
     * Reads the current zone for the player player and if player is visible.
     * @param action action to be called if successful
     */
    private void read(final BiConsumer<Zone, Boolean> action) {
        // Get old player zone (if one exists)
        playerZoneRef.addListenerForSingleValueEvent(new DataChangeListenerAdapter(zoneSnapshot -> {
            final Zone oldZone = zoneSnapshot.getValue(Zone.class);

            if (oldZone == null) {
                action.accept(null, null);
                return;
            }

            // Get if old data was visible or hidden
            playerVisibleRef.addListenerForSingleValueEvent(new DataChangeListenerAdapter(visibleSnapshot -> {
                final Boolean visible = (Boolean) visibleSnapshot.getValue();
                action.accept(oldZone, visible);
            }));
        }));
    }

    /**
     * Sets the new zone for the player and the zone index.
     * @param newZone the new zone of the player
     * @param action action to be run if successful
     */
    private void setNew(final Zone newZone, final Runnable action) {
        // Add to player zone index
        FirebaseDatabase.getInstance().getReference(getZoneNode(visible))
        .child(String.valueOf(newZone.getLatIndex())).child(String.valueOf(newZone.getLonIndex()))
        .push().setValue(userId).addOnFailureListener(failureListener)
        .addOnSuccessListener(aVoid -> {
            // Set player zone to new zone
            playerZoneRef.setValue(newZone).addOnFailureListener(failureListener)
            .addOnSuccessListener(aVoid1 -> {
                // Set visible or hidden
                playerVisibleRef.setValue(visible).addOnFailureListener(failureListener)
                .addOnSuccessListener(aVoid2 -> action.run());
            });
        });
    }

    /**
     * Removes a player from a zone index.
     * @param zone the zone to remove player from
     * @param visible whether to remove from the visible of the hidden index
     * @param action action to be run if successful
     */
    private void removeFromIndex(final Zone zone, final boolean visible, final Runnable action) {
        final DatabaseReference zoneIndexRef = FirebaseDatabase.getInstance().getReference(getZoneNode(visible));

        // Remove player from old player zone index entry
        zoneIndexRef.child(String.valueOf(zone.getLatIndex()))
        .child(String.valueOf(zone.getLonIndex())).removeValue()
        .addOnFailureListener(failureListener)
        .addOnSuccessListener(aVoid11 -> action.run());
    }

    /**
     * Gets the node name of the visible or hidden zone index.
     * @param visible whether to get the visible or hidden zone index
     * @return the node name
     */
    private static String getZoneNode(final boolean visible) {
        if (visible) {
            return FirebaseNodes.VISIBLE_PLAYER_ZONE_INDEX;
        } else {
            return FirebaseNodes.HIDDEN_PLAYER_ZONE_INDEX;
        }
    }
}
