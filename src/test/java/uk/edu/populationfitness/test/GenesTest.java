package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenes;
import uk.edu.populationfitness.models.genes.sinpi.SinPiOver2Genes;

import static org.junit.Assert.*;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class GenesTest {
    @Test public void testGenesAreEmpty(){
        // Given a set of genes with zero value
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildEmpty();

        // When tested they are all empty
        assertTrue(genes.areEmpty());
    }

    @Test public void testMutatedGenesAreNotAllZero(){
        // Given a set of genes that are empty and a high probability that they will mutate
        Config config = new Config();
        config.setMutationsPerGene(100);
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildEmpty();

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
        assertTrue(mutated_count <= 2.5 * config.getMutationsPerGene());
    }

    @Test public void testRandomGenesAreNotAllZero(){
        // Given a set of genes that are random
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildFromRandom();

        // Then the number of bits set falls inside the probability range
        int set_count = 0;
        for (int i = 0; i < genes.numberOfBits(); i++){
            if (genes.getCode(i) == 1){
                set_count++;
            }
        }

        assertTrue(set_count > 0.25 * genes.numberOfBits());
        assertTrue(set_count < 0.75 * genes.numberOfBits());
    }

    @Test public void testGenesAsIntegers(){
        // Given a set of genes that are random
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildFromRandom();

        // Then the genes as integers are non zero
        long[] integers = genes.asIntegers();
        assertTrue(integers.length >= 1);
        for (long integer : integers) {
            assertTrue(integer > 0);
        }
    }

    @Test public void testMutationCanBeDisabled(){
        // Given a set of genes with zero values that will not mutate
        Config config = new Config();
        config.setMutationsPerGene(0);
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildEmpty();

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
        double fitness = genes.fitness();
        assertTrue(0.0 <= fitness);
        assertTrue( 1.0 >= fitness);
    }

    @Test public void testGenesAsFloatUseDefaultRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void testGenesWithLargeBitCoding(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.setNumberOfGenes(10000);
        config.setSizeOfEachGene(2250);
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void testGenesWithConfiguredFloatRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.setFloatLower(1.5);
        config.setFloatUpper(10.5);
        BitSetGenes genes = new SinPiOver2Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    private BitSetGenes createFatherDifferentFromMother(Config config, BitSetGenes mother){
        BitSetGenes father = new SinPiOver2Genes(config);
        father.buildFromRandom();

        while(father.isEqual(mother)){
            father.mutate();
        }
        return father;
    }

    @Test public void testBabyIsNotIdenticalToMotherOrFather() {
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerGene(config.getMutationsPerGene() * 100000);
        BitSetGenes mother = new SinPiOver2Genes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2Genes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are different to both
        assertFalse(baby.isEqual(mother));
        assertFalse(baby.isEqual(father));
    }

    @Test public void testBabyIsNotZero(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerGene(config.getMutationsPerGene() * 10);
        BitSetGenes mother = new SinPiOver2Genes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2Genes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are non zero
        assertFalse(baby.areEmpty());
    }

    @Test public void testBabyIsSimilarToMotherAndFather(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerGene(config.getMutationsPerGene() * 10);
        BitSetGenes mother = new SinPiOver2Genes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new SinPiOver2Genes(config);

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
}
