package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.output.EpochsWriter;

import java.io.IOException;

public class TuneFunctionsTest {

    private void tune(Function function, FitnessRange range, int numberOfGenes, int sizeOfGenes, double minFactor, double maxFactor, double increment, int percentage) throws IOException {
        RepeatableRandom.resetSeed();
        Config config = new Config();
        config.genesFactory.function = function;
        config.number_of_genes = numberOfGenes;
        config.size_of_each_gene = sizeOfGenes;
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
        tune(Function.SinPiLinear, new FitnessRange(), 4, 10, 0.01, 2, 0.000001, 15);
    }

    @Test public void testTuneSinPiLinear2For100() throws IOException {
        tune(Function.SinPiLinear, new FitnessRange(), 100, 10, 0.0, 80.0, 0.000001, 15);
    }

    @Test public void testTuneRastrigin() throws IOException {
        tune(Function.Rastrigin, new FitnessRange().min(0).max(11500), 100, 10, 0.0, 2.0, 0.000001, 30);
    }

    @Test public void testTuneRastrigin20000() throws IOException {
        //tune(Function.Rastrigin, new FitnessRange().min(0).max(1014841), 20000, 2250, 50, 550, 0.001, 30);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, new FitnessRange().min(0).max(420), 100, 10, 0.005, 5, 0.00001, 10);
    }

    @Test public void testDiscoverSphere10000() throws IOException {
        //tune(Function.Sphere, new FitnessRange().min(0).max(15050), 10000, 2250, 0.001, 2, 0.000001, 80);
    }

    @Test public void testDiscoverSphere20000() throws IOException {
        //tune(Function.Sphere, new FitnessRange().min(0).max(29363), 20000, 2250, 0.00001, 2, 0.00000001, 80);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, new FitnessRange().min(-2500).max(800), 100, 10, 0.005, 20, 0.00000001, 20);
    }

    @Test public void testTuneStyblinksiTang20000() throws IOException {
        //tune(Function.StyblinksiTang, new FitnessRange().min(-20757).max(125), 20000, 2250, 0.05, 20, 0.0001, 30);
    }

    @Test public void  testTuneSchwefel() throws IOException {
        tune(Function.Schwefel, new FitnessRange().min(0).max(10000.0), 100, 10, 0.001, 20, 0.00000001, 30);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, new FitnessRange(), 100, 10, 0.001, 20, 0.00000001, 10);
    }

    @Test public void testTuneRosenbrock20000() throws IOException {
        //tune(Function.Rosenbrock, new FitnessRange().min(0).max(2497303), 20000, 2250, 0.001, 20, 0.0001, 30);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, new FitnessRange().min(0).max(17), 100, 10, 0.001, 2, 0.00000001, 10);
    }

    @Test public void testTuneSumOfPowers1000() throws IOException {
        tune(Function.SumOfPowers, new FitnessRange().min(0).max(17), 1000, 10, 0.001, 2, 0.00000001, 10);
    }

    @Test public void testTuneSumOfPowers10000() throws IOException {
        //tune(Function.SumOfPowers, new FitnessRange().min(0).max(17), 10000, 10, 0.001, 2, 0.00000001, 20);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, new FitnessRange().min(0).max(2140), 100, 10, 0.001, 2, 0.00000001, 15);
    }

    @Test public void testTuneAckleys() throws IOException {
        tune(Function.Ackleys, new FitnessRange(), 100, 10, 0.001, 5, 0.00000001, 20);
    }

    @Test public void testTuneAlpine() throws IOException {
        tune(Function.Alpine, new FitnessRange(), 100, 10, 0.001, 5, 0.00000001, 25);
    }

    @Test public void testTuneBrown() throws IOException {
        tune(Function.Brown, new FitnessRange(), 100, 10, 0.001, 5, 0.00000001, 15);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        tune(Function.ChungReynolds, new FitnessRange(), 100, 10, 0.001, 8, 0.00000001, 15);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        tune(Function.DixonPrice, new FitnessRange(), 100, 10, 0.001, 8, 0.00000001, 20);
    }

    @Test public void testTuneExponential() throws IOException {
        tune(Function.Exponential, new FitnessRange(), 100, 10, 0.001, 2, 0.00000001, 15);
    }

    @Test public void testTuneGriewank() throws IOException {
        tune(Function.Griewank, new FitnessRange(), 100, 10, 0.001, 4, 0.00000001, 15);
    }

    @Test public void testTuneQing() throws IOException {
        tune(Function.Qing, new FitnessRange(), 100, 10, 0.001, 4, 0.00000001, 30);
    }
}
