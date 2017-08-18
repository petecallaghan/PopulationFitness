package uk.edu.populationfitness.test;

import org.junit.Assert;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.Function;

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

    private void tune(Function function, int numberOfGenes, double minFactor, double maxFactor, double increment, int percentage){
        Config config = new Config();
        config.genesFactory.function = function;
        config.number_of_genes = numberOfGenes;
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = new Epochs(config);

        final double historic_kill = 1.066;

        epochs.addNextEpoch(new Epoch(config, -50).max(config.initial_population).kill(historic_kill).capacity(config.initial_population));
        epochs.addNextEpoch(new Epoch(config, 400).max(1700).kill(historic_kill).capacity(1700));
        epochs.addNextEpoch(new Epoch(config, 1086).max(4800).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1450).max(30000).kill(historic_kill));
        epochs.setFinalEpochYear(1901);

        Assert.assertTrue(generations.tuneFitnessFactorsForAllEpochs(epochs, minFactor, maxFactor, increment, percentage));

        epochs.printFitnessFactors();
    }

    @Test public void testTuneSinPiOver2For4(){
        tune(Function.SinPiOver2, 4, 0.5, 1.5, 0.01, 10);
    }

    @Test public void testTuneSinPiOver2For100(){
        tune(Function.SinPiOver2, 100, 0.5, 3, 0.01, 20);
    }

    @Test public void testTuneRastrigin(){
        tune(Function.Rastrigin, 100, 0.3, 10, 0.005, 25);
    }
}
