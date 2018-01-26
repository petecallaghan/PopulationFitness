package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ChungReynoldsGenes extends NormalizingBitSetGenes {

    private static final double TenToThe8th = FastMaths.pow(10.0, 8);

    public ChungReynoldsGenes(Config config) {
        super(config, 100.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return TenToThe8th * FastMaths.pow(n, 2);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          f left (x right ) = {left (sum from {i=1} to {n} {{x} rsub {i} rsup {2}} right )} ^ {2}
         */
        double fitness = 0.0;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);

            fitness += x * x;
        }

        return fitness * fitness;
    }
}
