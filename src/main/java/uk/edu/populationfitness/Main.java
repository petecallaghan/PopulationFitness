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
        Commands.configure(config, tuning, epochs, args);

        // Add the simulated epochs to the tuned epochs
        epochs.addNextEpoch(new Epoch(config, 1901).fitness(tuning.modern_fit).max(65000).kill(tuning.modern_kill).breedingProbability(tuning.modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 2016).fitness(tuning.disease_fit).max(65000).kill(tuning.historic_kill));
        epochs.addNextEpoch(new Epoch(config, 2068).fitness(tuning.historic_fit).max(65000).kill(tuning.historic_kill));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations);
    }
}
