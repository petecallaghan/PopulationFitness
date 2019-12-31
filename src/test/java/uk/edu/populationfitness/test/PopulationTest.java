package uk.edu.populationfitness.test;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Epoch;
import uk.edu.populationfitness.models.Epochs;
import uk.edu.populationfitness.models.Individual;
import uk.edu.populationfitness.models.Population;

import java.util.List;


/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class PopulationTest {
    private static final int INITIAL_POPULATION_SIZE = 30;
    private static final int BIRTH_YEAR = 1964;

    @Test public void testCreateNewNonEmptyPopulation(){
        // Given a small population
        Config config = new Config();
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        Epoch epoch = new Epoch(config, BIRTH_YEAR);

        // When the population is created from scratch
        population.addNewIndividuals(epoch, BIRTH_YEAR);

        // Then it has the right number of individuals for the birth year
        // and each individual has a set of genes
        assertEquals(config.getInitialPopulation(), population.individuals.size());
        for(Individual i : population.individuals){
            assertEquals(BIRTH_YEAR, i.birth_year);
        }
    }

    @Test public void testKillOffUnfit(){
        // Given a population that contains just babies
        Config config = new Config();
        Epochs epochs = new Epochs();
        Epoch epoch = new Epoch(config, -50);
        epoch.environment_capacity = INITIAL_POPULATION_SIZE;
        epoch.expected_max_population = INITIAL_POPULATION_SIZE;
        epochs.addNextEpoch(epoch);
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        population.addNewIndividuals(epoch, BIRTH_YEAR);

        // When we kill off the unfit
        int fatalities = population.killThoseUnfitOrReadyToDie(BIRTH_YEAR, epochs.epochs.get(0));

        // Some remain and some were killed
        assertTrue(0 < population.individuals.size());
        assertTrue(0 < fatalities);
        assertTrue(INITIAL_POPULATION_SIZE > population.individuals.size());
        assertEquals(population.capacityFactor(), 1.0, 0.1);
    }

    @Test public void testCreateANewGenerationFromTheCurrentOne(){
        // Given a population that is a mix of ages...
        Config config = new Config();
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        int current_year = BIRTH_YEAR + config.getMaxBreedingAge();
        int breeding_birth_year = current_year - config.getMinBreedingAge();
        Epoch epoch = new Epoch(config, BIRTH_YEAR);
        // .. some who will be too old to breed
        population.addNewIndividuals(epoch, BIRTH_YEAR);
        // .. some who will be too young to breed
        population.addNewIndividuals(epoch, current_year);
        // .. some who can breed
        population.addNewIndividuals(epoch, breeding_birth_year);

        // When we create a new population
        List<Individual> babies = population.addNewGeneration(new Epoch(config, current_year), current_year);

        // The right number of babies are produced and the babies are added
        assertTrue(babies.size() >= (config.getInitialPopulation() * config.getProbabilityOfBreeding() / 2) - config.getInitialPopulation() / 5);
        assertTrue(babies.size() <= (config.getInitialPopulation() * config.getProbabilityOfBreeding()) + config.getInitialPopulation() / 5);
        assertEquals((config.getInitialPopulation() * 3) + babies.size(),population.individuals.size());
    }
}
