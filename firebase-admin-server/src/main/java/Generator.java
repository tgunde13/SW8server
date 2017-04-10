import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import model.MapMinion;
import model.Zone;

import java.util.*;

/**
 * A class that can generate model.MapMinion and updates Firebase.
 */
class Generator extends TimerTask {
    private static final int MINIONS_PER_ZONE = 15;
    private static final int MINION_LIFETIME = 30*60*1000; // 30 minutes in milliseconds

    private final List<List<QueueMinion>> bulks;

    /**
     * Constructs an instance.
     */
    Generator() {
        bulks = new ArrayList<List<QueueMinion>>();

        // Set up 10 empty lists
        for (int i = 0; i < MINIONS_PER_ZONE; i++) {
            bulks.add(new ArrayList<QueueMinion>());
        }
    }

    /**
     * Starts a never ending generating.
     */
    void start() {
        // Remove potential existing map minions
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.ENVIRONMENT_SQUADS).removeValue(new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // Loop this
                Timer timer = new Timer(true);
                timer.scheduleAtFixedRate(Generator.this, 0, MINION_LIFETIME / MINIONS_PER_ZONE);

                System.out.println("TOB: Generator, started");
            }
        });
    }

    /**
     * Runs an iteration of generating.
     */
    @Override
    public void run() {
        System.out.println("TOB: Generator, run");

        // Remove minions in the first bulk from Firebase
        for (QueueMinion minion : bulks.get(0)) {
            minion.getRef().removeValue();
        }

        // Remove the first bulk
        bulks.remove(0);

        // Get zones with players in
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYER_ZONE_INDEX).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<Zone> zones = new HashSet<Zone>();

                for (DataSnapshot latSnapshot : dataSnapshot.getChildren()) {
                    int usedLatIndex = Integer.parseInt(latSnapshot.getKey());

                    for (DataSnapshot lonSnapshot : latSnapshot.getChildren()) {
                        int usedLonIndex = Integer.parseInt(lonSnapshot.getKey());

                        // Add zones with players in or players nearby
                        for (int latIndex = usedLatIndex - 1; latIndex <= usedLatIndex + 1; latIndex++) {
                            for (int lonIndex = usedLonIndex - 1; lonIndex <= usedLonIndex + 1; lonIndex++) {
                                zones.add(new Zone(latIndex, lonIndex));
                            }
                        }
                    }
                }

                // Make new bulk from the computed zones
                List<QueueMinion> bulk = new ArrayList<QueueMinion>();
                for (Zone zone : zones) {
                    MapMinion minion = zone.generateMapMinion();

                    DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference(FirebaseNodes.ENVIRONMENT_SQUADS)
                                            .child(Integer.toString(zone.getLatIndex()))
                                            .child(Integer.toString(zone.getLonIndex())).push();

                    ref.setValue(minion); // Add to Firebase

                    bulk.add(new QueueMinion(ref));
                }

                bulks.add(bulk);
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
