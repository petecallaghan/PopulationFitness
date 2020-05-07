package uk.edu.populationfitness.models.genes.reference;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SinXGenes extends NormalizingBitSetGenes {
    public SinXGenes(Config config) {
        super(config, Math.PI);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 1.0 * n;
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        double fitness = 0;

        for (double x : unknowns) {
            fitness += Math.sin(x);
        }

        return fitness;
    }
}


