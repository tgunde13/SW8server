import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;

import java.io.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by lapiki on 3/13/17.
 */
public class Main {
    public static void main(String [] args) throws FileNotFoundException, InterruptedException {
        //Sets up a Firebase connection with admin privileges.
        setUpFirebaseAdmin();

        MinionQueue queue = new MinionQueue();
        Generator generator = new Generator();

        //Makes sure to generate the first 500 minions. Thus also setting the minion cap at 500.
        setUpMinionQueue(500, queue, generator);

        while(true) {
            int removedminions;
            removedminions = queue.removeMinionsIfExpired((System.currentTimeMillis() / 1000));

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

    public static void setUpMinionQueue(int amountOfMinions, MinionQueue queue, Generator generator) {
        Random rand = new Random();

        int  n = rand.nextInt(50) + 1;

        for(int i = 0; i < amountOfMinions; i++) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Minion minion = generator.generateMinion();

            ref = ref.child("minions").child("0").push();
            queue.addMinion(ref.toString(), 0, ((System.currentTimeMillis() / 1000) + rand.nextInt(1800)));
            ref.setValue(minion);
        }
        queue.sortQueue();
    }

    private static void setUpFirebaseAdmin(){
        // Fetch the service account key JSON file contents

        InputStream serviceAccount = ClassLoader.getSystemClassLoader().getResourceAsStream("serviceAccountKey.json");
        //BufferedReader serviceAccount = new BufferedReader(new InputStreamReader(localInputStream));


        //FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

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
    }
}







































