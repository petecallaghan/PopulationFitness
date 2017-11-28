package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

/**
 * Adds normalizing of fitness to caching and interpolation.
 */
public abstract class NormalizingBitSetGenes extends CachingInterpolatingBitSetGenes {

    private double normalizationRatio;

    private boolean isNormalisationRatioSet;

    public NormalizingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config, maxInterpolatedValue);
        isNormalisationRatioSet = false;
    }

    /***
     * Implement this to define the normalization ratio
     * @param n
     * @return the normalization ratio
     */
    protected abstract double calculateNormalizationRatio(int n);

    @Override
    public double fitness(double fitness_factor) {
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }

        long[] integers = genes.toLongArray();

        if (!isNormalisationRatioSet){
            normalizationRatio = calculateNormalizationRatio(integers.length);
            isNormalisationRatioSet = true;
        }

        return storeScaledFitness(fitness_factor, fitness_factor * (calculateFitnessFromIntegers(integers) / normalizationRatio));
    }
}
