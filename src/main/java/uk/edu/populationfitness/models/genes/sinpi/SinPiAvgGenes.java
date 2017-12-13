package uk.edu.populationfitness.models.genes.sinpi;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class SinPiAvgGenes extends SinPiGenes {
    public SinPiAvgGenes(Config config){
        super(config);
    }

    @Override
    public double fitness(double fitness_factor){
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }
        /*
            Sum(1..n) sin(x) exp y / n
            where x in [0..pi]
         */

        double fitness = 0;
        long[] integer_values = asIntegers();

        for(int i = 0; i < integer_values.length; i++){
            long value =  integer_values[i];
            fitness += Math.abs(Math.pow(Math.sin(config.float_lower + interpolation_ratio * value), fitness_factor));
        }

        fitness = integer_values.length > 0 ? Math.abs(fitness) / integer_values.length : 0;
        return scaleAndStoreFitness(fitness_factor, fitness);
    }
}
