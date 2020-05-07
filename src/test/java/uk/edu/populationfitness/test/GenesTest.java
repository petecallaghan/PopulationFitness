package uk.edu.populationfitness.test;

import org.junit.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenes;
import uk.edu.populationfitness.models.genes.localmimina.AckleysGenes;

import static org.junit.Assert.*;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class GenesTest {
    @Test public void testGenesAreEmpty(){
        // Given a set of genes with zero value
        Config config = new Config();
        BitSetGenes genes = new AckleysGenes(config);
        genes.buildEmpty();

        // When tested they are all empty
        assertTrue(genes.areEmpty());
    }

    private void testTheRightNumberOfMutatedGenes(int numGenes, int geneSize, int mutations){
        // Given a set of genes that are empty and a high probability that they will mutate
        Config config = new Config();
        config.setNumberOfGenes(numGenes);
        config.setSizeOfEachGene(geneSize);
        config.setMutationsPerIndividual(mutations);
        BitSetGenes genes = new AckleysGenes(config);
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
        assertTrue(mutated_count >= 0.3 * config.getMutationsPerIndividual());
        assertTrue(mutated_count <= 2 * config.getMutationsPerIndividual());
    }


    @Test public void testMutatedGenesAreNotAllZero(){
        testTheRightNumberOfMutatedGenes(1000, 20, 1);
        testTheRightNumberOfMutatedGenes(20, 1000, 1);
        testTheRightNumberOfMutatedGenes(1000, 20, 20);
        testTheRightNumberOfMutatedGenes(20, 1000, 20);
        testTheRightNumberOfMutatedGenes(1000, 20, 20000);
        testTheRightNumberOfMutatedGenes(20, 1000, 20000);
    }

    @Test public void testRandomGenesAreNotAllZero(){
        // Given a set of genes that are random
        Config config = new Config();
        BitSetGenes genes = new AckleysGenes(config);
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

    @Test public void testGenesAsDoubles(){
        // Given a set of genes that are random
        Config config = new Config();
        BitSetGenes genes = new AckleysGenes(config);
        genes.buildFromRandom();

        // Then the genes as integers are non zero
        double[] unknowns = genes.asDoubles();
        assertTrue(unknowns.length >= 1);
        for (double x : unknowns) {
            assertTrue(x > 0);
        }
    }

    @Test public void testMutationCanBeDisabled(){
        // Given a set of genes with zero values that will not mutate
        Config config = new Config();
        config.setMutationsPerIndividual(0);
        BitSetGenes genes = new AckleysGenes(config);
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
        BitSetGenes genes = new AckleysGenes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void testGenesWithLargeBitCoding(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.setNumberOfGenes(10000);
        config.setSizeOfEachGene(2250);
        BitSetGenes genes = new AckleysGenes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    private BitSetGenes createFatherDifferentFromMother(Config config, BitSetGenes mother){
        BitSetGenes father = new AckleysGenes(config);
        father.buildFromRandom();

        while(father.isEqual(mother)){
            father.mutate();
        }
        return father;
    }

    @Test public void testBabyIsNotIdenticalToMotherOrFather() {
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerIndividual(1);
        BitSetGenes mother = new AckleysGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new AckleysGenes(config);

        // When the baby inherits from the mother and father
        final int mutated = baby.inheritFrom(mother, father);

        // Then the baby's genes are different to both
        assertFalse(baby.isEqual(mother));
        assertFalse(baby.isEqual(father));
        assertNotEquals(0, mutated);
    }

    @Test public void testBabyIsNotZero(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerIndividual(config.getMutationsPerIndividual() * 10);
        BitSetGenes mother = new AckleysGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new AckleysGenes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are non zero
        assertFalse(baby.areEmpty());
    }

    @Test public void testBabyIsSimilarToMotherAndFather(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.setMutationsPerIndividual(config.getMutationsPerIndividual() * 10);
        BitSetGenes mother = new AckleysGenes(config);
        mother.buildFromRandom();
        BitSetGenes father = createFatherDifferentFromMother(config, mother);
        BitSetGenes baby = new AckleysGenes(config);

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
