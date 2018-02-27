package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.cache.SharedCache;
import uk.edu.populationfitness.models.genes.cache.ThreadLocalGenesCache;
import uk.edu.populationfitness.models.genes.performance.GenesTimer;
import uk.edu.populationfitness.models.genes.performance.GenesTimerFactory;
import uk.edu.populationfitness.output.EpochsWriter;

import java.io.IOException;

public class TuneFunctionsTest {

    private static final int NoReduction = 1;

    private static final int ReducedPopulation = 100;
    
    private static final int Mutations = 100;

    private static final int SmallGenes = 100000;

    private static final int NumberOfGenes = 20000;

    private static final int SizeOfGenes = 1000;

    private static final int PopulationRatio = ReducedPopulation;

    private static final String EpochsPath = "epochs";

    private void tune(Function function,
                      double maxFactor,
                      int percentage) throws IOException {
        RepeatableRandom.resetSeed();
        Config config = new Config();
        config.genesFactory.useFitnessFunction(function);
        config.number_of_genes = NumberOfGenes;
        config.size_of_each_gene = SizeOfGenes;
        config.mutations_per_gene = Mutations;
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = UkPopulationEpochs.define(config);
        config.genesFactory = new GenesTimerFactory(config.genesFactory);
        GenesTimer.resetAll();

        chooseCacheForGeneSize(NumberOfGenes, SizeOfGenes);

        epochs.reducePopulation(PopulationRatio);
        config.initial_population = epochs.first().environment_capacity;

        PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, percentage);

        epochs.printFitnessFactors();

        GenesTimer.showAll();

        SharedCache.cache().close();

        Assert.assertTrue(result == PopulationComparison.WithinRange);

        // Record the result
        EpochsWriter.writeCsv(EpochsPath, function, config.number_of_genes, config.size_of_each_gene, config.mutations_per_gene, epochs);
    }

    private void chooseCacheForGeneSize(int numberOfGenes, int sizeOfGenes) {
        if (numberOfGenes * sizeOfGenes > SmallGenes){
            SharedCache.set(new ThreadLocalGenesCache(1));
        }
        else{
            SharedCache.setDefault();
        }
    }

    @Test public void testTuneRastrigin() throws IOException {
        tune(Function.Rastrigin, 2.0, 20);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, 5, 20);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, 10, 30);
    }

    @Test public void  testTuneSchwefel226() throws IOException {
        tune(Function.Schwefel226, 20, 20);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, -1, 30);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, 10, 20);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, 20, 20);
    }

    @Test public void testTuneAckleys() throws IOException {
        tune(Function.Ackleys, 5, 20);
    }

    @Test public void testTuneAlpine() throws IOException {
        tune(Function.Alpine, 20, 30);
    }

    @Test public void testTuneBrown() throws IOException {
        tune(Function.Brown, 5, 20);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        tune(Function.ChungReynolds, 8, 20);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        tune(Function.DixonPrice, 8, 20);
    }

    @Test public void testTuneExponential() throws IOException {
        tune(Function.Exponential, 2, 20);
    }

    @Test public void testTuneGriewank() throws IOException {
        tune(Function.Griewank, 4, 20);
    }

    @Test public void testTuneQing() throws IOException {
        tune(Function.Qing, 8, 20);
    }

    @Test public void testTuneSalomon() throws IOException {
        tune(Function.Salomon, 4, 15);
    }

    @Test public void testTuneSchumerSteiglitz() throws IOException {
        tune(Function.SchumerSteiglitz, 8, 20);
    }

    @Test public void testTuneSchwefel220() throws IOException {
        tune(Function.Schwefel220, 4, 20);
    }

    @Test public void testTuneTrid() throws IOException {
        tune(Function.Trid, 10.0, 20);
    }

    @Test public void testTuneZakharoy() throws IOException {
        tune(Function.Zakharoy, 100.0, 30);
    }
}
