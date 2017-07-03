package uk.edu.populationfitness.test;

import org.junit.jupiter.api.Test;
import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.Genes;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class GenesTest {
    @Test public void TestGenesAreEmpty(){
        // Given a set of genes with zero value
        Config config = new Config();
        Genes genes = new Genes(config);
        genes.buildEmpty();

        // When tested they are all empty
        assertTrue(genes.areEmpty());
    }

    @Test public void testMutatedGenesAreNotAllZero(){
        // Given a set of genes that are empty and a high probability that they will mutate
        Config config = new Config();
        Genes genes = new Genes(config);
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
        Genes genes = new Genes(config);
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

    private void thenTheyFallIntoTheFloatRange(Genes genes) {
        // Then they fall into the float range
        double fitness = genes.fitness(1.0);
        assertTrue(0.0 <= fitness);
        assertTrue( 1.0 >= fitness);
    }

    @Test public void testGenesAsFloatUseDefaultRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        Genes genes = new Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void TestGenesWithLargeBitCoding(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.number_of_genes = 111;
        config.size_of_each_gene = 131;
        Genes genes = new Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    @Test public void TestGenesWithConfiguredFloatRange(){
        // Given a set of genes with non zero values
        Config config = new Config();
        config.float_lower = 1.5;
        config.float_upper = 10.5;
        Genes genes = new Genes(config);
        genes.buildFromRandom();

        thenTheyFallIntoTheFloatRange(genes);
    }

    private Genes createFatherDifferentFromMother(Config config, Genes mother){
        Genes father = new Genes(config);
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
        Genes mother = new Genes(config);
        mother.buildFromRandom();
        Genes father = createFatherDifferentFromMother(config, mother);
        Genes baby = new Genes(config);

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
        Genes mother = new Genes(config);
        mother.buildFromRandom();
        Genes father = createFatherDifferentFromMother(config, mother);
        Genes baby = new Genes(config);

        // When the baby inherits from the mother and father
        baby.inheritFrom(mother, father);

        // Then the baby's genes are non zero
        assertFalse(baby.areEmpty());
    }

    @Test public void testBabyIsSimilarToMotherAndFather(){
        // Given a mother with some mutated genes, a father with some mutated genes and a baby
        Config config = new Config();
        config.mutation_probability *= 10;
        Genes mother = new Genes(config);
        mother.buildFromRandom();
        Genes father = createFatherDifferentFromMother(config, mother);
        Genes baby = new Genes(config);

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
