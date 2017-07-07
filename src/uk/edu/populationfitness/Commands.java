package uk.edu.populationfitness;

/**
 * Created by pete.callaghan on 07/07/2017.
 */
public class Commands {
    private static final String HELP = "Commands: -s [seed] -p [population size]";

    public long seed;

    public int population;

    public Commands(long defaultSeed, int defaultPopulation, String[] args){
        seed = defaultSeed;
        population = defaultPopulation;

        if (args.length % 2 == 1){
            System.out.println(HELP);
            System.exit(0);
        }

        for(int i = 0; i < args.length - 1; i+= 2){
            String argument = args[i].toLowerCase();
            try {
                if (argument.startsWith("-s")){
                    seed = Long.decode(args[i + 1]);
                    continue;
                }
                if (argument.startsWith("-p")){
                    population = Integer.decode(args[i + 1]);
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
