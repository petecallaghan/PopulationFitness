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
    private static final int ReducedPopulation = 1;

    private static final int SmallGenes = 100000;

    private static final int NumberOfGenes = 10;

    private static final int SizeOfGenes = 4;

    private static final int PopulationRatio = 1;

    private static final String EpochsPath = "epochs";

    private void tune(Function function,
                      double maxFactor,
                      int percentage) throws IOException {
        RepeatableRandom.resetSeed();
        Config config = new Config();
        config.getGenesFactory().useFitnessFunction(function);
        config.setNumberOfGenes(NumberOfGenes);
        config.setSizeOfEachGene(SizeOfGenes);
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = UkPopulationEpochs.define(config);
        config.setGenesFactory(new GenesTimerFactory(config.getGenesFactory()));
        GenesTimer.resetAll();

        chooseCacheForGeneSize(NumberOfGenes, SizeOfGenes);

        epochs.reducePopulation(PopulationRatio);
        config.setInitialPopulation(epochs.first().environment_capacity);

        PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, percentage);

        epochs.printFitnessFactors();

        GenesTimer.showAll();

        SharedCache.cache().close();

        Assert.assertTrue(result == PopulationComparison.WithinRange);

        // Record the result
        EpochsWriter.writeCsv(EpochsPath, function, config.getNumberOfGenes(), config.getSizeOfEachGene(), config.getMutationsPerGene(), epochs);
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
        tune(Function.Rastrigin, 2.0, 10);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, 5, 10);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, 10, 10);
    }

    @Test public void  testTuneSchwefel226() throws IOException {
        tune(Function.Schwefel226, 20, 10);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, -1, 10);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, 10, 10);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, 20, 10);
    }

    @Test public void testTuneAckleys() throws IOException {
        tune(Function.Ackleys, 5, 10);
    }

    @Test public void testTuneAlpine() throws IOException {
        tune(Function.Alpine, 20, 10);
    }

    @Test public void testTuneBrown() throws IOException {
        tune(Function.Brown, 5, 10);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        tune(Function.ChungReynolds, 8, 10);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        tune(Function.DixonPrice, 8, 10);
    }

    @Test public void testTuneExponential() throws IOException {
        tune(Function.Exponential, 2, 10);
    }

    @Test public void testTuneGriewank() throws IOException {
        tune(Function.Griewank, 400, 10);
    }

    @Test public void testTuneQing() throws IOException {
        tune(Function.Qing, 8, 10);
    }

    @Test public void testTuneSalomon() throws IOException {
        tune(Function.Salomon, 4, 10);
    }

    @Test public void testTuneSchumerSteiglitz() throws IOException {
        tune(Function.SchumerSteiglitz, 8, 10);
    }

    @Test public void testTuneSchwefel220() throws IOException {
        tune(Function.Schwefel220, 4, 10);
    }

    @Test public void testTuneTrid() throws IOException {
        tune(Function.Trid, 10.0, 10);
    }

    @Test public void testTuneZakharoy() throws IOException {
        tune(Function.Zakharoy, 100.0, 10);
    }
}
