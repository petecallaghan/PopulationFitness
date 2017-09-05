package uk.edu.populationfitness.test;

import org.junit.Assert;
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
        Config config = new Config();
        config.genesFactory.function = function;
        config.number_of_genes = numberOfGenes;
        config.range.min(range.min()).max(range.max());
        Population population = new Population(config);
        Generations generations = new Generations(population);
        Epochs epochs = new Epochs(config);

        final double historic_kill = 1.066;

        // https://en.wikipedia.org/wiki/Demography_of_England
        epochs.addNextEpoch(new Epoch(config, -50).max(config.initial_population).kill(historic_kill).capacity(config.initial_population));
        epochs.addNextEpoch(new Epoch(config, 400).max(1700).kill(historic_kill).capacity(1700));

        epochs.addNextEpoch(new Epoch(config, 1086).max(3100).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1190).max(3970).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1220).max(4230).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1250).max(4430).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1279).max(4750).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1290).max(4690).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1315).max(4120).kill(historic_kill)); // Great Famine of 1315–1317
        epochs.addNextEpoch(new Epoch(config, 1325).max(4810).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1348).max(2600).kill(historic_kill).disease(true)); // Black death
        epochs.addNextEpoch(new Epoch(config, 1351).max(2500).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1377).max(2080).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1400).max(2020).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1430).max(1900).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1450).max(2140).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1490).max(2350).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1522).max(2830).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1541).max(3200).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1560).max(4110).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1600).max(5310).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1650).max(5200).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1700).max(7755).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1801).max(8762).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1811).max(10402).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1821).max(12012).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1831).max(13659).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1841).max(15289).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1851).max(18325).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1861).max(21361).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1871).max(24297).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1881).max(27231).kill(historic_kill));
        epochs.addNextEpoch(new Epoch(config, 1891).max(30000).kill(historic_kill));
        epochs.setFinalEpochYear(1901);

        PopulationComparison result = generations.tuneFitnessFactorsForAllEpochs(epochs, minFactor, maxFactor, increment, percentage);

        epochs.printFitnessFactors();
        Assert.assertTrue(result == PopulationComparison.WithinRange);

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
        tune(Function.Rastrigin, new FitnessRange().min(0).max(10283), 100, 5, 20, 0.001, 10);
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
