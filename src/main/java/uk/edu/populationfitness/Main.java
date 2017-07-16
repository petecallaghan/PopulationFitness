package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class Main {

    private static int scale(int population){
        // 1 simulant equals X of the population. Divide all populations by X
        return (int)(population * Tuning.population_scale);
    }

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = new Epochs(config);

        Tuning tuning = Commands.configure(config, args);

        // https://en.wikipedia.org/wiki/Demography_of_England
        // Create baseline population
        final int reduced_baseline = (int)((1700.0 / 4000) * config.initial_population);
        epochs.addNextEpoch(new Epoch(config, -50).fitness(tuning.historic_fit).max(scale(config.initial_population)).kill(tuning.historic_kill).capacity(scale(config.initial_population)));
        epochs.addNextEpoch(new Epoch(config, 400).fitness(tuning.historic_fit).max(scale(reduced_baseline)).kill(tuning.historic_kill).capacity(scale(reduced_baseline)));

        // Historic growth to Bubonic plague
        epochs.addNextEpoch(new Epoch(config, 1086).fitness(tuning.historic_fit).max(scale(4800)).kill(tuning.historic_kill));

        // Plague
        epochs.addNextEpoch(new Epoch(config, 1348).fitness(tuning.disease_fit).max(scale(1900)).kill(tuning.historic_kill));

        // Subsequent historic growth
        epochs.addNextEpoch(new Epoch(config, 1450).fitness(tuning.historic_fit).max(scale(30000)).kill(tuning.historic_kill));

        epochs.addNextEpoch(new Epoch(config, 1901).fitness(tuning.modern_fit).max(scale(65000)).kill(tuning.modern_kill).breedingProbability(tuning.modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 2016).fitness(tuning.disease_fit).max(scale(6500)).kill(tuning.historic_kill));
        epochs.addNextEpoch(new Epoch(config, 2068).fitness(tuning.historic_fit).max(scale(65000)).kill(tuning.historic_kill));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations);
    }
}
