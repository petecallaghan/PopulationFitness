package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.output.EpochsReader;
import uk.edu.populationfitness.output.TuningReader;
import uk.edu.populationfitness.simulation.RunType;


import java.util.List;

/**
 * Created by pete.callaghan on 07/07/2017.
 */

public class Commands {
    public static final String Seed = "-s";
    public static final String TuningFile = "-t";
    public static final String EpochsFile = "-e";
    public static final String Id = "-i";
    public static final String CommandLine = "-c";
    public static final String ProcessCount = "-p";
    public static final String RandomSeed = "random";

    /**
     * Defines the path of the tuning file read from arguments.
     */
    public static String tuningFile = "";

    /**
     * Defines the path of the epochs file read from arguments.
     */
    public static String epochsFile = "";

    /**
     * Defines the command line for any child processes
     */
    public static String childCommandLine = "";

    /**
     * Reads tuning and epochs from the process arguments
     *
     * @param config
     * @param tuning
     * @param epochs
     * @param args
     */
    public static void configureTuningAndEpochsFromInputFiles(Config config, Tuning tuning, Epochs epochs, String[] args){
        int parallelCount = 1;

        if (args.length < 2){
            showHelp();
        }

        if (args.length % 2 == 1){
            showHelp();
        }

        for(int i = 0; i < args.length - 1; i+= 2){
            final String argument = args[i].toLowerCase();
            final String value  = args[i + 1];
            try {
                if (argument.startsWith(Seed)){
                    long seed = getSeed(value);
                    RepeatableRandom.setSeed(seed);
                    continue;
                }
                if (argument.startsWith(TuningFile)){
                    tuningFile = value;
                    TuningReader.read(tuning, tuningFile);
                    config.size_of_each_gene = tuning.size_of_genes;
                    config.number_of_genes = tuning.number_of_genes;
                    config.genesFactory.useFitnessFunction(tuning.function);
                    config.range.min(tuning.min_fitness).max(tuning.max_fitness);
                    config.mutations_per_gene = tuning.mutations_per_gene;
                    parallelCount = tuning.parallel_runs;
                    continue;
                }
                if (argument.startsWith(EpochsFile)){
                    epochsFile = value;
                    List<Epoch> read = EpochsReader.readEpochs(config, epochsFile);
                    epochs.epochs.addAll(read);
                    continue;
                }
                if (argument.startsWith(Id)){
                    config.id = tuning.id = value;
                    continue;
                }
                if (argument.startsWith(CommandLine)){
                    childCommandLine = value;
                    continue;
                }
                if (argument.startsWith(ProcessCount)){
                    parallelCount = Integer.parseInt(value);
                    continue;
                }
            }
            catch (Exception ignored){
                System.out.print(ignored);
            }
            showHelp();
        }
        tuning.parallel_runs = parallelCount;
    }

    private static long getSeed(String arg) {
        // Will set the seed from the current time if 'random' is chosen
        if (arg.toLowerCase().startsWith(RandomSeed)){
            return System.currentTimeMillis();
        }
        return Long.decode(arg);
    }

    private static void showHelp() {
        System.out.println("Commands:");
        System.out.println("    -s [seed number]| [random to set a seed from the current time]");
        System.out.println("    -t [csv file containing tuning]");
        System.out.println("    -e [csv file containing epochs]");
        System.out.println("    -i [simulation id - used to generate the name of the output files");
        System.out.println("    -c [command line for child processes - eg '-Xms10g -Xmx10g -jar target/populationfitness.jar']");
        System.out.println("    -p [process count of child processes, defaults to value in tuning file");
        System.exit(0);
    }
}