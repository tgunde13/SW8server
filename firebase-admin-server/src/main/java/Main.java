import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

/**
 * Created by lapiki on 3/13/17.
 */
public class Main {
    public static void main(String [] args) throws FileNotFoundException, InterruptedException {
        // Fetch the service account key JSON file contents
        FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

        // Initialize the app with a service account, granting admin privileges
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://league-of-locations-26618308.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        DatabaseReference adminRef = FirebaseDatabase
                .getInstance()
                .getReference("restricted_access/secret_document");
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            public void onCancelled(DatabaseError databaseError) {
                //DB fail
            }
        });

        Generator generator = new Generator();
        MinionQueue queue = new MinionQueue();

        for(int i = 0; i < 100; i++) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Minion minion = generator.generateMinion();

            ref = ref.child("minions").child("0").push();
            queue.addMinion(ref.toString(), 0, ((System.currentTimeMillis() / 1000) + 1800));
            ref.setValue(minion);
        }

        while(true) {
            int removedminions;
            removedminions = queue.removeMinionsIfExpired(System.currentTimeMillis());

            for(int i = 0; i < removedminions; i++) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Minion minion = generator.generateMinion();
                long currentTimeSeconds = (System.currentTimeMillis() / 1000);

                ref = ref.child("minions").child("0").push();
                queue.addMinion(ref.toString(), 0, (currentTimeSeconds + 1800));

                ref.setValue(minion);
            }
            TimeUnit.SECONDS.sleep(10);
        }
    }

    public static void setUpTestListener(FirebaseDatabase database) {
        final DatabaseReference ref = database.getReference("test");
        final DatabaseReference ref2 = database.getReference("test2");

        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = dataSnapshot.getValue(Integer.class);
                ref2.setValue((i*2));
            }

            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void setUpSessionListener(FirebaseDatabase database) {
        
    }
}







































