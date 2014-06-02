package com.company;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Test cases
 */
public class MainTest {

    @Test
    public void waterCountTest(){
        int[] args = {4, 2, 3, 4, 0, 3};
        WaterFill waterFill = new WaterFill(args);
        assertEquals(waterFill.getWaterCount(), 3);
    }

    @Test
    public void allocatingTest(){
        int[] args = {1, 4, 2, 0, 6, 1, 2, 5};
        WaterFill waterFill = new WaterFill(args);
        assertNotEquals(waterFill.getData().get(3).get(1), WaterFill.WATER);
        assertEquals(waterFill.getData().get(5).get(2), WaterFill.WATER);
        assertEquals(waterFill.getData().get(1).get(0), WaterFill.BLOCK);
    }
}
