package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ZakharoyGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator{
        @Override
        public double calculateValue(long n) {
            /**
             * Calculates the normalization ratio for a size of n
             *
             * {f left (x right )} over {100 n+ {left (sum from {i=1} to {n} {5 i } right )} ^ {2} + {left (sum from {i=1} to {n} {5 i } right )} ^ {4}}
             */
            if (n < 1) return 1;

            double value = 100 * n;
            double sum = 0;
            for(int i = 1; i <= n; i++){
                sum += i * 5;
            }
            return value + (sum * sum) + (sum * sum * sum * sum);
        }
    }

    private static final ExpensiveCalculatedValues NormalizationRatios = new ExpensiveCalculatedValues(new NormalizationRatioCalculator());

    public ZakharoyGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {2}} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {2} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {4}
         */
        double fitness = 0;

        double sum = 0;

        for(int i = 1; i <= integer_values.length; i++){
            double x = interpolate(integer_values[i - 1]);
            fitness += x * x;
            sum += 0.5 * i * x;
        }

        fitness += (sum * sum) + (sum * sum * sum * sum);

        return fitness;
    }
}
