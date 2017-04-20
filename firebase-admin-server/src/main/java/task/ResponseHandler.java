package task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import firebase.FirebaseNodes;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles response to clients over Firebase
 */
class ResponseHandler {
    /**
     * Responds to a client over Firebase.
     * @param userId Firebase user id of the client
     * @param statusCode status code to respond with
     */
    static void respond(final String userId, final int statusCode) {
        final Map<String, Object> map = new HashMap<>();
        map.put(FirebaseNodes.TASK_CODE, statusCode);

        respond(userId, map);
    }

    /**
     * Responds to a client over Firebase.
     * @param userId Firebase user id of the client
     * @param value value of response, inclusive status code
     */
    static void respond(final String userId, final Map<String, Object> value) {
        final DatabaseReference taskRef = FirebaseDatabase.getInstance().getReference().child(FirebaseNodes.TASKS);

        // Delete request so that client can create a new request
        taskRef.child(FirebaseNodes.REQUESTS).child(userId).removeValue((databaseError, databaseReference) -> {
            // Set response code
            taskRef.child(FirebaseNodes.RESPONSES).child(userId).setValue(value);
        });
    }
}
