package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

/**
 * Only calculates the fitness if the fitness factor has changed.
 */
public abstract class CachingInterpolatingBitSetGenes extends InterpolatingBitSetGenes{

    public CachingInterpolatingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config, maxInterpolatedValue);
    }

    /**
     * Implement this to calculate the fitness from an array of integers.
     *
     * @param integer_values
     * @return
     */
    protected abstract double calculateFitnessFromIntegers(long[] integer_values);

    @Override
    public double fitness(double fitness_factor) {
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }

        return storeScaledInvertedFitness(fitness_factor, calculateFitnessFromIntegers(genes.toLongArray()));
    }
}


