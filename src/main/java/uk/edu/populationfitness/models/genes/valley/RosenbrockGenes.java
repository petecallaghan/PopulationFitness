package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.CachingInterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class RosenbrockGenes extends CachingInterpolatingBitSetGenes {
    public RosenbrockGenes(Config config) {
        super(config, 2.048);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.sfu.ca/~ssurjano/rosen.html
         *
         * f(x) = sum{i=1 to d}[100( (x{i+1} - x{i}^2)^2 + (x{i} - 1)^2 )]
         *
         * Dimensions: d

         The Rosenbrock function, also referred to as the Valley or Banana function,
         is a popular test problem for gradient-based optimization algorithms

         The function is unimodal, and the global minimum lies in a narrow, parabolic valley.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-5, 10], for all i = 1, …, d,
         although it may be restricted to the hypercube xi ∈ [-2.048, 2.048], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (1,...,1)

         */
        double fitness = 0;

        for(int i = 0; i < integer_values.length - 1; i++){
            double x = interpolate(integer_values[i]);
            double xplus1 = interpolate(integer_values[i+1]);
            fitness += ( 100.0 * ( Math.pow( xplus1 - Math.pow(x, 2), 2) + Math.pow(x - 1, 2)) );
        }
        return fitness;
    }
}
