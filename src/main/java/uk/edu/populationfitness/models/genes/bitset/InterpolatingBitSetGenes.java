package uk.edu.populationfitness.models.genes.bitset;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;

public abstract class InterpolatingBitSetGenes extends BitSetGenes {
    private static class BitCountCalculator implements ValueCalculator<Long> {
        @Override
        public Long calculateValue(long bitCount) {
            return Math.min(Long.MAX_VALUE, (long)FastMaths.pow(2, bitCount)-1);
        }
    }

    private static final ExpensiveCalculatedValues<Long> BitCounts = new ExpensiveCalculatedValues(new InterpolatingBitSetGenes.BitCountCalculator());

    protected double interpolation_ratio;

    /**
     * Calculates the maximum value given the bit count
     *
     * @param bitCount
     * @return
     */
    private static long maxForBits(long bitCount){
        return BitCounts.findOrCalculate(bitCount);
    }

    protected InterpolatingBitSetGenes(Config config, double maxInterpolatedValue) {
        super(config);
        interpolation_ratio = maxInterpolatedValue / maxLongForSizeOfGene();
    }

    /**
     *
     * @return the maximum long value given the size of the genes
     */
    protected long maxLongForSizeOfGene(){
        return maxForBits(numberOfBits());
    }

    protected double interpolate(long integer_value){
        return interpolation_ratio * integer_value;
    }
}


