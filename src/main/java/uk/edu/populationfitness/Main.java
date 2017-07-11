package uk.edu.populationfitness;

import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Config config = new Config();
        Population population = new Population(config);
        Generations generations = new Generations(population);

        // 1 simulant equals 1000 of the population. Divide all populations by 1000
        Commands.configure(config, args);

        // https://en.wikipedia.org/wiki/Demography_of_England
        Epochs epochs = new Epochs(config);
        double historic_fitness = 0.8;
        double disease_fitness = 1.1;
        double modern_fitness = 0.1;
        epochs.addNextEpoch(new Epoch(config, -50).fitnessFactor(1.0).killConstant(1.0).maxPopulation(config.initial_population_size).environmentCapacity(config.initial_population_size));
        epochs.addNextEpoch(new Epoch(config, 400).fitnessFactor(1.0).maxPopulation(1700).environmentCapacity(1700));
        epochs.addNextEpoch(new Epoch(config, 1086).fitnessFactor(0.6).maxPopulation(4800));
        epochs.addNextEpoch(new Epoch(config, 1348).fitnessFactor(1.7).maxPopulation(1900));
        epochs.addNextEpoch(new Epoch(config, 1450).fitnessFactor(0.6).maxPopulation(30000));
        epochs.addNextEpoch(new Epoch(config, 1901).fitnessFactor(modern_fitness).maxPopulation(65000).breedingProbability(0.30));
        epochs.setFinalEpochYear(2015);
        //epochs.addNextEpoch(new Epoch(config, 2016).fitnessFactor(disease_fitness).maxPopulation(65000));
        //epochs.addNextEpoch(new Epoch(config, 2068).fitnessFactor(historic_fitness).maxPopulation(65000));
        //epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations);
    }
}
