package uk.edu.populationfitness.test;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Individual;


/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class IndividualTest {

    @Test public void testOldGuyReadyToDie(){
        // Given an old individual
        Config config = new Config();
        int birth_year = 1964;
        int current_year = birth_year + config.getMaxAge() + 10;
        Individual individual = new Individual(config, birth_year);

        // Then they are ready to die
        assertTrue(individual.isReadyToDie(current_year));
    }

    @Test public void testYoungGuyNotReadyToDie(){
        // Given a young individual
        Config config = new Config();
        int birth_year = 1964;
        int current_year = birth_year + config.getMaxAge() - 10;
        Individual individual = new Individual(config, birth_year);

        // Then they are not ready to die
        assertFalse(individual.isReadyToDie(current_year));
    }

    @Test public void testCanBreed(){
        // Given a breeding age individual
        Config config = new Config();
        int birth_year = 1964;
        int current_year = birth_year + config.getMinBreedingAge() + 1;
        Individual individual = new Individual(config, birth_year);

        // Then they can breed
        assertTrue(individual.canBreed(current_year));
    }

    @Test public void testYoungsterCannotBreed(){
        // Given a breeding age individual
        Config config = new Config();
        int birth_year = 1964;
        int current_year = birth_year + config.getMinBreedingAge() - 1;
        Individual individual = new Individual(config, birth_year);

        // Then they can breed
        assertFalse(individual.canBreed(current_year));
    }

    @Test public void testOldsterCannotBreed(){
        // Given a breeding age individual
        Config config = new Config();
        int birth_year = 1964;
        int current_year = birth_year + config.getMaxBreedingAge() + 1;
        Individual individual = new Individual(config, birth_year);

        // Then they can breed
        assertFalse(individual.canBreed(current_year));
    }
}
