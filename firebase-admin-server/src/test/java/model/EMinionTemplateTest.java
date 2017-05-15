package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class EMinionTemplateTest {
    @Test
    public void generateEMinionData() throws Exception {
        int swordCount = 0, spearCount = 0;

        for (int i = 0; i < 100; i++) {
            EMinionTemplate template = EMinionTemplate.generateEMinionData(0.0, 0.0);

            if (template.name.equals("Swordman")) {
                swordCount++;
            } else {
                spearCount++;
            }
        }

        assertTrue(swordCount > 25);
        assertTrue(spearCount > 25);
    }

}