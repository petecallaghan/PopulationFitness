package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class QingGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            double ratio = 0.0;
            for(int i = 1; i <= n; i++){
                double value = 250000.0  - i;
                ratio += value * value;
            }
            return n > 0 ? ratio : 1.0;
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new QingGenes.NormalizationRatioCalculator());

    public QingGenes(Config config) {
        super(config, 500.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          f left (x right ) = sum from {i=1} to {n} {{left ({x} rsub {i} rsup {2} -i right )} ^ {2}}
         */

        double fitness = 0.0;

        for(int i = 0; i < unknowns.length; i++){
            double x = unknowns[i];
            double value = x * x - (i + 1);
            fitness += value * value;
        }
        return fitness;
    }
}
