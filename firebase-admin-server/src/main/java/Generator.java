import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import model.EMinion;
import model.Zone;
import task.UnhandledValueEventListener;

import java.util.*;

/**
 * A class that can generate model.EMinion and updates Firebase.
 */
class Generator extends TimerTask {
    private static final int MINIONS_PER_ZONE = 15;
    private static final int MINION_LIFETIME = 30*60*1000; // 30 minutes in milliseconds

    private final List<List<QueueMinion>> bulks;

    /**
     * Constructs an instance.
     */
    Generator() {
        bulks = new ArrayList<>();

        // Set up 10 empty lists
        for (int i = 0; i < MINIONS_PER_ZONE; i++) {
            bulks.add(new ArrayList<>());
        }
    }

    /**
     * Starts a never ending generating.
     */
    void start() {
        // Remove potential existing map minions
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.ENVIRONMENT_SQUADS).removeValue((databaseError, databaseReference) -> {
            // Loop this
            final Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(Generator.this, 0, MINION_LIFETIME / MINIONS_PER_ZONE);

            System.out.println("TOB: Generator, started");
        });
    }

    /**
     * Runs an iteration of generating.
     */
    @Override
    public void run() {
        System.out.println("TOB: Generator, run");

        // Remove minions in the first bulk from Firebase
        for (final QueueMinion minion : bulks.get(0)) {
            minion.getRef().removeValue();
        }

        // Remove the first bulk
        bulks.remove(0);

        final Set<Zone> zones = new HashSet<>();

        // Add zones with visible players in
        addZones(zones, FirebaseDatabase.getInstance().getReference(FirebaseNodes.VISIBLE_PLAYER_ZONE_INDEX), () -> {
            // Add zones with hidden players in
            addZones(zones, FirebaseDatabase.getInstance().getReference(FirebaseNodes.HIDDEN_PLAYER_ZONE_INDEX), () -> {
                // Make new bulk from the computed zones
                final List<QueueMinion> bulk = new ArrayList<>();
                for (final Zone zone : zones) {
                    final EMinion minion = zone.generateMapMinion();

                    final DatabaseReference ref = FirebaseDatabase.getInstance()
                                                  .getReference(FirebaseNodes.ENVIRONMENT_SQUADS)
                                                  .child(Integer.toString(zone.getLatIndex()))
                                                  .child(Integer.toString(zone.getLonIndex())).push();

                    ref.setValue(minion); // Add to Firebase

                    bulk.add(new QueueMinion(ref));
                }

                bulks.add(bulk);
            });
        });
    }

    /**
     * Add zones from a reference to a set.
     * @param zones set to add to
     * @param ref reference to add from
     * @param action action to run if successful
     */
    private static void addZones(final Set<Zone> zones, final DatabaseReference ref, final Runnable action) {
        ref.addListenerForSingleValueEvent(new UnhandledValueEventListener(dataSnapshot -> {
            for (final DataSnapshot latSnapshot : dataSnapshot.getChildren()) {
                final int usedLatIndex = Integer.parseInt(latSnapshot.getKey());

                for (final DataSnapshot lonSnapshot : latSnapshot.getChildren()) {
                    final int usedLonIndex = Integer.parseInt(lonSnapshot.getKey());

                    // Add zones with players in or players nearby
                    for (int latIndex = usedLatIndex - 1; latIndex <= usedLatIndex + 1; latIndex++) {
                        for (int lonIndex = usedLonIndex - 1; lonIndex <= usedLonIndex + 1; lonIndex++) {
                            zones.add(new Zone(latIndex, lonIndex));
                        }
                    }
                }
            }

            action.run();
        }));
    }
}
