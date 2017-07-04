package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);

        config.initial_population_size = 40;

        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(-50).fitnessFactor(1.0).killConstant(1.0).maxPopulation(40000).environmentCapacity(config.initial_population_size));
        epochs.addNextEpoch(new Epoch(400).fitnessFactor(1.5).maxPopulation(20000).environmentCapacity(config.initial_population_size / 2));
        epochs.addNextEpoch(new Epoch(550).fitnessFactor(1.0).maxPopulation(20000));
        epochs.addNextEpoch(new Epoch(1086).fitnessFactor(1.1).maxPopulation(20000));
        epochs.addNextEpoch(new Epoch(1300).fitnessFactor(1.1).maxPopulation(40000));
        epochs.addNextEpoch(new Epoch(1348).fitnessFactor(3).maxPopulation(40000));
        epochs.addNextEpoch(new Epoch(1400).fitnessFactor(1.0).maxPopulation(250000));
        epochs.addNextEpoch(new Epoch(2016).fitnessFactor(3).maxPopulation(650000));
        epochs.addNextEpoch(new Epoch(2068).fitnessFactor(1.0).maxPopulation(650000));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);
    }
}
