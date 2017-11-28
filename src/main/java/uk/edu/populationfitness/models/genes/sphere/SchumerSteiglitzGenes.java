package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SchumerSteiglitzGenes extends NormalizingBitSetGenes {

    public SchumerSteiglitzGenes(Config config) {
        super(config, 100.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 10000.0 * Math.pow(n, 4.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {4}}
         */

        double fitness = 0.0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);

            fitness += Math.pow(x, 4.0);
        }

        return fitness;
    }
}
