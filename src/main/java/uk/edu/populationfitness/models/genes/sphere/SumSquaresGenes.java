package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.CachingInterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SumSquaresGenes extends NormalizingBitSetGenes {
    public SumSquaresGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        double sum = 0.0;

        for(int i = 1; i <= n; i++){
            sum += 100 * i;
        }
        return sum;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
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
        double fitness = 0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += (i + 1) * Math.pow(x, 2);
        }
        return fitness;
    }
}
