package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

/**
 * Only calculates the fitness if the fitness factor has changed.
 */
public abstract class CachingInterpolatingBitSetGenes extends InterpolatingBitSetGenes{

    CachingInterpolatingBitSetGenes(Config config, double maxInterpolatedValue) {
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
    public double fitness() {
        if (isFitnessStored()) {
            return storedFitness();
        }

        return storeScaledInvertedFitness(calculateFitnessFromIntegers(asIntegers()));
    }
}


