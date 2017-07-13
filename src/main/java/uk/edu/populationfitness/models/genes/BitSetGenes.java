package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.RepeatableRandom;

import java.util.BitSet;

import static java.lang.Math.abs;

/**
 * Extend this to provide different fitness functions that use a bit set
 *
 * Created by pete.callaghan on 03/07/2017.
 */
public abstract class BitSetGenes implements Genes {
    protected Config config;

    protected BitSet genes;

    protected static final double HALF_PROBABILITY = 0.5;

    protected double stored_fitness = 0;

    protected double stored_fitness_factor = 0;

    protected final int size_of_genes;

    public BitSetGenes(Config config){
        this.config = config;
        size_of_genes = config.number_of_genes * config.size_of_each_gene;
    }

    public void buildEmpty(){
        genes = new BitSet(size_of_genes);
        genes.clear();
    }

    public int getCode(int index){
        return genes.get(index) ? 1 : 0;
    }

    public void buildFromRandom(){
        buildEmpty();

        for(int i = 0; i < size_of_genes; i++){
            if (RepeatableRandom.generateNext() < HALF_PROBABILITY){
                genes.flip(i);
            }
        }
        stored_fitness_factor = 0;
    }

    public int numberOfBits(){
        return size_of_genes;
    }

    public void mutate(){
        for(int i = 0; i < size_of_genes; i++){
            if (RepeatableRandom.generateNext() < config.mutation_probability){
                genes.flip(i);
            }
        }
        stored_fitness_factor = 0;
    }

    private void inherit(BitSetGenes mother, BitSetGenes father){
        // Randomly picks the code index that crosses over from mother to father
        int cross_over_index = 1 + (int)(RepeatableRandom.generateNext() * (size_of_genes - 1));

        // Minimise the bit-wise copying
        if (cross_over_index > size_of_genes / 2){
            // Clone mother then copy from father
            genes = (BitSet)mother.genes.clone();

            for (int i = cross_over_index; i < size_of_genes; i++){
                genes.set(i, father.genes.get(i));
            }
        }
        else{
            // Clone father then copy from mother
            genes = (BitSet)father.genes.clone();

            for (int i = 0 ; i < cross_over_index; i++){
                genes.set(i, mother.genes.get(i));
            }
        }
        mutate();
    }

    public void inheritFrom(Genes mother, Genes father) {
        inherit((BitSetGenes)mother, (BitSetGenes)father);
    }

    public boolean areEmpty(){
        return genes.isEmpty();
    }

    public boolean isEqual(Genes other){
        return genes.equals(((BitSetGenes)other).genes);
    }
}
