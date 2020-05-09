package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SalomonGenes extends NormalizingBitSetGenes {
    private static final double MAX = 100.0;

    private static final double TwoPi = 2.0 * Math.PI;

    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            double sum = 0.0;

            for (int i = 0; i < n; i++) {
                sum += MAX * MAX;
            }

            final double sqrtSum = Math.sqrt(sum);

            return 1.0 - Math.cos(TwoPi * sqrtSum) + 0.1 * sqrtSum;
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new SalomonGenes.NormalizationRatioCalculator());

    public SalomonGenes(Config config) {
        super(config, MAX);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          f left (x right ) =1- cos {left (2π sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right ) +0.1 sqrt {sum from {i=1} to {n} {{x} rsub {i} rsup {2}}}}
         */

        double sum = 0.0;

        for (double x : unknowns) {
            sum += x * x;
        }

        final double sqrtSum = Math.sqrt(sum);

        return 1.0 - Math.cos(TwoPi * sqrtSum) + 0.1 * sqrtSum;
    }
}
