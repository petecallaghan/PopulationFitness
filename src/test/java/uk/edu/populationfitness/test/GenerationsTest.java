package uk.edu.populationfitness.test;

import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.output.GenerationsReader;
import uk.edu.populationfitness.output.GenerationsWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class GenerationsTest {
    @Test public void testProduceGenerationHistory(){
        // Given a standard configuration ...
        Config config = new Config();
        config.setMinBreedingAge(1); // so we get some babies
        Population population = new Population(config);
        Generations generations = new Generations(population);
        // ... with some epochs
        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(config, -50).fitness(1.0).capacity(4000));
        epochs.setFinalEpochYear(-40);

        // When the simulation runs through the epochs
        generations.createForAllEpochs(epochs);

        // Then we get a history of the simulation
        assertEquals(11, generations.history.size());
    }

    @Test public void addGenerationStatitics(){
        // Given two sets of generation statistics
        Config config = new Config();
        Epoch epoch = new Epoch(config, -50);
        GenerationStatistics first = new GenerationStatistics(epoch, epoch.start_year,  100, 10, 20, 12, 13, 1.0, 2.0);
        GenerationStatistics second = new GenerationStatistics(epoch, epoch.start_year,  23, 1, 5, 120, 78, 1.0, 2.0);

        // When they are added
        GenerationStatistics result = GenerationStatistics.add(first, second);

        // Then the results are correct
        assertAreAdded(first, second, result);
    }

    private static void assertAreAdded(GenerationStatistics first, GenerationStatistics second, GenerationStatistics result) {
        assertEquals(result.year, first.year);
        assertEquals(result.year, second.year);
        assertEquals(result.epoch.start_year, first.epoch.start_year);
        assertEquals(result.epoch.end_year, first.epoch.end_year);
        assertEquals(result.epoch.expected_max_population, first.epoch.expected_max_population + second.epoch.expected_max_population);
        assertEquals(result.epoch.environment_capacity, first.epoch.environment_capacity + second.epoch.environment_capacity);
        assertEquals(result.population, first.population + second.population);
        assertEquals(result.number_born, first.number_born + second.number_born);
        assertEquals(result.number_killed, first.number_killed + second.number_killed);
        assertEquals(result.capacity_factor,
                (first.capacity_factor * first.population +
                        second.capacity_factor * second.population) / result.population, 0.00001);
        assertEquals(result.average_age,
                (first.average_age * first.population +
                        second.average_age * second.population) / result.population, 0.00001);
        assertEquals(result.average_life_expectancy,
                (first.average_life_expectancy * first.number_killed +
                        second.average_life_expectancy * second.number_killed) / result.number_killed, 0.00001);
    }

    @Test public void addCollectionsOfGenerationStatistics(){
        // Given two collections of statistics
        Config config = new Config();
        Epoch epoch = new Epoch(config, -50);
        GenerationStatistics first = new GenerationStatistics(epoch, epoch.start_year,  100, 10, 20, 12, 13, 1.0, 2.0);
        GenerationStatistics second = new GenerationStatistics(epoch, epoch.start_year,  23, 1, 5, 120, 78, 1.0, 2.0);
        List<GenerationStatistics> firstSet = new ArrayList<>();
        firstSet.add(first);
        List<GenerationStatistics> secondSet = new ArrayList<>();
        secondSet.add(second);

        first.average_age = 10.5;
        second.average_age = 20.3;
        first.average_life_expectancy = 50.5;
        second.average_life_expectancy = 60.76;

        // When they are added
        List<GenerationStatistics> result = GenerationStatistics.add(firstSet, secondSet);

        // Then we have a collection of additions
        assertAreAdded(first, second, result.get(0));
    }

    @Test public void writeAndReadGenerationStatistics() throws IOException {
        // Given a standard configuration ...
        Config config = new Config();
        config.setMinBreedingAge(1); // so we get some babies
        Population population = new Population(config);
        Generations generations = new Generations(population);
        // ... with some epochs ...
        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(config, -25).fitness(1.0).capacity(4000));
        epochs.addNextEpoch(new Epoch(config, 0).fitness(1.0).capacity(4000));
        epochs.addNextEpoch(new Epoch(config, 25).fitness(1.0).capacity(4000));
        epochs.setFinalEpochYear(50);
        //  ... and some results written to a file
        generations.createForAllEpochs(epochs);
        String path = GenerationsWriter.writeCsv("test-results.csv", generations, new Tuning());

        // When the results are read back
        List<GenerationStatistics> readResult = GenerationsReader.readGenerations(config, path);

        // Then they are the same as those written
        assertAreEqual(generations.history, readResult);
    }

    private void assertAreEqual(List<GenerationStatistics> expected, List<GenerationStatistics> actual) {
        GenerationStatistics[] expectedArray = expected.toArray(new GenerationStatistics[0]);
        GenerationStatistics[] actualArray = actual.toArray(new GenerationStatistics[0]);
        assertEquals(expectedArray.length, actualArray.length);
        for(int i = 0; i < expectedArray.length; i++){
            GenerationStatistics e = expectedArray[i];
            GenerationStatistics a = actualArray[i];

            assertAreEqual(e, a);
        }
    }

    private void assertAreEqual(GenerationStatistics e, GenerationStatistics a) {
        assertEquals(e.epoch.start_year, a.epoch.start_year);
        assertEquals(e.epoch.end_year, a.epoch.end_year);
        assertEquals(e.epoch.environment_capacity, a.epoch.environment_capacity);
        assertEquals(e.epoch.isFitnessEnabled(), a.epoch.isFitnessEnabled());
        assertEquals(e.epoch.breedingProbability(), a.epoch.breedingProbability(), 0.01);
        assertEquals(e.year, a.year);
        assertEquals(e.epoch.fitness(), a.epoch.fitness(), 0.000001);
        assertEquals(e.epoch.expected_max_population, a.epoch.expected_max_population);
        assertEquals(e.population, a.population);
        assertEquals(e.number_born, a.number_born);
        assertEquals(e.number_killed, a.number_killed);
        assertEquals(e.bornElapsedInHundredths(), a.bornElapsedInHundredths(), 0.001);
        assertEquals(e.killElapsedInHundredths(), a.killElapsedInHundredths(), 0.001);
        assertEquals(e.average_fitness, a.average_fitness, 0.001);
        assertEquals(e.average_factored_fitness, a.average_factored_fitness, 0.001);
        assertEquals(e.fitness_deviation, a.fitness_deviation, 0.001);
        assertEquals(e.average_age, a.average_age, 0.001);
        assertEquals(e.capacity_factor, a.capacity_factor, 0.001);
        assertEquals(e.average_mutations, a.average_mutations, 0.001);
        assertEquals(e.average_life_expectancy, a.average_life_expectancy, 0.001);
    }
}
