package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SchumerSteiglitzGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator {
        @Override
        public double calculateValue(long n) {
            return 100000000.0 * n;
        }
    }

    private static final ExpensiveCalculatedValues NormalizationRatios = new ExpensiveCalculatedValues(new SchumerSteiglitzGenes.NormalizationRatioCalculator());

    public SchumerSteiglitzGenes(Config config) {
        super(config, 100.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {4}}
         */

        double fitness = 0.0;

        for (long integer_value : integer_values) {
            double x = interpolate(integer_value);

            fitness += FastMaths.pow(x, 4);
        }

        return fitness;
    }
}
