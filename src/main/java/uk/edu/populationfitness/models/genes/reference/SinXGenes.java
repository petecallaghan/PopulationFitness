package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SinXGenes extends NormalizingBitSetGenes {
    public SinXGenes(Config config) {
        super(config, 360.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 2.0 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {

        double fitness = integer_values.length;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);
            fitness += CosSineCache.sin(x);
        }

        return fitness;
    }
}
