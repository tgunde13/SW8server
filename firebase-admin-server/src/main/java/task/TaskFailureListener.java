package task;

import com.google.firebase.tasks.OnFailureListener;

/**
 * A Firebase failure listener.
 * On a failure, makes the response handler respond a HTTP internal server error.
 */
public class TaskFailureListener implements OnFailureListener {
    private final String userId;

    /**
     * Constructor.
     * @param userId Firebase user id
     */
    TaskFailureListener(String userId) {
        this.userId = userId;
    }

    /**
     * Makes the response handler respond with a HTTP internal server error.
     * @param e not used
     */
    public void onFailure(Exception e) {
        ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
        System.out.println("TOB: TaskFailureListener, onFailure");
    }
}
