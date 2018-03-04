package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

abstract class InvertedBitSetGenes extends BitSetGenes {
    InvertedBitSetGenes(Config config) {
        super(config);
    }

    /**
     * Call this to store the fitness, inverted to 1- fitness
     *
     * @param fitness_factor
     * @param fitness
     * @return the scaled stored fitness
     */
    double storeScaledInvertedFitness(double fitness_factor, double fitness){
        return storeScaledFitness(fitness_factor, fitness_factor * (1 - config.getRange().toScale(fitness)));
    }
}
