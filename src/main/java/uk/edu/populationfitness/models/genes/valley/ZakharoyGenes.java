package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class ZakharoyGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator<Double>{
        @Override
        public Double calculateValue(long n) {
            double value = (n * (n + 1.0) ) / 2.0;
            return 100 * n + 25 * (value * value + 35.0 * FastMaths.pow(value, 4));
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new NormalizationRatioCalculator());

    public ZakharoyGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /*
          f left (x right ) = sum from {i=1} to {n} {{x} rsub {i} rsup {2}} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {2} + {left (sum from {i=1} to {n} {0.5 i  {x} rsub {i}} right )} ^ {4}
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
