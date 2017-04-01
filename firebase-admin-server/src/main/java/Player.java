import java.util.List;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Player {
    String name;
    int id;
    List<PlayerMinion> minions;

    public Player() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<PlayerMinion> getMinions() {
        return minions;
    }
}
