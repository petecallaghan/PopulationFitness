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

        Tuning.historic_fit = 1.0;
        Tuning.disease_fit = 50;
        Tuning.modern_fit = 0.01;
        Tuning.modern_kill = 1.003;
        Tuning.historic_kill = 1.066;
        Tuning.modern_breeding = 0.13;
        config.initial_population = 4000;

        Commands.configure(args);

        // https://en.wikipedia.org/wiki/Demography_of_England
        // Create baseline population
        final int reduced_baseline = (int)((1700.0 / 4000) * config.initial_population);
        epochs.addNextEpoch(new Epoch(config, -50).fitness(Tuning.historic_fit).max(scale(config.initial_population)).kill(Tuning.historic_kill).capacity(scale(config.initial_population)));
        epochs.addNextEpoch(new Epoch(config, 400).fitness(Tuning.historic_fit).max(scale(reduced_baseline)).kill(Tuning.historic_kill).capacity(scale(reduced_baseline)));

        // Historic growth to Bubonic plague
        epochs.addNextEpoch(new Epoch(config, 1086).fitness(Tuning.historic_fit).max(scale(4800)).kill(Tuning.historic_kill));

        // Plague
        epochs.addNextEpoch(new Epoch(config, 1348).fitness(Tuning.disease_fit).max(scale(1900)).kill(Tuning.historic_kill));

        // Subsequent historic growth
        epochs.addNextEpoch(new Epoch(config, 1450).fitness(Tuning.historic_fit).max(scale(30000)).kill(Tuning.historic_kill));

        epochs.addNextEpoch(new Epoch(config, 1901).fitness(Tuning.modern_fit).max(scale(65000)).kill(Tuning.modern_kill).breedingProbability(Tuning.modern_breeding));
        epochs.addNextEpoch(new Epoch(config, 2016).fitness(Tuning.disease_fit).max(scale(6500)).kill(Tuning.historic_kill));
        epochs.addNextEpoch(new Epoch(config, 2068).fitness(Tuning.historic_fit).max(scale(65000)).kill(Tuning.historic_kill));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations);
    }
}
