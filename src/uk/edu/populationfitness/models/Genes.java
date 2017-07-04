package uk.edu.populationfitness.models;

import java.util.BitSet;
import java.util.SplittableRandom;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Genes {
    private Config config;

    private BitSet genes;

    private SplittableRandom random = new SplittableRandom();

    private final double configured_fitness_ratio;

    private static final double HALF_PROBABILITY = 0.5;

    private double stored_fitness = 0;

    private double stored_fitness_factor = 0;

    public Genes(Config config){
        this.config = config;
        this.configured_fitness_ratio = (config.float_upper - config.float_lower) / Long.MAX_VALUE;
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
        stored_fitness_factor = 0;
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
        stored_fitness_factor = 0;
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
        mutate();
    }

    public boolean areEmpty(){
        return genes.isEmpty();
    }

    public double fitness(double fitness_factor){
        if (abs(fitness_factor - stored_fitness_factor) < 0.01) {
            return stored_fitness;
        }
        // We need to calculate the fitness again
        stored_fitness_factor = fitness_factor;

        double fitness = 1.0;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            fitness *= Math.pow(Math.sin(config.float_lower + configured_fitness_ratio * integer_values[i]), fitness_factor);
        }

        stored_fitness = Math.max(0.0, fitness);
        return stored_fitness;
    }

    public boolean isEqual(Genes other){
        return genes.equals(other.genes);
    }
}
