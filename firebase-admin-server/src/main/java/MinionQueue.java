import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * The class that models the queue the server looks at.
 * It is sorted by timestamps, so the server always looks a the first element.
 */
public class MinionQueue {
    ArrayList<QueueMinion> minionQueue = new ArrayList<QueueMinion>();

    /**
     * Creates a QueueMinion from the parameters, and adds it to he queue.
     * @param key The Firebase path to the corresponding JSON minion.
     * @param zone The zone the minion is in.
     * @param timestamp Timestamp of the creation of the minion.
     */
    public void addMinion(String key, int zone, long timestamp){
        minionQueue.add(new QueueMinion(key, zone, timestamp));
    }

    /**
     * Removes all the expired minions.
     * @param currentTime The current internal time of the server.
     * @return How many minions where removed.
     */
    public int removeMinionsIfExpired(long currentTime){
        int removedMinions = 0;

        while((minionQueue.size() > 0) && (minionQueue.get(0).timestamp <= currentTime)) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(minionQueue.get(0).key);

            ref.removeValue();
            minionQueue.remove(0);

            removedMinions++;
        }
        return removedMinions;
    }

    /**
     * Sorts the queue in ascending order by timestamps.
     */
    public void sortQueue(){
        Collections.sort(minionQueue, new Comparator<QueueMinion>() {
            public int compare(QueueMinion q1, QueueMinion q2) {
                return (int) (q1.timestamp - q2.timestamp);
            }
        });
    }
}
