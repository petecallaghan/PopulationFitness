package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class SumSquaresGenes extends InvertedBitSetGenes {

    private double interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 5.12 / max_value;
    }

    public SumSquaresGenes(Config config) {
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
         * http://www.sfu.ca/~ssurjano/sumsqu.html
         *
         * f(x) = sum{i=1 to d}[ i * x{i} ^ 2]
         *
         * Dimensions: d

         The Sum Squares function, also referred to as the Axis Parallel Hyper-Ellipsoid function,
         has no local minimum except the global one. It is continuous, convex and unimodal.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-10, 10], for all i = 1, …, d,
         although this may be restricted to the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (0,...,0)

         */
        long[] integer_values = genes.toLongArray();
        double fitness = 0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness += i * Math.pow(x, 2);
        }

        return storeScaledInvertedFitness(fitness_factor, fitness);
    }

    private double interpolate(int i, long[] integer_values){
        return interpolation_ratio * integer_values[i];
    }
}
