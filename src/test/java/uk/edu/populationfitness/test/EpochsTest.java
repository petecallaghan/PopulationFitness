package uk.edu.populationfitness.test;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.output.EpochsReader;
import uk.edu.populationfitness.output.EpochsWriter;

import java.io.IOException;


/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class EpochsTest {
    private static final int UNDEFINED_YEAR = -1;

    @Test public void testCreateEpochs(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs();

        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400));
        epochs.addNextEpoch(new Epoch(config, 550));
        epochs.addNextEpoch(new Epoch(config, 1086));
        epochs.addNextEpoch(new Epoch(config, 1300));
        epochs.addNextEpoch(new Epoch(config, 1348));
        epochs.addNextEpoch(new Epoch(config, 1400));
        epochs.addNextEpoch(new Epoch(config, 2016));
        epochs.addNextEpoch(new Epoch(config, 2068));
        epochs.setFinalEpochYear(-50 + config.getNumberOfYears() - 1);

        // When we iterate over the epochs
        int first_year = UNDEFINED_YEAR;
        int last_year = UNDEFINED_YEAR;
        int number_of_years = 0;

        for (Epoch e: epochs.epochs){
            for(int year = e.start_year; year <= e.end_year; year++){
                if (first_year == UNDEFINED_YEAR){
                    first_year =year;
                }
                last_year = year;
                number_of_years++;
            }
        }

        // Then we traverse all the years
        assertEquals(-50, first_year);
        assertEquals( -50 + config.getNumberOfYears() - 1, last_year);
        assertEquals( +config.getNumberOfYears(), number_of_years);
    }

    @Test public void testEpochFitnessFactors(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs();

        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400).fitness(2.0));

        // When we iterate over the epochs we find the right fitness factors
        assertEquals(1.0, epochs.epochs.get(0).fitness(), 0.1);
        assertEquals(2.0, epochs.epochs.get(1).fitness(), 0.1);
    }

    @Test public void testEpochEnvironmentCapacity(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(config, -50).capacity(1000));
        epochs.addNextEpoch(new Epoch(config, 400));

        // When we iterate over the epochs we find the right environment capacity
        assertEquals(1000, epochs.epochs.get(0).environment_capacity);
        assertEquals(epochs.epochs.get(0).environment_capacity, epochs.epochs.get(0).expected_max_population);
        assertFalse(epochs.epochs.get(0).isCapacityUnlimited());
        assertEquals(Epoch.UNLIMITED_CAPACITY, epochs.epochs.get(1).environment_capacity);
        assertTrue(epochs.epochs.get(1).isCapacityUnlimited());
        assertEquals(epochs.epochs.get(0).environment_capacity, epochs.epochs.get(0).prev_environment_capacity);
        assertEquals(epochs.epochs.get(0).environment_capacity, epochs.epochs.get(1).prev_environment_capacity);
    }

    @Test public void testEnvironmentCapacityScales(){
        Config config = new Config();
        Epoch epoch = new Epoch(config, 1000);
        epoch.end_year = 1500;
        epoch.prev_environment_capacity = 1000;
        epoch.environment_capacity = 2000;

        assertEquals(epoch.prev_environment_capacity, epoch.capacityForYear(epoch.start_year));
        assertEquals(epoch.environment_capacity, epoch.capacityForYear(epoch.end_year));
        assertEquals((epoch.environment_capacity + epoch.prev_environment_capacity)/2, epoch.capacityForYear((epoch.end_year+epoch.start_year)/2));
    }

    @Test public void testEpochWriteAndRead() throws IOException{
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = UkPopulationEpochs.define(config);
        double delta = 0.0000000001;

        // When we write them and then read them
        String path = EpochsWriter.writeCsv("epochs", Function.Undefined, config.getNumberOfGenes(), config.getSizeOfEachGene(), config.getMutationsPerGene(), epochs);
        Epochs found = new Epochs();
        found.epochs.addAll(EpochsReader.readEpochs(config, path));

        // Then we get back the original epochs
        assertEquals(epochs.epochs.size(), found.epochs.size());
        for(int i = 0; i < epochs.epochs.size(); i++){
            Epoch expected = epochs.epochs.get(i);
            Epoch actual = found.epochs.get(i);

            assertEquals(expected.start_year, actual.start_year);
            assertEquals(expected.end_year, actual.end_year);
            assertEquals(expected.environment_capacity, actual.environment_capacity);
            assertEquals(expected.breedingProbability(), actual.breedingProbability(), delta);
            assertEquals(expected.disease(), actual.disease());
            assertEquals(expected.fitness(), actual.fitness(), delta);
            assertEquals(expected.expected_max_population, actual.expected_max_population);
            assertEquals(expected.maxAge(), actual.maxAge());
            assertEquals(expected.maxBreedingAge(), actual.maxBreedingAge());
        }
    }
}
