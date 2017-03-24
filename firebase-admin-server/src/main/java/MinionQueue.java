import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lapiki on 3/15/17.
 */
public class MinionQueue {
    ArrayList<QueueMinion> minionQueue = new ArrayList<QueueMinion>();

    public void addMinion(String key, int zone, long timestamp){
        minionQueue.add(new QueueMinion(key, zone, timestamp));

    }

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

    public void sortQueue(){
        Collections.sort(minionQueue, new Comparator<QueueMinion>() {
            public int compare(QueueMinion q1, QueueMinion q2) {
                return (int) (q2.timestamp - q1.timestamp);
            }
        });
    }
}
