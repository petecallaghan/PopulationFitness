package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class GriewankGenes extends NormalizingBitSetGenes {
    public GriewankGenes(Config config) {
        super(config, 600.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 900.0 * n + 2.0;
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          f left (x right ) = sum from {i=1} to {n} {{{x} rsub {i} rsup {2}} over {400} - prod from {i=1} to {n} {cos {left ({{x} rsub {i}} over {sqrt {i}} right )} +1}}
         */

        if (unknowns.length < 1) return 0.0;

        double fitness = 1.0;
        double product = 1.0;

        for(int i = 1; i < unknowns.length; i++) {
            double x = unknowns[i];

            product *= CosSineCache.cos(x / Math.sqrt(1.0 * i));
            fitness += (x * x / 400.0);
        }
        return fitness + product;
    }
}
