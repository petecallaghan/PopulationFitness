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
    protected double calculateFitnessFromGenes(double[] unknowns) {

        /*
          f left (x right ) =- sum from {i=1} to {n} {left lline {x} rsub {i} right rline}
         */
        double fitness = 10.0 * unknowns.length;

        for (double x : unknowns) {
            fitness -= Math.abs(x);
        }

        return fitness;
    }
}
