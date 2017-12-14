package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.cache.DiskBackedGeneValues;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.models.genes.performance.GenesTimer;
import uk.edu.populationfitness.models.genes.performance.GenesTimerFactory;
import uk.edu.populationfitness.output.EpochsWriter;

import java.io.IOException;

public class TuneFunctionsTest {

    private static final int NoReduction = 1;
    
    private static final int Mutations = 1;
    private static final long MemorySpace = 1024L * 1024L * 1024L * 2L; // 2GB

    private void tune(Function function,
                      int numberOfGenes,
                      int sizeOfGenes,
                      double maxFactor,
                      int percentage,
                      int populationRatio, 
                      int mutations) throws IOException {
        RepeatableRandom.resetSeed();
        Config config = new Config();
        config.genesFactory.useFitnessFunction(function);
        config.number_of_genes = numberOfGenes;
        config.size_of_each_gene = sizeOfGenes;
        config.mutations_per_gene = mutations;
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = UkPopulationEpochs.define(config);
        config.genesFactory = new GenesTimerFactory(config.genesFactory);
        GenesTimer.resetAll();

        epochs.reducePopulation(populationRatio);
        config.initial_population = epochs.first().environment_capacity;

        PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, percentage);

        epochs.printFitnessFactors();

        GenesTimer.showAll();

        Assert.assertTrue(result == PopulationComparison.WithinRange);

        // Record the result
        EpochsWriter.writeCsv(function, config.number_of_genes, mutations, epochs);
    }

    @Test public void testTuneRastrigin() throws IOException {
        SharedCache.setDefault();
        tune(Function.Rastrigin, 100, 10, 2.0, 10, NoReduction, Mutations);
    }

    @Test public void testTuneRastrigin20000() throws IOException {
        //tune(Function.Rastrigin, 20000, 2250, 20.0, 80, 200, Mutations);
    }

    @Test public void testTuneRastrigin20000With100Mutations() throws IOException {
        DiskBackedGeneValues cache = new DiskBackedGeneValues(MemorySpace);
        SharedCache.set(cache);
        tune(Function.Rastrigin, 20000, 2250, 20.0, 20, 100, 100);
        cache.close();
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, 100, 10, 5, 20, NoReduction, Mutations);
    }

    @Test public void testDiscoverSphere10000() throws IOException {
        //tune(Function.Sphere, 10000, 2250, 10, 80, 100, Mutations);
    }

    @Test public void testDiscoverSphere20000() throws IOException {
        //tune(Function.Sphere, 20000, 2250, 10, 80, 200, Mutations);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        SharedCache.setDefault();
        tune(Function.StyblinksiTang, 100, 10, 4, 30, NoReduction, Mutations);
    }

    @Test public void testTuneStyblinksiTang20000() throws IOException {
        //tune(Function.StyblinksiTang, 20000, 2250, 4, 80, 150, Mutations);
    }

    @Test public void  testTuneSchwefel226() throws IOException {
        SharedCache.setDefault();
        tune(Function.Schwefel226, 100, 10, 20, 15, NoReduction, Mutations);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        SharedCache.setDefault();
        tune(Function.Rosenbrock, 100, 10, 10, 30, NoReduction, Mutations);
    }

    @Test public void testTuneRosenbrock20000() throws IOException {
        //tune(Function.Rosenbrock, 20000, 2250, 20, 25, 150, Mutations);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        SharedCache.setDefault();
        tune(Function.SumOfPowers, 100, 101, 10, 15, NoReduction, Mutations);
    }

    @Test public void testTuneSumOfPowers1000() throws IOException {
        SharedCache.setDefault();
        tune(Function.SumOfPowers, 1000, 10, 20, 15, NoReduction, Mutations);
    }

    @Test public void testTuneSumOfPowers10000() throws IOException {
        SharedCache.setDefault();
        tune(Function.SumOfPowers, 10000, 10, 20, 15, 100, Mutations);
    }

    @Test public void testTuneSumSquares() throws IOException {
        SharedCache.setDefault();
        tune(Function.SumSquares, 100, 10, 20, 15, NoReduction, Mutations);
    }

    @Test public void testTuneAckleys() throws IOException {
        SharedCache.setDefault();
        tune(Function.Ackleys, 100, 10, 5, 15, NoReduction, Mutations);
    }

    @Test public void testTuneAlpine() throws IOException {
        SharedCache.setDefault();
        tune(Function.Alpine, 100, 10, 10, 20, NoReduction, Mutations);
    }

    @Test public void testTuneBrown() throws IOException {
        SharedCache.setDefault();
        tune(Function.Brown, 100, 10, 5, 15, NoReduction, Mutations);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        SharedCache.setDefault();
        tune(Function.ChungReynolds, 100, 10, 8, 15, NoReduction, Mutations);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        SharedCache.setDefault();
        tune(Function.DixonPrice, 100, 10, 8, 15, NoReduction, Mutations);
    }

    @Test public void testTuneExponential() throws IOException {
        SharedCache.setDefault();
        tune(Function.Exponential, 100, 10, 2, 15, NoReduction, Mutations);
    }

    @Test public void testTuneGriewank() throws IOException {
        SharedCache.setDefault();
        tune(Function.Griewank, 100, 10, 4, 15, NoReduction, Mutations);
    }

    @Test public void testTuneQing() throws IOException {
        SharedCache.setDefault();
        tune(Function.Qing, 100, 10, 8, 15, NoReduction, Mutations);
    }

    @Test public void testTuneSalomon() throws IOException {
        SharedCache.setDefault();
        tune(Function.Salomon, 100, 10, 4, 15, NoReduction, Mutations);
    }

    @Test public void testTuneSchumerSteiglitz() throws IOException {
        SharedCache.setDefault();
        tune(Function.SchumerSteiglitz, 100, 10, 4, 15, NoReduction, Mutations);
    }

    @Test public void testTuneSchwefel220() throws IOException {
        SharedCache.setDefault();
        tune(Function.Schwefel220, 100, 10, 4, 15, NoReduction, Mutations);
    }

    @Test public void testTuneTrid() throws IOException {
        SharedCache.setDefault();
        tune(Function.Trid, 100, 10, 40000, 15, NoReduction, Mutations);
    }

    @Test public void testTuneZakharoy() throws IOException {
        SharedCache.setDefault();
        tune(Function.Zakharoy, 100, 10, 5000000, 15, NoReduction, Mutations);
    }
}
