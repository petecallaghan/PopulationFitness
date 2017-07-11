package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;

/**
 * Created by pete.callaghan on 07/07/2017.
 */
public class Commands {
    private static final String HELP = "Commands: -s [seed] -p [population size] -f [fitness factor] -k [kill factor]";

    public static void configure(Config config, String[] args){

        if (args.length % 2 == 1){
            System.out.println(HELP);
            System.exit(0);
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
                    int population = Integer.decode(args[i + 1]);
                    config.initial_population_size = population;
                    continue;
                }
                if (argument.startsWith("-f")){
                    double fitness = Double.parseDouble(args[i + 1]);
                    config.fitness_factor_adjstument = fitness;
                    continue;
                }
                if (argument.startsWith("-k")){
                    double kill_constant = Double.parseDouble(args[i + 1]);
                    config.kill_constant_adjustment = kill_constant;
                    continue;
                }
            }
            catch (Exception ignored){
            }
            System.out.println(HELP);
            System.exit(0);
        }
    }
}
