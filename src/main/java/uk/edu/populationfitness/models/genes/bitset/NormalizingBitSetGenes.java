package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

/**
 * Adds normalizing of fitness to caching and interpolation.
 */
public abstract class NormalizingBitSetGenes extends CachingInterpolatingBitSetGenes {

    private double normalizationRatio;

    private boolean isNormalisationRatioSet;

    protected NormalizingBitSetGenes(Config config, double maxInterpolatedValue) {
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
    public double fitness() {
        if (isFitnessStored()) {
            return storedFitness();
        }

        final long[] integers = asIntegers();

        if (!isNormalisationRatioSet){
            normalizationRatio = calculateNormalizationRatio(integers.length);
            isNormalisationRatioSet = true;
        }

        return storeFitness(calculateFitnessFromIntegers(integers) / normalizationRatio);
    }
}
