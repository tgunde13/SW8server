package model;

/**
 * An class that holds an avatarKey and minionKey used to identify a specific battle minion
 */
public class BattleMinionIdentifier {
    private String avatarKey;
    private String minionKey;

    /**
     * Takes an avatarKey and minionKey used to identify a battleMinion
     * @param avatarKey the key of the avatar that this minion is owned by
     * @param minionKey the key of the minion that we want to get
     */
    public BattleMinionIdentifier(final String avatarKey, final String minionKey){
        this.avatarKey = avatarKey;
        this.minionKey = minionKey;
    }

    /**
     * Default constructor
     */
    protected BattleMinionIdentifier(){

    }

    /**
     * Getter for avatar key
     * @return the avatarKey
     */
    public String getAvatarKey() {
        return avatarKey;
    }

    /**
     * Getter for minion key
     * @return the minionKey
     */
    public String getMinionKey() {
        return  minionKey;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        final BattleMinionIdentifier identifier = (BattleMinionIdentifier) obj;

        return avatarKey.equals(identifier.avatarKey) && minionKey.equals(identifier.minionKey);

    }
}
