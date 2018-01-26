package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class AlpineGenes extends NormalizingBitSetGenes {

    public AlpineGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 8.7149 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {

        /*
          f left (x right ) = sum from {i=1} to {n} {left lline {x} rsub {i} sin {left ({x} rsub {i} right ) +0.1 {x} rsub {i}} right rline}
         */

        double fitness = 0.0;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);
            fitness += Math.abs(x * CosSineCache.sin(x) + 0.1 * x);
        }

        return fitness;
    }
}
