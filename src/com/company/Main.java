package com.company;

class Main {
    private static final int LEVELS_LIMIT = 2048;
    private static final int HEIGHT_LIMIT = 2048;
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Invalid usage: arguments' count must be one or greater.");
            System.out.println("Usage: java -jar waterFill.jar [<level>...]");
            return;
        }
        if(args.length > LEVELS_LIMIT){
            System.out.println("Error: too many args (" + LEVELS_LIMIT + " - maximum");
            return;
        }

        int[] intArgs = new int[args.length];
        for(int i = 0; i < args.length; ++i){
            try {
                intArgs[i] = Integer.decode(args[i]);
                if(intArgs[i] > HEIGHT_LIMIT){
                    throw new IllegalArgumentException();
                }
            }
            catch (NumberFormatException e){
                System.out.println("Invalid " + i + "-th level format: must be int (given: " + args[i] + ")");
                return;
            } catch (Exception e) {
                System.out.println("Error: " + i + " element greater than " + HEIGHT_LIMIT + " .");
            }
            if(intArgs[i] < 0) intArgs[i] = 0;
        }
	    WaterFill waterFill = new WaterFill(intArgs);
        System.out.println("water cells: " + waterFill.getWaterCount());
        VisualModel visualModel = new VisualModel();
        visualModel.show(waterFill.getData());
    }
}
