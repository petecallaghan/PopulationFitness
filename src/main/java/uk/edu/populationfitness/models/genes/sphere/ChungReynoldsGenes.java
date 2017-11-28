package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ChungReynoldsGenes extends NormalizingBitSetGenes {
    private static final double TenToThe8th = Math.pow(10.0, 8.0);

    public ChungReynoldsGenes(Config config) {
        super(config, 100.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        setNormalizationRatio(TenToThe8th * Math.pow(integer_values.length, 2.0));

        /**
         * f left (x right ) = {left (sum from {i=1} to {n} {{x} rsub {i} rsup {2}} right )} ^ {2}
         */
        double fitness = 0.0;

        for(int i = 0; i < integer_values.length; i++) {
            double x = interpolate(integer_values[i]);

            fitness += Math.pow(x, 2.0);
        }

        return Math.pow(fitness, 2.0);
    }
}
