import battle.ChosenMoves;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import model.EMinion;
import task.TaskManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lapiki on 3/13/17.
 */
public class Main {
    public static void main(final String [] args) throws FileNotFoundException, InterruptedException {
        //Sets up a Firebase connection with admin privileges.
        setUpFirebaseAdmin();

        //new Generator().start();
        TaskManager.start();

        Thread.currentThread().join();
    }

    /**
     * Sets up a connection to our Firebase backend, with admin priviledges.
     * @throws FileNotFoundException
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







































