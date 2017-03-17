import java.util.ArrayList;
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

        while(minionQueue.get(0).timestamp <= currentTime) {
            minionQueue.remove(0);
            removedMinions++;
        }
        return removedMinions;
    }

}
