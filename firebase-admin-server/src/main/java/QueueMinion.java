import java.security.Timestamp;

/**
 * Created by lapiki on 3/15/17.
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
