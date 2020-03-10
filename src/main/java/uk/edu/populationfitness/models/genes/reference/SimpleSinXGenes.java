package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SimpleSinXGenes extends NormalizingBitSetGenes {
    public SimpleSinXGenes(Config config) {
        super(config, 180.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 1.0 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {

        double fitness = 0;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);
            double y = CosSineCache.sin(x);
            fitness += y;
        }

        return fitness;
    }
}
