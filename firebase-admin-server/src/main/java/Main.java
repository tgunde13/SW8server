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

        //Infinite loop, that keeps spawning new minions.
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
            //The number 7 is the lowest that 60 can ot be divided by, thus it is chosen.
            TimeUnit.SECONDS.sleep(7);
        }
    }

    /**
     * Sets up a minion queue with a given amount of minions. Spreads their expiration over 30 minutes.
     * @param amountOfMinions The amount of minions that should be in the game.
     * @param queue The MinionQueue object that the minions should be in.
     * @param generator The Generatior object that generates the minions.
     */
    public static void setUpMinionQueue(int amountOfMinions, MinionQueue queue, Generator generator) {
        Random rand = new Random();

        for(int i = 0; i < amountOfMinions; i++) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Minion minion = generator.generateMinion();

            ref = ref.child("minions").child("0").push();

            //We divide by 1000 to get seconds in tead of miliseconds. We ad a number between 0 and 1799 to expire now or in 30 min.
            queue.addMinion(ref.toString(), 0, ((System.currentTimeMillis() / 1000) + rand.nextInt(1800)));
            ref.setValue(minion);
        }
        //Since we used random numbers, we need to sort the queue, bacause of the way it works.
        queue.sortQueue();
    }

    /**
     * Sets up a connection to our Firebase backend, with admin priviledges.
     * @throws FileNotFoundException
     */
    private static void setUpFirebaseAdmin() throws FileNotFoundException {
        // Fetch the service account key JSON file contents

        //For when making .jar file
        InputStream serviceAccount = ClassLoader.getSystemClassLoader().getResourceAsStream("serviceAccountKey.json");

        //For normal build
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







































