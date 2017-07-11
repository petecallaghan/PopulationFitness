package uk.edu.populationfitness.test;

import org.junit.jupiter.api.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;

import static org.junit.jupiter.api.Assertions.*;

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
        Epochs epochs = new Epochs();

        config.fitness_factor_adjstument = 2.0;

        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400).fitnessFactor(2.0));

        // When we iterate over the epochs we find the right fitness factors
        assertEquals(config.fitness_factor_adjstument, epochs.epochs.get(0).fitnessFactor());
        assertEquals(config.fitness_factor_adjstument * 2.0, epochs.epochs.get(1).fitnessFactor());
    }

    @Test public void testEpochKillConstants(){
        // Given a set of epochs
        Epochs epochs = new Epochs();
        Config config = new Config();
        epochs.addNextEpoch(new Epoch(config, -50));
        epochs.addNextEpoch(new Epoch(config, 400).killConstant(2.0));

        // When we iterate over the epochs we find the right kill constants
        assertEquals(Epoch.DEFAULT_KILL_CONSTANT, epochs.epochs.get(0).killConstant());
        assertEquals(2.0, epochs.epochs.get(1).killConstant());
    }

    @Test public void testEpochEnvironmentCapacity(){
        // Given a set of epochs
        Epochs epochs = new Epochs();
        Config config = new Config();
        epochs.addNextEpoch(new Epoch(config, -50).environmentCapacity(1000));
        epochs.addNextEpoch(new Epoch(config, 400));

        // When we iterate over the epochs we find the right environment capacity
        assertEquals(1000, epochs.epochs.get(0).environment_capacity);
        assertFalse(epochs.epochs.get(0).isCapacityUnlimited());
        assertEquals(Epoch.UNLIMITED_CAPACITY, epochs.epochs.get(1).environment_capacity);
        assertTrue(epochs.epochs.get(1).isCapacityUnlimited());
    }
}
