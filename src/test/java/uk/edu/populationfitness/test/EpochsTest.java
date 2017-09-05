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
import java.util.List;


/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class EpochsTest {
    private static final int UNDEFINED_YEAR = -1;

    @Test public void testCreateEpochs(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs(config);

        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400));
        epochs.addNextEpoch(new Epoch(config, 550));
        epochs.addNextEpoch(new Epoch(config, 1086));
        epochs.addNextEpoch(new Epoch(config, 1300));
        epochs.addNextEpoch(new Epoch(config, 1348));
        epochs.addNextEpoch(new Epoch(config, 1400));
        epochs.addNextEpoch(new Epoch(config, 2016));
        epochs.addNextEpoch(new Epoch(config, 2068));
        epochs.setFinalEpochYear(-50 + config.number_of_years - 1);

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
        assertEquals( -50 + config.number_of_years - 1, last_year);
        assertEquals( + config.number_of_years, number_of_years);
    }

    @Test public void testEpochFitnessFactors(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs(config);

        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400).fitness(2.0));

        // When we iterate over the epochs we find the right fitness factors
        assertEquals(1.0, epochs.epochs.get(0).fitness(), 0.1);
        assertEquals(2.0, epochs.epochs.get(1).fitness(), 0.1);
    }

    @Test public void testEpochKillConstants(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs(config);
        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400).kill(2.0));

        // When we iterate over the epochs we find the right kill constants
        assertEquals(1.0, epochs.epochs.get(0).kill(), 0.1);
        assertEquals(2.0, epochs.epochs.get(1).kill(), 0.1);
    }

    @Test public void testEpochEnvironmentCapacity(){
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = new Epochs(config);
        epochs.addNextEpoch(new Epoch(config, -50).capacity(1000));
        epochs.addNextEpoch(new Epoch(config, 400));

        // When we iterate over the epochs we find the right environment capacity
        assertEquals(1000, epochs.epochs.get(0).environment_capacity);
        assertFalse(epochs.epochs.get(0).isCapacityUnlimited());
        assertEquals(Epoch.UNLIMITED_CAPACITY, epochs.epochs.get(1).environment_capacity);
        assertTrue(epochs.epochs.get(1).isCapacityUnlimited());
    }

    @Test public void testEpochWriteAndRead() throws IOException{
        // Given a set of epochs
        Config config = new Config();
        Epochs epochs = UkPopulationEpochs.define(config);
        double delta = 0.0000000001;

        // When we write them and then read them
        String path = EpochsWriter.writeCsv(Function.Undefined, 100, epochs);
        Epochs found = new Epochs(config);
        found.epochs.addAll(EpochsReader.readEpochs(config, path));

        // Then we get back the original epochs
        assertEquals(epochs.epochs.size(), found.epochs.size());
        for(int i = 0; i < epochs.epochs.size(); i++){
            Epoch expected = epochs.epochs.get(i);
            Epoch actual = found.epochs.get(i);

            assertEquals(expected.start_year, actual.start_year);
            assertEquals(expected.end_year, actual.end_year);
            assertEquals(expected.kill(), actual.kill(), delta);
            assertEquals(expected.environment_capacity, actual.environment_capacity);
            assertEquals(expected.breedingProbability(), actual.breedingProbability(), delta);
            assertEquals(expected.disease(), actual.disease());
            assertEquals(expected.fitness(), actual.fitness(), delta);
            assertEquals(expected.expected_max_population, actual.expected_max_population);
        }
    }
}
