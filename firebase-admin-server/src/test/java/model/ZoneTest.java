package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class ZoneTest {
    @Test
    public void hashCodeNotEquals() throws Exception {
        Zone a = new Zone(1, 2);
        Zone b = new Zone(1, 3);

        assertNotEquals(a.hashCode(), b.hashCode());
    }
    @Test
    public void hashCodeEquals() throws Exception {
        Zone a = new Zone(1, 2);
        Zone b = new Zone(1, 2);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals() throws Exception {
        Zone a = new Zone(1, 2);
        Zone b = new Zone(1, 2);

        assertTrue(a.equals(b));
    }

    @Test
    public void notEquals() throws Exception {
        Zone a = new Zone(1, 2);
        Zone b = new Zone(1, 3);

        assertFalse(a.equals(b));
    }

    @Test
    public void generateMapMinion() throws Exception {
        Zone a = new Zone(1, 2);
        for (int i = 0; i < 100; i++) {
            EMinionTemplate t = a.generateMapMinion();
            assertTrue(t.getLat() >= 0.01);
            assertTrue(t.getLat() < 0.02);
            assertTrue(t.getLon() >= 0.02);
            assertTrue(t.getLon() < 0.03);
        }
    }

    @Test
    public void getLatIndex() throws Exception {
        Zone a = new Zone(1, 2);
        assertEquals(1, a.getLatIndex());
    }

    @Test
    public void getLonIndex() throws Exception {
        Zone a = new Zone(1, 2);
        assertEquals(2, a.getLonIndex());
    }

}