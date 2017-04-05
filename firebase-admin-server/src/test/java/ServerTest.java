import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lapiki on 4/3/17.
 */
public class ServerTest {

    @Test
    public void queueMinionCreationTest() {
        QueueMinion q = new QueueMinion("aaa", 5 , 1000);

        assertTrue("aaa".equals(q.key));
        assertEquals(5, q.zone);
        assertEquals(1000, q.timestamp);
    }
}
