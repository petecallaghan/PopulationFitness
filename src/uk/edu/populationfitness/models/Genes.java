package uk.edu.populationfitness.models;

import java.util.BitSet;
import java.util.Random;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Genes {
    private Config config;

    private BitSet genes;

    private Random random = new Random();

    private static final double HALF_PROBABILITY = 0.5;

    public Genes(Config config){
        this.config = config;
    }

    public void buildEmpty(){
        genes = new BitSet(config.number_of_genes * config.size_of_each_gene);
        genes.clear();
    }

    public int getCode(int index){
        return genes.get(index) ? 1 : 0;
    }

    public void setCode(int index, int value){
        genes.set(index, value == 0 ? false : true);
    }

    public void buildFromRandom(){
        buildEmpty();

        for(int i = 0; i < genes.size(); i++){
            if (random.nextDouble() > HALF_PROBABILITY){
                genes.flip(i);
            }
        }
    }

    public int numberOfBits(){
        return genes.size();
    }

    public void mutate(){
        for(int i = 0; i < genes.size(); i++){
            if (random.nextDouble() < config.mutation_probability){
                genes.flip(i);
            }
        }
    }

    public void inheritFrom(Genes mother, Genes father) {
        // Randomly picks the code index that crosses over from mother to father
        int length = mother.genes.size();

        int cross_over_index = 1 + (int)(random.nextDouble() * (length - 1));

        // Minimise the bit-wise copying
        if (cross_over_index > length / 2){
            // Clone mother then copy from father
            genes = (BitSet)mother.genes.clone();

            for (int i = cross_over_index; i < genes.length(); i++){
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
    }

    public boolean areEmpty(){
        return genes.isEmpty();
    }

    public double fitness(double fitness_factor){
        double ratio = (config.float_upper - config.float_lower) / Long.MAX_VALUE;

        long[] integer_values = genes.toLongArray();

        double fitness = 1.0;

        for(int i = 0; i < integer_values.length; i++){
            fitness *= Math.pow(Math.sin(config.float_lower + ratio * integer_values[i]), fitness_factor);
        }

        return Math.max(0.0, fitness);
    }

    public boolean isEqual(Genes other){
        return genes.equals(other.genes);
    }
}
