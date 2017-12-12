package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class TridGenes extends NormalizingBitSetGenes{
    private static class MinCalculator implements ValueCalculator{

        @Override
        public double calculateValue(long index) {
            double min = (0.0 - index) * (index + 4.0) * (index - 1.0);
            return min / 6;
        }
    }

    private static class MaxCalculator implements ValueCalculator{

        @Override
        public double calculateValue(long index) {
            /**
             * M= left [{n} over {2} right ] {( {n} ^ {2} +1)} ^ {2} + left (n- left [{n} over {2} right ] right ) {( {n} ^ {2} -1)} ^ {2} + left (n-1 right ) {n} ^ {4}
             *
             */
            long nOver2 = Math.round(((double)index) / 2.0);
            long nSquared = index * index;

            return nOver2 * FastMaths.pow(nSquared + 1, 2) +
                    (index - nOver2) * FastMaths.pow(nSquared - 1, 2) +
                    (index - 1) * nSquared * nSquared;
        }
    }

    private static final ExpensiveCalculatedValues CachedMinValues = new ExpensiveCalculatedValues(new MinCalculator());
    private static final ExpensiveCalculatedValues CachedMaxValues = new ExpensiveCalculatedValues(new MaxCalculator());

    double min;
    double max;

    public TridGenes(Config config) {
        super(config, 0.0);
    }

    /**
     * Calculates the interpolation values and the normalization values
     * @param n
     * @return
     */
    @Override
    protected double calculateNormalizationRatio(int n) {
        min = CachedMinValues.findOrCalculate(n);
        max = CachedMaxValues.findOrCalculate(n);
        calculateInterpolationRatio(n);
        return max - min;
    }

    private void calculateInterpolationRatio(int n){
        /**
         * {- {n} ^ {2} ≤x} rsub {i} ≤+ {n} ^ {2}
         */
        interpolation_ratio = n * n / maxLongForSizeOfGene();
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * https://www.sfu.ca/~ssurjano/trid.html
         *
         * f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} -1 right )} ^ {2} -} sum from {i=2} to {n} {{x} rsub {i} {x} rsub {i-1}}
         */
        double fitness = 0.0 - min;

        if (integer_values.length > 0){
            double previousX = interpolate(integer_values[0]);
            double firstSum = FastMaths.pow(previousX - 1, 2);
            double secondSum = 0;
            for(int i = 1; i < integer_values.length; i++){
                double x = interpolate(integer_values[i]);
                firstSum += FastMaths.pow(x - 1, 2);
                secondSum += x * previousX;
                previousX = x;
            }
            fitness += (firstSum - secondSum);
        }
        return fitness;
    }
}
