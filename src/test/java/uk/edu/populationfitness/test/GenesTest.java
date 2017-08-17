package uk.edu.populationfitness.test;

import org.junit.Test;
import static org.junit.Assert.*;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.*;

import java.util.ArrayList;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class GenesTest {
    @Test public void TestGenesAreEmpty(){
        // Given a set of genes with zero value
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildEmpty();

        // When tested they are all empty
        assertTrue(genes.areEmpty());
    }

    @Test public void testMutatedGenesAreNotAllZero(){
        // Given a set of genes that are empty and a high probability that they will mutate
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildEmpty();
        config.mutation_probability *= 10;

        // When they are mutated
        genes.mutate();

        // Then the number of bits mutated falls inside the probability range
        int mutated_count = 0;
        for (int i = 0; i < genes.numberOfBits(); i++){
            if (genes.getCode(i) == 1){
                mutated_count++;
            }
        }

        assertTrue(mutated_count > 0);
        assertTrue(mutated_count <= 2.0 * genes.numberOfBits() * config.mutation_probability);
    }

    @Test public void testMutationCanBeDisabled(){
        // Given a set of genes with zero values that will not mutate
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildEmpty();
        config.mutation_probability = 0;

        // When they are mutated
        genes.mutate();

        // Then none have changed
        int mutated_count = 0;
        for (int i = 0; i < genes.numberOfBits(); i++){
            if (genes.getCode(i) == 1){
                mutated_count++;
            }
        }

        assertEquals(0, mutated_count);
    }

    private void thenTheyFallIntoTheFloatRange(BitSetGenes genes) {
        // Then they fall into the float range
        double fitness = genes.fitness(1.0);
        assertTrue(0.0 <= fitness);
        assertTrue( 1.0 >= fitness);
    }

    @Test public void testGenesAsFloatUseDefaultRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void TestGenesWithLargeBitCoding(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.number_of_genes = 111;
        config.size_of_each_gene = 131;
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void TestGenesWithConfiguredFloatRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.float_lower = 1.5;
        config.float_upper = 10.5;
        BitSetGenes genes = new SinPiOver2BitSetGenes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    private BitSetGenes createFatherDifferentFromMother(Config config, BitSetGenes mother){
        BitSetGenes father = new SinPiOver2BitSetGenes(config);
        father.buildFromRandom();

        while(father.isEqual(mother)){
            father.mutate();
        }
        return father;
    }

    @Test public void testBabyIsNotIdenticalToMotherOrFather() {
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.mutation_probability *= 10;
        BitSetGenes mother = new SinPiOver2BitSetGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2BitSetGenes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are different to both
        assertFalse(baby.isEqual(mother));
        assertFalse(baby.isEqual(father));
    }

    @Test public void testBabyIsNotZero(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.mutation_probability *= 10;
        BitSetGenes mother = new SinPiOver2BitSetGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2BitSetGenes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are non zero
        assertFalse(baby.areEmpty());
    }

    @Test public void testBabyIsSimilarToMotherAndFather(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.mutation_probability *= 10;
        BitSetGenes mother = new SinPiOver2BitSetGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2BitSetGenes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        boolean similar_to_father = false;
        boolean similar_to_mother = false;
        // Then the baby's genes have some similarity to both
        for(int i = 0; i < baby.numberOfBits(); i++){
            if (baby.getCode(i) == mother.getCode(i)){
                similar_to_mother = true;
            }
            if (baby.getCode(i) == father.getCode(i)){
                similar_to_father = true;
            }
        }
        assertTrue(similar_to_mother);
        assertTrue(similar_to_father);
    }

    private void GenesAreDistributedWithoutExcessiveSpikes(Function function, double fitness_factor){
        BitSetGenesFactory factory = new BitSetGenesFactory();
        factory.function = function;
        // Given a number of randomly generated genes
        Config config = new Config();
        config.number_of_genes = 100;
        ArrayList<Genes> genes = new ArrayList<>();
        for(int i = 0; i < 40000; i++){
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
            double fitness = g.fitness(fitness_factor);
            int i = Math.min(99, (int)(fitness * 100));
            fitnesses[i]++;
        }

        // Then the gene fitness is distributed without excessive spikes
        System.out.println(function.toString());
        for(int f: fitnesses){
            System.out.println(f);
            assertTrue(f < 20 * (genes.size() / fitnesses.length));
        }
    }

    @Test public void testGenesAreDistributedWithoutExcessiveSpikes(){
        GenesAreDistributedWithoutExcessiveSpikes(Function.SinPi, 1.0);
        GenesAreDistributedWithoutExcessiveSpikes(Function.SinPiAvg, 1.0);
        GenesAreDistributedWithoutExcessiveSpikes(Function.Rastrigin, 0.7);
        GenesAreDistributedWithoutExcessiveSpikes(Function.Rastrigin, -0.7);
    }
}
