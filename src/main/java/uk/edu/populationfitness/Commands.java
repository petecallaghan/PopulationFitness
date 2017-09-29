package uk.edu.populationfitness;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.RepeatableRandom;
import uk.edu.populationfitness.output.EpochsReader;
import uk.edu.populationfitness.output.TuningReader;

import java.io.IOException;
import java.util.List;

/**
 * Created by pete.callaghan on 07/07/2017.
 */
public class Commands {
    public static void configureTuningAndEpochsFromInputFiles(Config config, Tuning tuning, Epochs epochs, String[] args){
        if (args.length < 2){
            showHelp();
        }

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
                if (argument.startsWith("-t")){
                    TuningReader.read(tuning, args[i + 1]);
                    config.size_of_each_gene = tuning.size_of_genes;
                    config.number_of_genes = tuning.number_of_genes;
                    config.genesFactory.function = tuning.function;
                    config.range.min(tuning.min_fitness).max(tuning.max_fitness);
                    continue;
                }
                if (argument.startsWith("-e")){
                    List<Epoch> read = EpochsReader.readEpochs(config, args[i + 1]);
                    epochs.epochs.addAll(read);
                    continue;
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
        System.out.println("    -t [csv file containing tuning]");
        System.out.println("    -e [csv file containing epochs]");
        System.exit(0);
    }
}