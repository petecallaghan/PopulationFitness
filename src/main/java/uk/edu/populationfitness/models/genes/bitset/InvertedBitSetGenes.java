package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

abstract class InvertedBitSetGenes extends BitSetGenes {
    InvertedBitSetGenes(Config config) {
        super(config);
    }

    /**
     * Call this to store the fitness, inverted to 1- fitness
     *
     * @param fitness
     * @return the scaled stored fitness
     */
    double storeScaledInvertedFitness(double fitness){
        return storeFitness((1 - config.getRange().toScale(fitness)));
    }
}
