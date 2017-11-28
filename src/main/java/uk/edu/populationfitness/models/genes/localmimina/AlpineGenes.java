package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
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

        /**
         * f left (x right ) = sum from {i=1} to {n} {left lline {x} rsub {i} sin {left ({x} rsub {i} right ) +0.1 {x} rsub {i}} right rline}
         */

        double fitness = 0.0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += Math.abs(x * Math.sin(x) + 0.1 * x);
        }

        return fitness;
    }
}
