package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class DixonPriceGenes extends NormalizingBitSetGenes {

    private static final double TwoTenSquared = Math.pow(210.0, 2.0);

    public DixonPriceGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 121.0 + TwoTenSquared * (( 2.0 + n) / 2.0) * (n - 1);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = {left ({x} rsub {1} -1 right )} ^ {2} +  sum from {i=2} to {n} {i {left ({2x} rsub {i} rsup {2} - {x} rsub {i-1} right )} ^ {2}}
         */

        double fitness = integer_values.length > 0 ? Math.pow(interpolate(integer_values[0]) - 1.0, 2.0) : 0.0;

        for(int i = 1; i < integer_values.length; i++) {
            double xIMinus1 = interpolate(integer_values[i - 1]);
            double x = interpolate(integer_values[i]);

            fitness += (i + 1) * Math.pow((2.0 * Math.pow(x, 2.0) - xIMinus1), 2.0);
        }

        return fitness;
    }
}
