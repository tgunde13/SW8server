import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import task.TaskManager;

import java.io.*;

/**
 * Main class, is tasked with setting up a connection with firebase, start the generation of minions,
 * and start the task manager
 */
public class Main {
    /**
     * Main method, starts the operations
     * @param args main args, unused
     * @throws FileNotFoundException if serviceAccountKey.json is not found
     * @throws InterruptedException if thread is interrupted
     */
    public static void main(final String [] args) throws FileNotFoundException, InterruptedException {
        //Sets up a Firebase connection with admin privileges.
        setUpFirebaseAdmin();

        new Generator().start();
        TaskManager.start();

        Thread.currentThread().join();
    }

    /**
     * Sets up a connection to our Firebase backend, with admin priviledges.
     * @throws FileNotFoundException if serviceAccountKey.json is not found
     */
    private static void setUpFirebaseAdmin() throws FileNotFoundException {
        // Fetch the service account key JSON file contents

        // TODO Lapiki: Lav bedre løsning
        //For when making .jar file
        //InputStream serviceAccount = ClassLoader.getSystemClassLoader().getResourceAsStream("serviceAccountKey.json");

        //For normal build
        final FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

        // Initialize the app with a service account, granting admin privileges
        final FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl("https://league-of-locations-26618308.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        final DatabaseReference adminRef = FirebaseDatabase
                .getInstance()
                .getReference("restricted_access/secret_document");
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            public void onCancelled(final DatabaseError databaseError) {
                //DB fail
            }
        });
    }
}







































