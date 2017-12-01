package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SalomonGenes extends NormalizingBitSetGenes {
    private static final double NormalizationConstant = 200.0 * Math.PI + 10.0;

    private static final double TwoPi = 2.0 * Math.PI;

    public SalomonGenes(Config config) {
        super(config, 100.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 1.0 - CosSineCache.cos(NormalizationConstant * Math.sqrt(n));
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) =1- cos {left (2π sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right ) +0.1 sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}}}
         */

        double sum = 0.0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            sum += x * x;
        }

        double sqrtSum = Math.sqrt(sum);

        return 1.0 - CosSineCache.cos(TwoPi * sqrtSum) + 0.1 * sqrtSum;
    }
}
