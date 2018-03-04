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

        // When the population is created from scratch
        population.addNewIndividuals(BIRTH_YEAR);

        // Then it has the right number of individuals for the birth year
        // and each individual has a set of genes
        assertEquals(config.getInitialPopulation(), population.individuals.size());
        for(Individual i : population.individuals){
            assertEquals(BIRTH_YEAR, i.birth_year);
            assertFalse(i.genes.areEmpty());
        }
    }

    @Test public void testKillOffOldies(){
        // Given a population that is a mixture of oldies and babies, where fitness is ignored
        Config config = new Config();
        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(config, -50).disableFitness());
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        population.addNewIndividuals(BIRTH_YEAR);
        int current_year = BIRTH_YEAR + config.getMaxAge();
        population.addNewIndividuals(current_year);

        // When we kill off the elderly
        assertEquals(2 * config.getInitialPopulation(), population.individuals.size());
        int fatalities = population.killThoseUnfitOrReadyToDie(current_year, epochs.epochs.get(0));

        // Just the babies remain and the elderly were killed
        assertEquals(config.getInitialPopulation(), population.individuals.size());
        assertEquals(config.getInitialPopulation(), fatalities);
        for (Individual i: population.individuals){
            assertEquals(0, i.age(current_year));
        }
    }

    @Test public void testKillOffUnfit(){
        // Given a population that contains just babies
        Config config = new Config();
        Epochs epochs = new Epochs();
        epochs.addNextEpoch(new Epoch(config, -50));
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        population.addNewIndividuals(BIRTH_YEAR);

        // When we kill off the unfit
        int fatalities = population.killThoseUnfitOrReadyToDie(BIRTH_YEAR, epochs.epochs.get(0));

        // Some remain and some were killed
        assertTrue(0 < population.individuals.size());
        assertTrue(0 < fatalities);
        assertTrue(INITIAL_POPULATION_SIZE > population.individuals.size());
    }

    @Test public void testCreateANewGenerationFromTheCurrentOne(){
        // Given a population that is a mix of ages...
        Config config = new Config();
        config.setInitialPopulation(INITIAL_POPULATION_SIZE);
        Population population = new Population(config);
        int current_year = BIRTH_YEAR + config.getMaxBreedingAge();
        int breeding_birth_year = current_year - config.getMinBreedingAge();
        // .. some who will be too old to breed
        population.addNewIndividuals(BIRTH_YEAR);
        // .. some who will be too young to breed
        population.addNewIndividuals(current_year);
        // .. some who can breed
        population.addNewIndividuals(breeding_birth_year);

        // When we create a new population
        List<Individual> babies = population.addNewGeneration(new Epoch(config, current_year), current_year);

        // The right number of babies are produced and the babies are added
        assertTrue(babies.size() >= (config.getInitialPopulation() * config.getProbabilityOfBreeding() / 2) - config.getInitialPopulation() / 5);
        assertTrue(babies.size() <= (config.getInitialPopulation() * config.getProbabilityOfBreeding()) + config.getInitialPopulation() / 5);
        assertEquals((config.getInitialPopulation() * 3) + babies.size(),population.individuals.size());
    }
}
