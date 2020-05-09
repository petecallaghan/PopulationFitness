package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.Function;
import uk.edu.populationfitness.models.genes.Genes;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class GenesDistributionTest {

    private static final int Population = 40000;

    private static final int NumberOfGenes = 10;

    private static final int SizeOfGenes = 10;

    private void GenesAreDistributedWithoutExcessiveSpikes(Function function, double fitness_factor){
        BitSetGenesFactory factory = new BitSetGenesFactory();
        factory.useFitnessFunction(function);
        // Given a number of randomly generated genes
        Config config = new Config();
        config.setNumberOfGenes(NumberOfGenes);
        config.setSizeOfEachGene(SizeOfGenes);
        config.setMutationsPerIndividual(1.0);
        ArrayList<Genes> genes = new ArrayList<>();
        for(int i = 0; i < Population; i++){
            Genes next = factory.build(config);
            next.buildFromRandom();
            genes.add(next);
        }

        // When the fitnesses are counted into a distribution
        int[] fitnesses = new int[100];
        for(int i = 0; i < fitnesses.length; i++){
            fitnesses[i] = 0;
        }

        for (Genes g: genes) {
            double fitness = g.fitness() * fitness_factor;
            int i = Math.abs(Math.min(99, (int)(fitness * 100)));
            fitnesses[i]++;
        }

        // Then the gene fitness is distributed without excessive spikes
        System.out.println(function.toString());
        for(int f: fitnesses){
            System.out.println(f);
            assertTrue(f < 20 * (genes.size() / fitnesses.length));
        }
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSchwefel220(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Schwefel220, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSchumerSteiglitz(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.SchumerSteiglitz, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesQing(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Qing, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesGriewank(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Griewank, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesExponential(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Exponential, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesDixonPrice(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.DixonPrice, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesChungReynolds(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.ChungReynolds, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesBrown(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Brown, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesAlpine(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Alpine, 5);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesAckleys(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Ackleys, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSumOfPowers(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.SumOfPowers, 0.05);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesRastrigin(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Rastrigin, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesStyblinksiTang(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.StyblinksiTang, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesRosenbrock(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Rosenbrock, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSchwefel226(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Schwefel226, 0.01);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSphere(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.Sphere, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSumSquares(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.SumSquares, 1.0);
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikesSinX(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.SinX, 1.0);
    }
}
