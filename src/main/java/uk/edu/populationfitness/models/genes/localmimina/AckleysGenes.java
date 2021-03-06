package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class AckleysGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            /*
              {f left (x right )} over {20 left (1- {e} ^ {-0.2α} right ) +e- {e} ^ {-1}}
             */
            return 20.0 * (1.0 - Math.exp(-0.2 * alpha)) + Math.E - Math.exp(-1.0);
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new AckleysGenes.NormalizationRatioCalculator());

    private static final double alpha = 4.5;

    private static final double TwentyPlusE = 20.0 + Math.E;

    private static final double TwoPi = 2.0 * Math.PI;

    public AckleysGenes(Config config) {
        super(config, 5.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          http://www.cs.unm.edu/~neal.holts/dga/benchmarkFunction/ackley.html

          f left (x right ) =-20 exp {left (-0.2 sqrt {{1} over {n} sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right )} - exp {left ({1} over {n} sum from {i=1} to {n} {cos {left ({2πx} rsub {i} right )}} right ) +20+ exp⁡ (1)}
         */
        double firstSum = 0.0;
        double secondSum = 0.0;

        for (double x : unknowns) {
            firstSum += x * x;
            secondSum += CosSineCache.cos(TwoPi * x);
        }

        double n = unknowns.length;

        return -20.0 * Math.exp(-0.2 * Math.sqrt(firstSum / n)) - Math.exp(secondSum / n) + TwentyPlusE;
    }
}
