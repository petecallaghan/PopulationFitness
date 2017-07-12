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
        double historic_fitness = 1.0;
        double disease_fitness = 50;
        double modern_fitness = 0.01;
        double modern_kill_constant = 1.001;
        double modern_breeding_probability = 0.13;
        epochs.addNextEpoch(new Epoch(config, -50).fitnessFactor(historic_fitness).maxPopulation(config.initial_population_size).environmentCapacity(config.initial_population_size));
        epochs.addNextEpoch(new Epoch(config, 400).fitnessFactor(historic_fitness).maxPopulation(1700).environmentCapacity(1700));
        epochs.addNextEpoch(new Epoch(config, 1086).fitnessFactor(historic_fitness).maxPopulation(4800));
        epochs.addNextEpoch(new Epoch(config, 1348).fitnessFactor(disease_fitness).maxPopulation(1900));
        epochs.addNextEpoch(new Epoch(config, 1450).fitnessFactor(historic_fitness).maxPopulation(30000));
        epochs.addNextEpoch(new Epoch(config, 1901).fitnessFactor(modern_fitness).maxPopulation(65000).killConstant(modern_kill_constant).breedingProbability(modern_breeding_probability));
        epochs.addNextEpoch(new Epoch(config, 2016).fitnessFactor(disease_fitness).maxPopulation(65000).killConstant(modern_kill_constant).breedingProbability(modern_breeding_probability));
        epochs.addNextEpoch(new Epoch(config, 2068).fitnessFactor(historic_fitness).maxPopulation(65000).killConstant(modern_kill_constant).breedingProbability(modern_breeding_probability));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

        generations.createForAllEpochs(epochs);

        GenerationsWriter.writeCsv(generations);
    }
}
