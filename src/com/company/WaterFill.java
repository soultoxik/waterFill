package com.company;


import java.util.ArrayList;
import java.util.List;

/**
 * Contains logic of water filling
 */
class WaterFill {
    private int[] levels;
    private int maxHeight = 0;
    private List<List<Character>> map;
    private int waterCount = 0;

    public static final Character WATER = '~';
    public static final Character BLOCK = 'm';
    public static final Character EMPTY = ' ';

    /**
     * Init map array with BLOCKs (given) and EMPTY (other) and fill this surface with water
     * @param levels heights' levels
     */
    public WaterFill(int[] levels){
        this.levels = levels;
        map = new ArrayList<List<Character>>(levels.length);

        maxHeight = max(levels);
        for(int i = 0; i < levels.length; ++i){
            map.add(new ArrayList<Character>(maxHeight));
            for(int j = 0; j < levels[i]; ++j){
               map.get(i).add(BLOCK);
            }
            for(int j = levels[i]; j < maxHeight; ++j){
                map.get(i).add(EMPTY);
            }
        }
        fill();
    }

    /**
     * Search for maximum value
     * @param array heights' levels
     * @return position of maximum
     */
    private static int max(int[] array) {
        int maximum = array[0];
        for (int value : array) {
            if (value > maximum) {
                maximum = value;
            }
        }
        return maximum;
    }

    /**
     * Fill map (surface) with water
     * Iterates over separated (by 0) segments
     */
    private void fill(){
        int mark = 0;
        for(int i = 0; i < levels.length; ++i){
            if(levels[i] == 0 || i == levels.length - 1){
                fillIteration(mark, i);
                mark = i + 1;

            }
        }
    }

    /**
     * Fills segment with coords given
     * @param left start point
     * @param right end point
     */
    private void fillIteration(int left, int right){
        int i = left;
        int leftMax;
        int rightLocalMax;
        while(i < right ) {
            leftMax = findDecreasingProgression(i, right);
            rightLocalMax = posMax(leftMax, right);

            if(leftMax >= rightLocalMax) {
                break;
            }
            fillBetween(leftMax, rightLocalMax);
            i = rightLocalMax;
        }
    }

    /**
     * Fills with water shelf
     * @param leftMax start point
     * @param rightLocalMax end point
     */
    private void fillBetween(int leftMax, int rightLocalMax) {
        if(rightLocalMax == levels.length){
            return;
        }
        int waterLevel = Math.min(levels[leftMax], levels[rightLocalMax]);
        for(int i = leftMax; i <= rightLocalMax; ++i){
            for (int j = levels[i]; j < waterLevel; ++j){
                waterCount++;
                map.get(i).set(j, WATER);
            }
        }
    }

    /**
     * Finds first occurrence of maximum value on interval
     * @param pos start point
     * @param right end point
     * @return position of max value
     */
    private int posMax(int pos, int right) {
        int maximum = levels[pos+1];
        int maxPos = 0;
        int i;
        for (i = pos + 1; i <= right; ++i) {
            if (levels[i] > maximum) {
                maximum = levels[i];
                maxPos = i;
            }
        }
        return maxPos;
    }

    /**
     * Finds decreasing progression
     * @param i start point
     * @param right end point
     * @return positions, where values began to decrease
     */
    private int findDecreasingProgression(int i, int right) {
        int leftMax = levels[i];
        while (i + 1 < right && levels[i] <= levels[i + 1]){
            i++;
            if(levels[i] > leftMax){
                return i;
            }
        }
        return i;
    }


    /**
     * Get count of WATER blocks
     * @return count
     */
    public int getWaterCount() {
        return waterCount;
    }

    /**
     * Get map (surface) with BLOCK and WATER blocks
     * @return map
     */
    public List<List<Character>> getData(){ return map;}
}
