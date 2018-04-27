package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import org.junit.Test;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.performance.GenesTimer;
import uk.edu.populationfitness.models.genes.performance.GenesTimerFactory;
import uk.edu.populationfitness.output.EpochsWriter;
import uk.edu.populationfitness.output.TuningWriter;

import java.io.IOException;

public class TuneFunctionsTest {
    private static final int NumberOfGenes = 20000;

    private static final int SizeOfGenes = 1000;

    private static final int PopulationRatio = 100;

    private static final String EpochsPath = "epochs";

    private static final String TuningPath = "tuning";

    private static final int TuningPercentage = 15;

    private static final double MutationsPerIndividual = 150;

    private void tune(Function function, double maxFactor) throws IOException {
        tune(function, maxFactor, TuningPercentage);
    }

    private void tune(Function function, double maxFactor, int tuningPercentage) throws IOException {
        RepeatableRandom.resetSeed();

        final Config config = buildTimedTuningConfig(function);
        final Epochs epochs = UkPopulationEpochs.define(config);
        final Generations generations = new Generations(new Population(config));

        epochs.reducePopulation(PopulationRatio);
        config.setInitialPopulation(epochs.first().environment_capacity);
        config.setMutationsPerGene(MutationsPerIndividual);

        final PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, tuningPercentage);
        final Tuning tuning = createTuningFromEpochs(config, epochs);

        showResults(tuning);

        writeResults(function, config, epochs, tuning);

        assertTuned(result, tuning);
    }

    private void showResults(Tuning tuning) {
        showTuning(tuning);
        GenesTimer.showAll();
    }

    private void showTuning(Tuning tuning) {
        System.out.print("Tuned Disease fitness:");
        System.out.println(tuning.disease_fit);
        System.out.print("Tuned Historic fitness:");
        System.out.println(tuning.historic_fit);
        System.out.print("Tuned Modern fitness:");
        System.out.println(tuning.modern_fit);
    }

    private void assertTuned(PopulationComparison result, Tuning tuning) {
        // Ensure that we successfully tuned
        Assert.assertTrue(result == PopulationComparison.WithinRange);

        // Ensure that the tuning result is what we expect
        Assert.assertTrue(tuning.disease_fit < tuning.modern_fit);
    }

    private void writeResults(Function function, Config config, Epochs epochs, Tuning tuning) throws IOException {
        EpochsWriter.writeCsv(EpochsPath, function, config.getNumberOfGenes(), config.getSizeOfEachGene(), config.getMutationsPerGene(), epochs);
        TuningWriter.writeInPath(TuningPath, tuning);
    }

    private void setUpGeneTimers(Config config) {
        config.setGenesFactory(new GenesTimerFactory(config.getGenesFactory()));
        GenesTimer.resetAll();
    }

    private Config buildTimedTuningConfig(Function function) {
        Config config = new Config();
        config.getGenesFactory().useFitnessFunction(function);
        config.setNumberOfGenes(NumberOfGenes);
        config.setSizeOfEachGene(SizeOfGenes);
        config.scaleMutationsPerGeneFromBitCount(Config.MutationScale);
        setUpGeneTimers(config);
        return config;
    }

    private Tuning createTuningFromEpochs(Config config, Epochs epochs){
        Tuning tuning = new Tuning();
        tuning.function = config.getGenesFactory().getFitnessFunction();
        tuning.size_of_genes = config.getSizeOfEachGene();
        tuning.number_of_genes = config.getNumberOfGenes();
        tuning.parallel_runs = 1;
        tuning.series_runs = 1;
        tuning.mutations_per_gene = config.getMutationsPerGene();

        Epoch diseaseEpoch = findDiseaseEpoch(epochs);
        Epoch historicalEpoch = findHistoricalEpoch(epochs);
        Epoch modernEpoch = findModernEpoch(epochs);

        tuning.historic_fit = historicalEpoch.averageCapacityFactor() * historicalEpoch.fitness();
        tuning.modern_fit = modernEpoch.averageCapacityFactor() * modernEpoch.fitness();
        tuning.modern_breeding = modernEpoch.breedingProbability();
        tuning.disease_fit = diseaseEpoch.averageCapacityFactor() * diseaseEpoch.fitness();

        return tuning;
    }

    private Epoch findModernEpoch(Epochs epochs) {
        // Find the modern epoch with the max fitness factor
        Epoch modern = epochs.epochs.get(epochs.epochs.size() - 1);
        double max = modern.fitness() * modern.averageCapacityFactor();

        for(int i = epochs.epochs.size() - 1; i > epochs.epochs.size() - 6; i --){
            Epoch current = epochs.epochs.get(i);
            double currentFitness = current.fitness() * current.averageCapacityFactor();
            if (max < currentFitness){
                max = currentFitness;
                modern = current;
            }
        }
        return modern;
    }

    private Epoch findHistoricalEpoch(Epochs epochs) {
        for (Epoch epoch: epochs.epochs) {
            if (epoch.start_year >= 1451) {
                // Use this epoch as the historical epoch
                return epoch;
            }
        }
        return null;
    }

    private Epoch findDiseaseEpoch(Epochs epochs) {
        for (Epoch epoch: epochs.epochs) {
            if (epoch.disease()){
                return epoch;
            }
        }
        return null;
    }

    @Test public void testTuneRastrigin() throws IOException {
        tune(Function.Rastrigin, 10.0);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, 5, 25);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, 10, 30);
    }

    @Test public void  testTuneSchwefel226() throws IOException {
        tune(Function.Schwefel226, 20, 40);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, 10);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, 10, 30);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, 20, 25);
    }

    @Test public void testTuneAckleys() throws IOException {
        tune(Function.Ackleys, 4);
    }

    @Test public void testTuneAlpine() throws IOException {
        tune(Function.Alpine, 20, 40);
    }

    @Test public void testTuneBrown() throws IOException {
        tune(Function.Brown, 10, 25);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        tune(Function.ChungReynolds, 8, 40);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        tune(Function.DixonPrice, 8, 30);
    }

    @Test public void testTuneExponential() throws IOException {
        tune(Function.Exponential, 4, 20);
    }

    @Test public void testTuneGriewank() throws IOException {
        tune(Function.Griewank, 400);
    }

    @Test public void testTuneQing() throws IOException {
        tune(Function.Qing, 8, 25);
    }

    @Test public void testTuneSalomon() throws IOException {
        tune(Function.Salomon, 4);
    }

    @Test public void testTuneSchumerSteiglitz() throws IOException {
        tune(Function.SchumerSteiglitz, 8, 25);
    }

    @Test public void testTuneSchwefel220() throws IOException {
        tune(Function.Schwefel220, 4, 25);
    }

    @Test public void testTuneTrid() throws IOException {
        tune(Function.Trid, 5.0, 25);
    }

    @Test public void testTuneZakharoy() throws IOException {
        tune(Function.Zakharoy, 1.8, 10);
    }
}
