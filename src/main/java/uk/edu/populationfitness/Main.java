package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = new Epochs(config);
        Tuning tuning = new Tuning();
        tuning.id = config.id;

        // Read all the data from the arguments
        Commands.configureTuningAndEpochsFromInputFiles(config, tuning, epochs, args);

        // Add the simulated epochs to the tuned epochs
        int diseaseStartYear = epochs.last().end_year + 1;
        int recoveryStartYear = diseaseStartYear + 3;
        int finalYear = recoveryStartYear + 80;
        int maxExpected = epochs.last().expected_max_population;

        epochs.addNextEpoch(new Epoch(config, diseaseStartYear).fitness(tuning.disease_fit).max(maxExpected).kill(tuning.historic_kill));
        epochs.addNextEpoch(new Epoch(config, recoveryStartYear).fitness(tuning.historic_fit).max(maxExpected).kill(tuning.historic_kill));
        epochs.setFinalEpochYear(finalYear);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations, tuning);
    }
}
