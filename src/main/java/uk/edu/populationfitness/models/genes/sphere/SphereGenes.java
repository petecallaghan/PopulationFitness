package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SphereGenes extends NormalizingBitSetGenes {
    public SphereGenes(Config config) {
        super(config, 5.12);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 5.12 * 5.12 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.sfu.ca/~ssurjano/spheref.html
         *
         * f(x) = sum{i=1 to n}[x{i}^2]
         *
         * Dimensions: d

         The Sphere function has d local minima except for the global one. It is continuous, convex and unimodal. The plot shows its two-dimensional form.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-5.12, 5.12], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (0,...,0)

         */
        double fitness = 0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += x * x;
        }
        return fitness;
    }
}
