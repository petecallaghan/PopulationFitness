package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ExponentialGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            return 1.0 - Math.exp(-0.5 * n);
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new ExponentialGenes.NormalizationRatioCalculator());

    public ExponentialGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          f left (x right ) =- exp left (-0.5 sum from {i=2} to {n} {{x} rsub {i} rsup {2}} right )
         */

        double fitness = 0.0;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);

            fitness += x * x;
        }

        return 1.0 - Math.exp(-0.5 * fitness);
    }
}
