package uk.edu.populationfitness.tuning;

import org.junit.Assert;
import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.GenesFactory;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;
import uk.edu.populationfitness.models.genes.performance.GenesTimer;
import uk.edu.populationfitness.models.genes.performance.GenesTimerFactory;

import java.util.ArrayList;

public class DiscoverFunctionRangeTest {

    private static final int NumberOfGenes = 20000;

    private static final int SizeOfGenes = 2250;

    private static final int PopulationSize = 10;

    private void DiscoverFunctionRange(Function function, int numberOfGenes, int sizeOfGenes, FitnessRange range, int populationSize){
        GenesFactory factory = new GenesTimerFactory(new BitSetGenesFactory());
        factory.useFitnessFunction(function);
        // Given a number of randomly generated genes
        Config config = new Config();
        config.setNumberOfGenes(numberOfGenes);
        config.setSizeOfEachGene(sizeOfGenes);
        config.getRange().min(range.min()).max(range.max());
        config.setGenesFactory(factory);
        GenesTimer.resetAll();

        ArrayList<Genes> genes = new ArrayList<>();

        Genes empty = factory.build(config);
        empty.buildEmpty();
        genes.add(empty);

        Genes full = factory.build(config);
        full.buildFull();
        genes.add(full);

        for(int i = 0; i < populationSize; i++){
            Genes next = factory.build(config);
            next.buildFromRandom();
            next.mutate();
            genes.add(next);
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Genes g: genes) {
            double fitness = g.fitness(1.0);

            if (fitness < min) min = fitness;
            if (fitness > max) max = fitness;
        }

        System.out.print(function.toString());
        System.out.print("(");
        System.out.print(numberOfGenes);
        System.out.print(") min=");
        System.out.print(min);
        System.out.print(" max=");
        System.out.println(max);

        GenesTimer.showAll();

        Assert.assertTrue("Min above zero", min >= -0.1);
        Assert.assertTrue("Usable range", max - min >= 0.01);
    }

    @Test public void testDiscoverRastrigin() {
        DiscoverFunctionRange(Function.Rastrigin, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSphere() {
        DiscoverFunctionRange(Function.Sphere, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverStyblinksiTang() {
        DiscoverFunctionRange(Function.StyblinksiTang, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSchwefel226() {
        DiscoverFunctionRange(Function.Schwefel226, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverRosenbrock() {
        DiscoverFunctionRange(Function.Rosenbrock, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSumOfPowers() {
        DiscoverFunctionRange(Function.SumOfPowers, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSumSquares() {
        DiscoverFunctionRange(Function.SumSquares, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverAckleys() {
        DiscoverFunctionRange(Function.Ackleys, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverAlpine() {
        DiscoverFunctionRange(Function.Alpine, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverBrown() {
        DiscoverFunctionRange(Function.Brown, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverChungReynolds() {
        DiscoverFunctionRange(Function.ChungReynolds, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverDixonPrice() {
        DiscoverFunctionRange(Function.DixonPrice, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverExponential() {
        DiscoverFunctionRange(Function.Exponential, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverGriewank() {
        DiscoverFunctionRange(Function.Griewank, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverQing() {
        DiscoverFunctionRange(Function.Qing, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSalomon() {
        DiscoverFunctionRange(Function.Salomon, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSchumerSteiglitz() {
        DiscoverFunctionRange(Function.SchumerSteiglitz, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverSchwefel220() {
        DiscoverFunctionRange(Function.Schwefel220, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverTrid() {
        DiscoverFunctionRange(Function.Trid, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }

    @Test public void testDiscoverZakharoy() {
        DiscoverFunctionRange(Function.Zakharoy, NumberOfGenes, SizeOfGenes, new FitnessRange(), PopulationSize);
    }
}
