package uk.edu.populationfitness.test;

import org.junit.Assert;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.output.EpochsWriter;

import java.io.IOException;

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

    private void tune(Function function, FitnessRange range, int numberOfGenes, double minFactor, double maxFactor, double increment, int percentage) throws IOException {
        RepeatableRandom.resetSeed();
        Config config = new Config();
        config.genesFactory.function = function;
        config.number_of_genes = numberOfGenes;
        config.range.min(range.min()).max(range.max());
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = UkPopulationEpochs.define(config);

        PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, minFactor, maxFactor, increment, percentage);

        epochs.printFitnessFactors();
        Assert.assertTrue(result == PopulationComparison.WithinRange);

        // Record the result
        EpochsWriter.writeCsv(function, config.number_of_genes, epochs);
    }

    @Test public void testTuneSinPiLinear2For4() throws IOException {
        tune(Function.SinPiLinear, new FitnessRange(), 4, 0.01, 2, 0.000001, 15);
    }

    @Test public void testTuneSinPiLinear2For100() throws IOException {
        tune(Function.SinPiLinear, new FitnessRange(), 100, 0.0, 80.0, 0.000001, 10);
    }

    @Test public void testTuneSinPiOver2For4() throws IOException {
        //tune(Function.SinPiOver2, new FitnessRange(), 4, 0.0, 4.0, 0.01, 10);
    }

    @Test public void testTuneSinPiOver2For100() throws IOException {
        //tune(Function.SinPiOver2, new FitnessRange(), 100, 0.0, 80.0, 0.000001, 10);
    }

    @Test public void testTuneRastrigin() throws IOException {
        tune(Function.Rastrigin, new FitnessRange().min(0).max(10283), 100, 5, 40, 0.001, 10);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, new FitnessRange().min(0).max(390), 100, 0.005, 5, 0.00001, 10);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, new FitnessRange().min(-2440).max(770), 100, 0.005, 20, 0.00000001, 5);
    }

    @Test public void testTuneSchwefel() throws IOException {
        tune(Function.Schwefel, new FitnessRange().min(0).max(9641.1), 100, 0.001, 20, 0.00000001, 5);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, new FitnessRange().min(0).max(31860), 100, 0.001, 20, 0.00000001, 5);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, new FitnessRange().min(0).max(6), 100, 0.001, 2, 0.00000001, 5);
    }

    @Test public void testTuneSumOfPowers1000() throws IOException {
        tune(Function.SumOfPowers, new FitnessRange().min(0).max(11), 1000, 0.001, 2, 0.00000001, 5);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, new FitnessRange().min(0).max(2140), 100, 0.001, 2, 0.00000001, 5);
    }
}
