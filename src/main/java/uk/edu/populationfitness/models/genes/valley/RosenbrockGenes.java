package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class RosenbrockGenes extends InvertedBitSetGenes {

    private double interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 2.048 / max_value;
    }

    public RosenbrockGenes(Config config) {
        super(config);
        long max_value = maxForBits(size_of_genes);
        interpolation_ratio = interpolationRatio(max_value);
    }

    @Override
    public double fitness(double fitness_factor) {
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }
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
        long[] integer_values = genes.toLongArray();
        double fitness = 0;

        for(int i = 0; i < integer_values.length - 1; i++){
            double x = interpolate(i, integer_values);
            double xplus1 = interpolate(i+1, integer_values);
            fitness += ( 100.0 * ( Math.pow( xplus1 - Math.pow(x, 2), 2) + Math.pow(x - 1, 2)) );
        }

        return storeScaledInvertedFitness(fitness_factor, fitness);
    }

    private double interpolate(int i, long[] integer_values){
        return interpolation_ratio * integer_values[i];
    }
}
