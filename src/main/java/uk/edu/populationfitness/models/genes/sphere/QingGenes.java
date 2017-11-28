package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class QingGenes extends NormalizingBitSetGenes {

    public QingGenes(Config config) {
        super(config, 500.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        double ratio = 0.0;
        for(int i = 1; i <= n; i++){
            ratio +=Math.pow(250000.0  - i, 2.0);
        }
        return n > 0 ? ratio : 1.0;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} rsup {2} -i right )} ^ {2}}
         */

        double fitness = 0.0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);

            fitness += Math.pow(Math.pow(x, 2.0) - (i + 1), 2.0);
        }
        return fitness;
    }
}
