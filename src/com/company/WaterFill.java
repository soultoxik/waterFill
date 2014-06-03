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

    public void show(){
        for(int i = maxHeight - 1; i >= 0; --i){
            for (List<Character> row : map) {
                System.out.print(row.get(i) + " ");
            }
            System.out.println();
        }
    }

    private static int max(int[] array) {
        int maximum = array[0];
        for (int value : array) {
            if (value > maximum) {
                maximum = value;
            }
        }
        return maximum;
    }

    public void fill(){
        int mark = 0;
        for(int i = 0; i < levels.length; ++i){
            if(levels[i] == 0 || i == levels.length - 1){
                fillIteration(mark, i);
                mark = i + 1;

            }
        }
    }

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


    public int getWaterCount() {
        return waterCount;
    }
    public List<List<Character>> getData(){ return map;}
}
