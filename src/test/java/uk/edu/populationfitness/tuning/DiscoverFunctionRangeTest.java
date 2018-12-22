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

    private static final int SizeOfGenes = 1000;

    private static final int PopulationSize = 100;

    private void DiscoverFunctionRange(Function function, boolean mustPass){
        GenesFactory factory = new GenesTimerFactory(new BitSetGenesFactory());
        factory.useFitnessFunction(function);
        // Given a number of randomly generated genes
        Config config = new Config();
        config.setNumberOfGenes(NumberOfGenes);
        config.setSizeOfEachGene(SizeOfGenes);
        config.setGenesFactory(factory);
        GenesTimer.resetAll();

        ArrayList<Genes> genes = new ArrayList<>();

        for(int i = 0; i < PopulationSize; i++){
            Genes next = factory.build(config);
            next.buildFromRandom();
            next.mutate();
            genes.add(next);
        }

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Genes g: genes) {
            double fitness = g.fitness();
            System.out.print(fitness);
            System.out.print(" ");

            if (fitness < min) min = fitness;
            if (fitness > max) max = fitness;
        }
        System.out.println(" ");

        System.out.print(function.toString());
        System.out.print("(");
        System.out.print(NumberOfGenes);
        System.out.print(") min=");
        System.out.print(min);
        System.out.print(" max=");
        System.out.println(max);

        GenesTimer.showAll();

        Assert.assertTrue("Min above zero", min >= -0.1);
        Assert.assertEquals("Usable range", mustPass, max - min >= 0.0001);
    }

    @Test public void testDiscoverRastrigin() {
        DiscoverFunctionRange(Function.Rastrigin, true);
    }

    @Test public void testDiscoverSphere() {
        DiscoverFunctionRange(Function.Sphere, true);
    }

    @Test public void testDiscoverStyblinksiTang() {
        DiscoverFunctionRange(Function.StyblinksiTang, true);
    }

    @Test public void testDiscoverSchwefel226() {
        DiscoverFunctionRange(Function.Schwefel226, true);
    }

    @Test public void testDiscoverRosenbrock() {
        DiscoverFunctionRange(Function.Rosenbrock, true);
    }

    @Test public void testDiscoverSumOfPowers() {
        DiscoverFunctionRange(Function.SumOfPowers, true);
    }

    @Test public void testDiscoverSumSquares() {
        DiscoverFunctionRange(Function.SumSquares, true);
    }

    @Test public void testDiscoverAckleys() {
        DiscoverFunctionRange(Function.Ackleys, true);
    }

    @Test public void testDiscoverAlpine() {
        DiscoverFunctionRange(Function.Alpine, true);
    }

    @Test public void testDiscoverBrown() {
        DiscoverFunctionRange(Function.Brown, true);
    }

    @Test public void testDiscoverChungReynolds() {
        DiscoverFunctionRange(Function.ChungReynolds, true);
    }

    @Test public void testDiscoverDixonPrice() {
        DiscoverFunctionRange(Function.DixonPrice, true);
    }

    @Test public void testDiscoverExponential() {
        DiscoverFunctionRange(Function.Exponential, true);
    }

    @Test public void testDiscoverGriewank() {
        DiscoverFunctionRange(Function.Griewank, true);
    }

    @Test public void testDiscoverQing() {
        DiscoverFunctionRange(Function.Qing, true);
    }

    @Test public void testDiscoverSalomon() {
        DiscoverFunctionRange(Function.Salomon, true);
    }

    @Test public void testDiscoverSchumerSteiglitz() {
        DiscoverFunctionRange(Function.SchumerSteiglitz, true);
    }

    @Test public void testDiscoverSchwefel220() {
        DiscoverFunctionRange(Function.Schwefel220, true);
    }

    @Test public void testDiscoverTrid() {
        DiscoverFunctionRange(Function.Trid, true);
    }

    @Test public void testDiscoverZakharoy() {
        DiscoverFunctionRange(Function.Zakharoy, true);
    }

    @Test public void testDiscoverFixedOne() {
        DiscoverFunctionRange(Function.FixedOne, false);
    }

    @Test public void testDiscoverFixedHalf() {
        DiscoverFunctionRange(Function.FixedHalf, false);
    }

    @Test public void testDiscoverFixedZero() {
        DiscoverFunctionRange(Function.FixedZero, false);
    }

    @Test public void testDiscoverRandom() {
        DiscoverFunctionRange(Function.Random, true);
    }

    @Test public void testDiscoverSinX() {
        DiscoverFunctionRange(Function.SinX, true);
    }
}
