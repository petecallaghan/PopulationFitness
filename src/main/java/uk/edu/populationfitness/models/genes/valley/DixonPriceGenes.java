package uk.edu.populationfitness.models.genes.valley;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class DixonPriceGenes extends NormalizingBitSetGenes {

    private static final double TwoTenSquared = FastMaths.pow(210.0, 2);

    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            return 121.0 + TwoTenSquared * (( 2.0 + n) / 2.0) * (n - 1);
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new DixonPriceGenes.NormalizationRatioCalculator());

    public DixonPriceGenes(Config config) {
        super(config, 10.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          f left (x right ) = {left ({x} rsub {1} -1 right )} ^ {2} +  sum from {i=2} to {n} {i {left ({2x} rsub {i} rsup {2} - {x} rsub {i-1} right )} ^ {2}}
         */

        double fitness = unknowns.length > 0 ? FastMaths.pow(unknowns[0] - 1.0, 2) : 0.0;

        for(int i = 1; i < unknowns.length; i++) {
            double xIMinus1 = unknowns[i - 1];
            double x = unknowns[i];
            double value = (2.0 * x * x - xIMinus1);
            fitness += (i + 1) * value * value;
        }

        return fitness;
    }
}
