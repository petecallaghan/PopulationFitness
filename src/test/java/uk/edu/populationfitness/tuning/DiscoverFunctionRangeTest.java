package uk.edu.populationfitness.tuning;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;
import uk.edu.populationfitness.models.genes.fitness.Statistics;

import java.util.ArrayList;

public class DiscoverFunctionRangeTest {

    private void DiscoverFunctionRange(Function function, int numberOfGenes, int sizeOfGenes, FitnessRange range, int populationSize){
        BitSetGenesFactory factory = new BitSetGenesFactory();
        factory.function = function;
        // Given a number of randomly generated genes
        Config config = new Config();
        config.number_of_genes = numberOfGenes;
        config.size_of_each_gene = sizeOfGenes;
        config.range.min(range.min()).max(range.max()).statistics(new Statistics());

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
        config.range.statistics().show();
    }

    @Test
    public void testDiscoverSinPiOver210() {
        DiscoverFunctionRange(Function.SinPiOver2, 10, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSinPiOver2100() {
        DiscoverFunctionRange(Function.SinPiOver2, 100, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSinPiOver21000() {
        DiscoverFunctionRange(Function.SinPiOver2, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSinPiLinear10() {
        DiscoverFunctionRange(Function.SinPiLinear, 10, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSinPiLinear100() {
        DiscoverFunctionRange(Function.SinPiLinear, 100, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSinPiLinear1000() {
        DiscoverFunctionRange(Function.SinPiLinear, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverRastrigin10() {
        DiscoverFunctionRange(Function.Rastrigin, 10, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverRastrigin20000() {
        DiscoverFunctionRange(Function.Rastrigin, 20000, 2250, new FitnessRange(), 10);
    }

    @Test public void testDiscoverRastrigin100() {
        DiscoverFunctionRange(Function.Rastrigin, 100, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverRastrigin1000() {
        DiscoverFunctionRange(Function.Rastrigin, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSphere10() {
        DiscoverFunctionRange(Function.Sphere, 10, 10, new FitnessRange().min(0).max(27), 4000);
    }

    @Test public void testDiscoverSphere100() {
        DiscoverFunctionRange(Function.Sphere, 100, 10, new FitnessRange().min(0).max(279), 4000);
    }

    @Test public void testDiscoverSphere1000() {
        DiscoverFunctionRange(Function.Sphere, 1000, 10, new FitnessRange().min(0).max(1747), 4000);
    }

    @Test public void testDiscoverSphere10000() {
        DiscoverFunctionRange(Function.Sphere, 10000, 2250, new FitnessRange().min(0).max(15050), 10);
    }

    @Test public void testDiscoverSphere20000() {
        DiscoverFunctionRange(Function.Sphere, 20000, 2250, new FitnessRange().min(0).max(29363), 10);
    }

    @Test public void testDiscoverStyblinksiTang10() {
        DiscoverFunctionRange(Function.StyblinksiTang, 10, 10, new FitnessRange().min(-39.2).max(125), 4000);
    }

    @Test public void testDiscoverStyblinksiTang100() {
        DiscoverFunctionRange(Function.StyblinksiTang, 100, 10, new FitnessRange().min(-433).max(764), 4000);
    }

    @Test public void testDiscoverStyblinksiTang1000() {
        DiscoverFunctionRange(Function.StyblinksiTang, 1000, 10, new FitnessRange().min(-2087).max(1206), 4000);
    }

    @Test public void testDiscoverStyblinksiTang20000() {
        DiscoverFunctionRange(Function.StyblinksiTang, 20000, 2250, new FitnessRange().min(-20757).max(125), 10);
    }

    @Test public void testDiscoverSchwefel10() {
        DiscoverFunctionRange(Function.Schwefel, 10, 10, new FitnessRange().min(0).max(1257), 4000);
    }

    @Test public void testDiscoverSchwefel100() {
        DiscoverFunctionRange(Function.Schwefel, 100, 10, new FitnessRange().min(0).max(9641.1), 4000);
    }

    @Test public void testDiscoverSchwefel1000() {
        DiscoverFunctionRange(Function.Schwefel, 1000, 10, new FitnessRange().min(0).max(75960), 4000);
    }

    @Test public void testDiscoverRosenbrock100() {
        DiscoverFunctionRange(Function.Rosenbrock, 100, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverRosenbrock20000() {
        DiscoverFunctionRange(Function.Rosenbrock, 20000, 2250, new FitnessRange(), 10);
    }

    @Test public void testDiscoverRosenbrock1000() {
        DiscoverFunctionRange(Function.Rosenbrock, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSumOfPowers100() {
        DiscoverFunctionRange(Function.SumOfPowers, 100, 10, new FitnessRange().min(0).max(8), 4000);
    }

    @Test public void testDiscoverSumOfPowers1000() {
        DiscoverFunctionRange(Function.SumOfPowers, 1000, 10, new FitnessRange().min(0).max(12), 4000);
    }

    @Test public void testDiscoverSumOfPowers10000() {
        DiscoverFunctionRange(Function.SumOfPowers, 10000, 10, new FitnessRange().min(0).max(18), 4000);
    }

    @Test public void testDiscoverSumOfPowers100000() {
        DiscoverFunctionRange(Function.SumOfPowers, 100000, 10, new FitnessRange().min(0).max(20.4), 400);
    }

    @Test public void testDiscoverSumSquares100() {
        DiscoverFunctionRange(Function.SumSquares, 100, 10, new FitnessRange().min(0).max(2139.4), 4000);
    }

    @Test public void testDiscoverSumSquares1000() {
        DiscoverFunctionRange(Function.SumSquares, 1000, 10, new FitnessRange().min(0).max(144056), 4000);
    }

    @Test public void testDiscoverAckleys() {
        DiscoverFunctionRange(Function.Ackleys, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverAlpine() {
        DiscoverFunctionRange(Function.Alpine, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverBrown() {
        DiscoverFunctionRange(Function.Brown, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverChungReynolds() {
        DiscoverFunctionRange(Function.ChungReynolds, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverDixonPrice() {
        DiscoverFunctionRange(Function.DixonPrice, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverExponential() {
        DiscoverFunctionRange(Function.Exponential, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverGriewank() {
        DiscoverFunctionRange(Function.Griewank, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverQing() {
        DiscoverFunctionRange(Function.Qing, 1000, 10, new FitnessRange(), 4000);
    }

    @Test public void testDiscoverSalomon() {
        DiscoverFunctionRange(Function.Salomon, 1000, 10, new FitnessRange(), 4000);
    }
}
