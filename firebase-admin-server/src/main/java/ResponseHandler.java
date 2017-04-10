import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import firebase.FirebaseNodes;

/**
 * Handles response to clients over Firebase
 */
class ResponseHandler {
    /**
     * Responds to a client over Firebase
     * @param userId Firebase user id of the client
     * @param statusCode status code to respond with
     */
    static void respond(final String userId, final int statusCode) {
        // Delete request so that client can create a new request
        final DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child(FirebaseNodes.TASKS);
        taskRef.child(FirebaseNodes.REQUESTS).child(userId).removeValue(new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                DatabaseReference respondCodeRef = taskRef.child(FirebaseNodes.RESPONSES).child(userId).child(FirebaseNodes.STATUS_CODE);

                // Check for error
                if (databaseError != null) {
                    respondCodeRef.setValue(HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
                    return;
                }

                // Set response code
                taskRef.child(FirebaseNodes.RESPONSES).child(userId).child(FirebaseNodes.STATUS_CODE)
                .setValue(statusCode);
            }
        });
    }

    static void respond(final DataSnapshot request, final int code) {
        respond(request.getKey(), code);
    }
}
