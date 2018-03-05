package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class BrownGenes extends NormalizingBitSetGenes {
    public BrownGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return n > 1 ? 2.0 * (n - 1) : 1.0;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          f left (x right ) = sum from {i=1} to {n-1} {left [{left ({x} rsub {i} rsup {2} right )} ^ {{x} rsub {i+1} rsup {2} +1} + {left ({x} rsub {i+1} rsup {2} right )} ^ {{x} rsub {i} rsup {2} +1} right ]}
         */

        double fitness = 0.0;

        double xN = interpolate(integer_values[0]);
        double xNSquared = xN * xN;

        if (integer_values.length < 2){
            return xNSquared;
        }

        for(int i = 0; i < integer_values.length - 1; i++){
            double xNPlus1 = interpolate(integer_values[i+1]);
            double xNPlus1Squared = xNPlus1 * xNPlus1;
            fitness += Math.pow(xNSquared, xNPlus1Squared + 1.0) + Math.pow(xNPlus1Squared, xNSquared + 1.0);

            xNSquared = xNPlus1Squared;
        }

        return fitness;
    }
}


