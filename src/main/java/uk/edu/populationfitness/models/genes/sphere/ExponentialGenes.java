package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;
import uk.edu.populationfitness.models.genes.localmimina.AckleysGenes;

public class ExponentialGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator {
        @Override
        public double calculateValue(long n) {
            return 1.0 - Math.exp(-0.5 * n);
        }
    }

    private static final ExpensiveCalculatedValues NormalizationRatios = new ExpensiveCalculatedValues(new ExponentialGenes.NormalizationRatioCalculator());

    public ExponentialGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) =- exp left (-0.5 sum from {i=2} to {n} {{x} rsub {i} rsup {2}} right )
         */

        double fitness = 0.0;

        for(int i = 0; i < integer_values.length; i++) {
            double x = interpolate(integer_values[i]);

            fitness += x * x;
        }

        return 1.0 - Math.exp(-0.5 * fitness);
    }
}
