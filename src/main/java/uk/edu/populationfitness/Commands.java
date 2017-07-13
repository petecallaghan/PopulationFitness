package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.models.genes.BitSetGenesFactory;

/**
 * Created by pete.callaghan on 07/07/2017.
 */
public class Commands {
    public static void configure(Config config, String[] args){

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
                if (argument.startsWith("-f")){
                    int fitness = Integer.decode(args[i + 1]);
                    switch (fitness){
                        case 1:
                            config.genesFactory.fitness = BitSetGenesFactory.Fitness.SinPi;
                            continue;
                        case 2:
                            config.genesFactory.fitness = BitSetGenesFactory.Fitness.SinPiOver2;
                            continue;
                    }
                }
            }
            catch (Exception ignored){
                System.out.print(ignored);
            }
            showHelp();
        }
    }

    private static void showHelp() {
        System.out.println("Commands:");
        System.out.println("    -s [seed]");
        System.out.println("    -p [population population_scale]");
        System.out.println("    -f [fitness functions: 1 = Sin Pi, 2 = Sin Pi over 2]");
        System.exit(0);
    }
}
