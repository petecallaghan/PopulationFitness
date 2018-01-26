package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class RosenbrockGenes extends NormalizingBitSetGenes {

    public RosenbrockGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 1222100 * (n - 1);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          http://www.sfu.ca/~ssurjano/rosen.html

          f(x) = sum{i=1 to d}[100( (x{i+1} - x{i}^2)^2 + (x{i} - 1)^2 )]

          Dimensions: d

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
            double xSquared = x * x;
            double diff = xplus1 - xSquared;
            fitness += 100.0 * ( diff * diff + (x - 1) * (x - 1));
        }
        return fitness;
    }
}
