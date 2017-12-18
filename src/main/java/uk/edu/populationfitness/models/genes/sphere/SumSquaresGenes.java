package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;
import uk.edu.populationfitness.models.genes.valley.DixonPriceGenes;

public class SumSquaresGenes extends NormalizingBitSetGenes {

    private static class NormalizationRatioCalculator implements ValueCalculator {
        @Override
        public double calculateValue(long n) {
            double sum = 0.0;

            for(int i = 1; i <= n; i++){
                sum += 100 * i;
            }
            return sum;
        }
    }

    private static final ExpensiveCalculatedValues NormalizationRatios = new ExpensiveCalculatedValues(new NormalizationRatioCalculator());

    public SumSquaresGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
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
            fitness += (i + 1) * x * x;
        }
        return fitness;
    }
}
