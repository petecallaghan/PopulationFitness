package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;

public abstract class InterpolatingBitSetGenes extends InvertedBitSetGenes {

    protected double interpolation_ratio;

    /**
     * Calculates the maximum value given the bit count
     *
     * @param bitCount
     * @return
     */
    private static long maxForBits(long bitCount){
        return Math.min(Long.MAX_VALUE, (long)Math.pow(2, bitCount)-1);
    }

    public InterpolatingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config);
        interpolation_ratio = maxInterpolatedValue / maxLongForSizeOfGene();
    }

    /**
     *
     * @return the maximum long value given the size of the genes
     */
    protected long maxLongForSizeOfGene(){
        return maxForBits(size_of_genes);
    }

    public double interpolate(long integer_value){
        return interpolation_ratio * integer_value;
    }
}


