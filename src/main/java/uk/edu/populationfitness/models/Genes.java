package uk.edu.populationfitness.models;

import java.util.BitSet;

import static java.lang.Math.abs;

/**
 * todo Create a suite of fitness functions to test different populations
 * Created by pete.callaghan on 03/07/2017.
 */
public class Genes {
    private Config config;

    private BitSet genes;

    private final double configured_fitness_ratio;

    private static final double HALF_PROBABILITY = 0.5;

    private double stored_fitness = 0;

    private double stored_fitness_factor = 0;

    private final int size_of_genes;

    public Genes(Config config){
        this.config = config;
        size_of_genes = config.number_of_genes * config.size_of_each_gene;
        long max_value = Math.min(Long.MAX_VALUE, (long)Math.pow(2, size_of_genes)-1);
        this.configured_fitness_ratio = (config.float_upper - config.float_lower) / max_value;
    }

    public void buildEmpty(){
        genes = new BitSet(size_of_genes);
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

    public void inheritFrom(Genes mother, Genes father) {
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

    public boolean areEmpty(){
        return genes.isEmpty();
    }

    public double fitness(double fitness_factor){
        if (abs(fitness_factor - stored_fitness_factor) < 0.01) {
            return stored_fitness;
        }
        // We need to calculate the fitness again
        stored_fitness_factor = fitness_factor;

        /*
            Product(1..n) sin(x) exp y
            where x in [0..pi]
            y is the fitness factor that narrows the range of x that produces a non zero result
            This function produces a shallow arc of fitness values across the range of x, varying from 0 to 1
            The fitness factor narrows the width of the arc, centred on 0.5, so genes with values closer to 0 or pi will
            produce lower fitness values. Increasing the fitness factor will narrow the arc, reducing the %
            of fitness results that are away from 0.5. Reducing the fitness factor below 1.0 will widen the arc.
         */

        double fitness = 1.0;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            fitness *= Math.pow(Math.sin(config.float_lower + configured_fitness_ratio * Math.abs(integer_values[i])), fitness_factor);
        }

        stored_fitness = Math.max(0.0, fitness);

        return stored_fitness;
    }

    public boolean isEqual(Genes other){
        return genes.equals(other.genes);
    }
}
