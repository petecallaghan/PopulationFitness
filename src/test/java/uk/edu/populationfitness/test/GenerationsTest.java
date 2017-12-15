package uk.edu.populationfitness.test;

import uk.edu.populationfitness.models.*;
import org.junit.Test;

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
        config.min_breeding_age = 1; // so we get some babies
        Population population = new Population(config);
        Generations generations = new Generations(population);
        // ... with some epochs
        Epochs epochs = new Epochs(config);
        epochs.addNextEpoch(new Epoch(config, -50).fitness(1.0).kill(1.0).capacity(4000));
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
        GenerationStatistics first = new GenerationStatistics(epoch, epoch.start_year,  100, 10, 20, 12, 13);
        GenerationStatistics second = new GenerationStatistics(epoch, epoch.start_year,  23, 1, 5, 120, 78);

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
    }

    @Test public void addCollectionsOfGenerationStatistics(){
        // Given two collections of statistics
        Config config = new Config();
        Epoch epoch = new Epoch(config, -50);
        GenerationStatistics first = new GenerationStatistics(epoch, epoch.start_year,  100, 10, 20, 12, 13);
        GenerationStatistics second = new GenerationStatistics(epoch, epoch.start_year,  23, 1, 5, 120, 78);
        List<GenerationStatistics> firstSet = new ArrayList<>();
        firstSet.add(first);
        List<GenerationStatistics> secondSet = new ArrayList<>();
        secondSet.add(second);

        // When they are added
        List<GenerationStatistics> result = GenerationStatistics.add(firstSet, secondSet);

        // Then we have a collection of additions
        assertAreAdded(first, second, result.get(0));
    }
}
