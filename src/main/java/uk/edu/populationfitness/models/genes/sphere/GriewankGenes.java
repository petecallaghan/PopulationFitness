package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class GriewankGenes extends NormalizingBitSetGenes {
    public GriewankGenes(Config config) {
        super(config, 600.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return n < 1 ? 1.0 : 900.0 * n - n * Math.cos(600.0 / n) + 1.0;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = sum from {i=1} to {n} {{{x} rsub {i} rsup {2}} over {400} - prod from {i=1} to {n} {cos {left ({{x} rsub {i}} over {sqrt {i}} right )} +1}}
         */

        if (integer_values.length < 1) return 0.0;

        double fitness = 1.0;
        double product = 1.0;

        for(int i = 1; i < integer_values.length; i++) {
            double x = interpolate(integer_values[i]);

            product *= Math.cos(x / Math.sqrt(1.0 * i));
            fitness += (Math.pow(x, 2.0) / 400.0);
        }
        return fitness + product;
    }
}
