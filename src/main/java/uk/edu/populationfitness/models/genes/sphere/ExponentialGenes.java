package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ExponentialGenes extends NormalizingBitSetGenes {

    public ExponentialGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 1.0 - Math.exp(-0.5 * n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) =- exp left (-0.5 sum from {i=2} to {n} {{x} rsub {i} rsup {2}} right )
         */

        double fitness = 0.0;

        for(int i = 1; i < integer_values.length; i++) {
            double x = interpolate(integer_values[i]);

            fitness += Math.pow(x, 2.0);
        }

        return 1.0 - Math.exp(-0.5 * fitness);
    }
}
