package uk.edu.populationfitness.models.genes.Ridge;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class Schwefel220Genes extends NormalizingBitSetGenes {
    public Schwefel220Genes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 10.0 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {

        /**
         * f left (x right ) =- sum from {i=1} to {n} {left lline {x} rsub {i} right rline}
         */
        double fitness = 10.0 * integer_values.length;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness -= Math.abs(x);
        }

        return fitness;
    }
}
