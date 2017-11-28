package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

/**
 * Adds normalizing of fitness to caching and interpolation.
 */
public abstract class NormalizingBitSetGenes extends CachingInterpolatingBitSetGenes {

    private double normalizationRatio;

    public NormalizingBitSetGenes(Config config, double maxInterpolatedValue, double normalizationRatio) {
        super(config, maxInterpolatedValue);
        this.normalizationRatio = normalizationRatio;
    }

    public NormalizingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config, maxInterpolatedValue);
        this.normalizationRatio = 0.0;
    }

    protected void setNormalizationRatio(double normalizationRatio){
        this.normalizationRatio = normalizationRatio;
    }

    @Override
    public double fitness(double fitness_factor) {
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }

        return storeScaledFitness(fitness_factor, fitness_factor * (calculateFitnessFromIntegers(genes.toLongArray()) / normalizationRatio));
    }
}
