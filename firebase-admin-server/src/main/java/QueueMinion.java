import java.security.Timestamp;

/**
 * Models a minions that is in the server queue. And thus only contains needed information.
 */
public class QueueMinion {
    String key;
    int zone;
    long timestamp;

    public QueueMinion(String key, int zone, long timestamp) {
        this.key = key;
        this.zone = zone;
        this.timestamp = timestamp;
    }
}
