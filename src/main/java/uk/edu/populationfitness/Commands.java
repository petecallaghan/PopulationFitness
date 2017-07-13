package uk.edu.populationfitness;

import uk.edu.populationfitness.models.RepeatableRandom;

/**
 * Created by pete.callaghan on 07/07/2017.
 */
public class Commands {
    public static void configure(String[] args){

        if (args.length % 2 == 1){
            showHelp();
        }

        for(int i = 0; i < args.length - 1; i+= 2){
            String argument = args[i].toLowerCase();
            try {
                if (argument.startsWith("-s")){
                    long seed = Long.decode(args[i + 1]);
                    RepeatableRandom.setSeed(seed);
                    continue;
                }
                if (argument.startsWith("-p")){
                    double scale = Double.parseDouble(args[i + 1]);
                    Tuning.population_scale = scale;
                    continue;
                }
            }
            catch (Exception ignored){
            }
            showHelp();
        }
    }

    private static void showHelp() {
        System.out.println("Commands:");
        System.out.println("    -s [seed]");
        System.out.println("    -p [population population_scale]");
        System.exit(0);
    }
}
