package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

/**
 * Created by pete.callaghan on 13/07/2017.
 */
public class SinPiLinearBitSetGenes extends SinPiBitSetGenes {

    public SinPiLinearBitSetGenes(Config config){
        super(config);
    }

    @Override
    public double fitness(double fitness_factor){
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }
        /*
            Product(1..n) sin(x) exp y
            where x in [0..pi/2]
            y is the fitness factor that narrows the range of x that produces a non zero result
            This function produces a shallow arc of fitness values across the range of x, varying from 0 to 1
            The fitness factor narrows the width of the arc, peaking on 1, so genes with values closer to 0 or pi will
            produce lower fitness values. Increasing the fitness factor will narrow the arc, reducing the %
            of fitness results that are away from 0.5. Reducing the fitness factor below 1.0 will widen the arc.
         */

        double fitness = fitness_factor;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            long value =  integer_values[i];
            double ratio = (i == integer_values.length - 1 ? remainder_interpolation_ratio : interpolation_ratio);
            fitness *= Math.sin(config.float_lower + ratio * value);
        }

        return storedFitness(fitness_factor, Math.abs(fitness));
    }
}
