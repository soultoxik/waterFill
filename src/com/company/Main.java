package com.company;

class Main {
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Invalid usage: arguments' count must be one or greater.");
            System.out.println("Usage: java -jar waterFill.jar [<level>...]");
            return;
        }
        int[] intArgs = new int[args.length];
        for(int i = 0; i < args.length; ++i){
            try {
                intArgs[i] = Integer.decode(args[i]);
            }
            catch (NumberFormatException e){
                System.out.println("Invalid " + i + "-th level format: must be int (given: " + args[i] + ")");
                return;
            }
            if(intArgs[i] < 0) intArgs[i] = 0;
        }
	    WaterFill waterFill = new WaterFill(intArgs);
        System.out.println("water cells: " + waterFill.getWaterCount());
        VisualModel visualModel = new VisualModel();
        visualModel.show(waterFill.getData());
    }
}
