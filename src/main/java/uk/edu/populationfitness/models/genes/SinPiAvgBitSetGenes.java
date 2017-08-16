package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class SinPiAvgBitSetGenes extends SinPiBitSetGenes {
    public SinPiAvgBitSetGenes(Config config){
        super(config);
    }

    @Override
    public double fitness(double fitness_factor){
        if (abs(fitness_factor - stored_fitness_factor) < 0.01) {
            return stored_fitness;
        }
        // We need to calculate the fitness again
        stored_fitness_factor = fitness_factor;

        /*
            Sum(1..n) sin(x) exp y / n
            where x in [0..pi]
         */

        double fitness = 0;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            long value =  integer_values[i];
            double ratio = (i == integer_values.length - 1 ? remainder_interpolation_ratio : interpolation_ratio);
            fitness += Math.abs(Math.pow(Math.sin(config.float_lower + ratio * value), fitness_factor));
        }

        fitness = integer_values.length > 0 ? Math.abs(fitness) / integer_values.length : 0;
        stored_fitness = fitness;

        return stored_fitness;
    }
}
