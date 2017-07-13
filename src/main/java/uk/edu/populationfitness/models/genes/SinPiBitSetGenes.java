package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class SinPiBitSetGenes extends BitSetGenes {

    protected double configured_fitness_ratio;

    public SinPiBitSetGenes(Config config){
        super(config);
        long max_value = Math.min(Long.MAX_VALUE, (long)Math.pow(2, size_of_genes)-1);
        this.configured_fitness_ratio = ((config.float_upper) - config.float_lower) / max_value;
    }

    @Override
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
            The fitness factor narrows the width of the arc, peaking on 1, so genes with values closer to 0 or pi will
            produce lower fitness values. Increasing the fitness factor will narrow the arc, reducing the %
            of fitness results that are away from 0.5. Reducing the fitness factor below 1.0 will widen the arc.
         */

        double fitness = 1.0;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            long value =  Math.abs(integer_values[i]);
            fitness *= Math.pow(Math.sin(config.float_lower + configured_fitness_ratio * value), fitness_factor);
        }

        stored_fitness = Math.max(0.0, fitness);

        return stored_fitness;
    }
}
