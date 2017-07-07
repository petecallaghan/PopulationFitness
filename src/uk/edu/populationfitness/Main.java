package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;

public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);

        // 1 simulant equals 1000 of the population. Divide all populations by 1000
        Commands commands = new Commands(31l, config.initial_population_size, args);

        config.initial_population_size = commands.population;
        RepeatableRandom.setSeed(commands.seed);

        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(-50).fitnessFactor(1.0).killConstant(1.0).maxPopulation(4000).environmentCapacity(config.initial_population_size));
        epochs.addNextEpoch(new Epoch(400).fitnessFactor(1.5).maxPopulation(2000));//.environmentCapacity(config.initial_population_size / 2));
        epochs.setFinalEpochYear(500);
        //epochs.addNextEpoch(new Epoch(550).fitnessFactor(1.0).maxPopulation(2000));
        /*epochs.addNextEpoch(new Epoch(1086).fitnessFactor(1.1).maxPopulation(2000));
        epochs.addNextEpoch(new Epoch(1300).fitnessFactor(1.1).maxPopulation(4000));
        epochs.addNextEpoch(new Epoch(1348).fitnessFactor(3).maxPopulation(4000));
        epochs.addNextEpoch(new Epoch(1400).fitnessFactor(1.0).maxPopulation(25000));
        epochs.addNextEpoch(new Epoch(2016).fitnessFactor(3).maxPopulation(65000));
        epochs.addNextEpoch(new Epoch(2068).fitnessFactor(1.0).maxPopulation(65000));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);*/

        generations.createForAllEpochs(epochs);
    }
}
