package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class BrownGenes extends NormalizingBitSetGenes {
    public BrownGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 2.0 * (n - 1);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = sum from {i=1} to {n-1} {left [{left ({x} rsub {i} rsup {2} right )} ^ {{x} rsub {i+1} rsup {2} +1} + {left ({x} rsub {i+1} rsup {2} right )} ^ {{x} rsub {i} rsup {2} +1} right ]}
         */

        double fitness = 0.0;

        for(int i = 0; i < integer_values.length - 1; i++){
            double xN = interpolate(integer_values[i]);
            double xNPlus1 = interpolate(integer_values[i+1]);
            double xNSquared = Math.pow(xN, 2.0);
            double xNPlus1Squared = Math.pow(xNPlus1, 2.0);
            fitness += Math.pow(xNSquared, xNPlus1Squared + 1.0) + Math.pow(xNPlus1Squared, xNSquared + 1.0);
        }

        return fitness;
    }
}


