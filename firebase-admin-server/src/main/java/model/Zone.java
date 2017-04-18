package model;

import java.util.Random;

/**
 * A zone of minions.
 */
public class Zone {
    private static final int ZONES_PER_DEGREE = 100; // The width and height of a zone is 0.01 degrees
    //static final int LAT_SPAN = 180; // Span of the world in latitude
    private static final int LON_SPAN = 360; // Span of the world in longitude

    private final int latIndex, lonIndex;

    /**
     * Constructs an empty zone.
     * @param latIndex latitude index
     * @param lonIndex longitude index
     */
    public Zone(final int latIndex, final int lonIndex) {
        this.latIndex = latIndex;
        this.lonIndex = lonIndex;
    }

    /**
     * Used by Firebase
     */
    @SuppressWarnings("unused")
    private Zone() {
        latIndex = 0;
        lonIndex = 0;
    }

    /**
     * Gets the hash code of this.
     * Zones with the same hash code is equal.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return lonIndex + LON_SPAN * ZONES_PER_DEGREE * latIndex;
    }

    /**
     * Gets if this is equal to some object.
     * It is equal if the object is a zone with the same latitude and longitude indexes.
     * @param obj the object
     * @return true if and only if this is equal to the object
     */
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Zone && ((Zone) obj).latIndex == latIndex && ((Zone) obj).lonIndex == lonIndex;
    }

    /**
     * Generates a map minion.
     * @return the minion
     */
    public EMinion generateMapMinion() {
        final Random rand = new Random();

        // Random coordinates within this
        final double lat = (rand.nextDouble() / ((double) ZONES_PER_DEGREE)) + ((double) latIndex) / ((double) ZONES_PER_DEGREE);
        final double lon = (rand.nextDouble() / ((double) ZONES_PER_DEGREE)) + ((double) lonIndex) / ((double) ZONES_PER_DEGREE);

        return new EMinion(lat, lon);
    }

    /**
     * Gets the latitude index.
     * Used by Firebase.
     * @return the latitude index
     */
    @SuppressWarnings("WeakerAccess")
    public int getLatIndex() {
        return latIndex;
    }

    /**
     * Gets the longitude index.
     * Used by Firebase.
     * @return the longitude index
     */
    @SuppressWarnings("WeakerAccess")
    public int getLonIndex() {
        return lonIndex;
    }
}
