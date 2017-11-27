package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.CachingInterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class SumOfPowersGenes extends CachingInterpolatingBitSetGenes {
    public SumOfPowersGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.sfu.ca/~ssurjano/sumpow.html
         *
         * f(x) = sum{i=1 to d}[ abs(x{i}) ^ i+1]
         *
         * Dimensions: d

         The function is unimodal.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-1, 1], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (0,...,0)

         */
        double fitness = 0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += Math.pow(Math.abs(x), i + 1);
        }
        return fitness;
    }
}
