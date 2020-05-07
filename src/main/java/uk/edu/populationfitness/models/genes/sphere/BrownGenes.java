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
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          f left (x right ) = sum from {i=1} to {n-1} {left [{left ({x} rsub {i} rsup {2} right )} ^ {{x} rsub {i+1} rsup {2} +1} + {left ({x} rsub {i+1} rsup {2} right )} ^ {{x} rsub {i} rsup {2} +1} right ]}
         */

        if (unknowns.length < 1){
            return 0.0;
        }

        double fitness = 0.0;

        double xN = unknowns[0];
        double xNSquared = xN * xN;

        if (unknowns.length < 2){
            return xNSquared;
        }

        for(int i = 0; i < unknowns.length - 2; i++){
            double xNPlus1 = unknowns[i+1];
            double xNPlus1Squared = xNPlus1 * xNPlus1;
            fitness += Math.pow(xNSquared, xNPlus1Squared + 1.0) + Math.pow(xNPlus1Squared, xNSquared + 1.0);

            xNSquared = xNPlus1Squared;
        }

        return fitness;
    }
}


