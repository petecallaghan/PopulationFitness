package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import org.junit.Test;
import uk.edu.populationfitness.Tuning;
import uk.edu.populationfitness.UkPopulationEpochs;
import uk.edu.populationfitness.models.*;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.performance.GenesTimer;
import uk.edu.populationfitness.models.genes.performance.GenesTimerFactory;
import uk.edu.populationfitness.output.EpochsWriter;
import uk.edu.populationfitness.output.TuningWriter;
import uk.edu.populationfitness.simulation.SimulationThread;

import java.io.IOException;
import java.util.stream.Collectors;

public class TuneFunctionsTest {
    private static final int RepresentativeRealNumberOfGenes = 20000;

    private static final int RepresentativeRealSizeOfGenes = 1000;

    private static final double RealMutationsPerIndividual = 150.0;

    private static final int NumberOfGenes = 10;

    private static final int SizeOfGenes = 100;

    private static final int PopulationRatio = 1; 

    private static final String EpochsPath = "epochs";

    private static final String TuningPath = "tuning";

    private static final int TuningPercentage = 15;

    private static final double MutationsPerIndividual = (NumberOfGenes * SizeOfGenes * RealMutationsPerIndividual)
            / (RepresentativeRealNumberOfGenes * RepresentativeRealSizeOfGenes);

    private void tune(Function function, double maxFactor) throws IOException {
        tune(function, maxFactor, TuningPercentage, true);
    }

    private void tune(Function function, double maxFactor, int tuningPercentage) throws IOException {
        tune(function, maxFactor, tuningPercentage, true);
    }

    private void tune(Function function, double maxFactor, int tuningPercentage, boolean willPass) throws IOException {
        RepeatableRandom.resetSeed();

        final Config config = buildTimedTuningConfig(function);
        final Epochs epochs = UkPopulationEpochs.define(config);
        final PopulationComparison result = tuneEpochFitnessFactors(maxFactor, tuningPercentage, config, epochs);

        final Tuning tuning = createTuningFromEpochs(config, epochs);

        if (willPass){
            checkTuningMeetsHypothesis(epochs, config, tuning);
        }

        showResults(tuning);

        writeResults(function, config, epochs, tuning);

        assertTuned(result, tuning, epochs, willPass);
    }

    private PopulationComparison tuneEpochFitnessFactors(double maxFactor, int tuningPercentage, Config config, Epochs epochs) {
        final Generations generations = new Generations(new Population(config));

        epochs.reducePopulation(PopulationRatio);
        config.setInitialPopulation(epochs.first().environment_capacity);
        config.setMutationsPerGene(MutationsPerIndividual);

        return generations.tuneFitnessFactorsForAllEpochs(epochs, 0.0, maxFactor, 0.000001, tuningPercentage);
    }

    private void checkTuningMeetsHypothesis(Epochs epochs, Config config, Tuning tuning){
        SimulationThread simulation = new SimulationThread(config, epochs, tuning, 1);
        simulation.run();

        final FitnessStatistics historical = new FitnessStatistics(simulation.generations.history.stream()
                .filter(s -> !s.epoch.modern()).collect(Collectors.toList()));
        final FitnessStatistics modern = new FitnessStatistics(simulation.generations.history.stream()
                .filter(s -> s.epoch.modern()).collect(Collectors.toList()));

        historical.print("Historical");
        modern.print("Modern");

        Assert.assertTrue(historical.fitnessesTrend > modern.fitnessesTrend);
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

    private void assertTuned(PopulationComparison result, Tuning tuning, Epochs epochs, boolean willPass) {
        if (willPass){
            // Ensure that we successfully tuned
            Assert.assertTrue(result == PopulationComparison.WithinRange);

            // Ensure that the tuning result is what we expect
            Assert.assertTrue(tuning.disease_fit <= tuning.modern_fit);
            Assert.assertTrue(tuning.historic_fit <= tuning.modern_fit);
            Assert.assertTrue(tuning.disease_fit <= tuning.historic_fit);
            //Assert.assertTrue(tuning.historic_fit < findMinModernFitness(epochs));
        }
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

        tuning.historic_fit = findHistoricalFitness(epochs);
        tuning.modern_fit = findAvgModernFitness(epochs);
        tuning.modern_breeding = epochs.last().breedingProbability();
        tuning.disease_fit = findDiseaseFitness(epochs);

        return tuning;
    }

    private double findAvgModernFitness(Epochs epochs) {
        return epochs.epochs.stream().filter(e -> e.modern()).mapToDouble(e -> e.fitness()).average().getAsDouble();
    }

    private double findMinModernFitness(Epochs epochs) {
        return epochs.epochs.stream().filter(e -> e.modern()).mapToDouble(e -> e.fitness()).min().getAsDouble();
    }

    private double findHistoricalFitness(Epochs epochs) {
        return epochs.epochs.stream().filter(e -> !e.modern() && !e.disease() && e.start_year > 1000).mapToDouble(e -> e.fitness()).average().getAsDouble();
    }

    private double findDiseaseFitness(Epochs epochs) {
        return epochs.epochs.stream().filter(e -> e.disease()).mapToDouble(e -> e.fitness()).min().getAsDouble();
    }

    @Test public void testTuneRastrigin() throws IOException {
        tune(Function.Rastrigin, 10.0, 20);
    }

    @Test public void testTuneSphere() throws IOException {
        tune(Function.Sphere, 5, 15);
    }

    @Test public void testTuneStyblinksiTang() throws IOException {
        tune(Function.StyblinksiTang, 10, 15);
    }

    @Test public void  testTuneSchwefel226() throws IOException {
        tune(Function.Schwefel226, 20, 15);
    }

    @Test public void testTuneRosenbrock() throws IOException {
        tune(Function.Rosenbrock, 10, 15);
    }

    @Test public void testTuneSumOfPowers() throws IOException {
        tune(Function.SumOfPowers, 10, 15);
    }

    @Test public void testTuneSumSquares() throws IOException {
        tune(Function.SumSquares, 4, 20);
    }

    @Test public void testTuneAckleys() throws IOException {
        tune(Function.Ackleys, 3, 15);
    }

    @Test public void testTuneAlpine() throws IOException {
        tune(Function.Alpine, 10, 15); //, 30);
    }

    @Test public void testTuneBrown() throws IOException {
        tune(Function.Brown, 5, 15);
    }

    @Test public void testTuneChungReynolds() throws IOException {
        tune(Function.ChungReynolds, 10, 15);
    }

    @Test public void testTuneDixonPrice() throws IOException {
        tune(Function.DixonPrice, 8, 15);
    }

    @Test public void testTuneExponential() throws IOException {
        tune(Function.Exponential, 4, 15);
    }

    @Test public void testTuneGriewank() throws IOException {
        tune(Function.Griewank, 7, 15);
    }

    @Test public void testTuneQing() throws IOException {
        tune(Function.Qing, 8, 15);
    }

    @Test public void testTuneSalomon() throws IOException {
        tune(Function.Salomon, 1, 15);
    }

    @Test public void testTuneSchumerSteiglitz() throws IOException {
        tune(Function.SchumerSteiglitz, 8, 15);
    }

    @Test public void testTuneSchwefel220() throws IOException {
        tune(Function.Schwefel220, 4, 15);
    }

    @Test public void testTuneTrid() throws IOException {
        tune(Function.Trid, 11.0, 15);
    }

    @Test public void testTuneZakharoy() throws IOException {
        tune(Function.Zakharoy, 100.0, 15);
    }

    @Test public void testTuneFixedOne() throws IOException {
        tune(Function.FixedOne, 1.0, 20, false);
    }

    @Test public void testTuneFixedHalf() throws IOException {
        tune(Function.FixedHalf, 2.0, 20, false);
    }

    @Test public void testTuneFixedZero() throws IOException {
        tune(Function.FixedZero, 2.0, 20, false);
    }

    @Test public void testTuneRandom() throws IOException {
        tune(Function.Random, 10.0, 20, false);
    }

    @Test public void testTuneSinX() throws IOException {
        tune(Function.SinX, 1.5, 15);
    }
}
