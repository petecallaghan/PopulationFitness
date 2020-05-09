package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class QingGenes extends NormalizingBitSetGenes {
    private static final double MAX = 500.0;

    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            double ratio = 0.0;

            for(int i = 0; i < n; i++){
                double value = (MAX * MAX) / 1.2;// - (i + 1);
                ratio += value * value;
            }
            return ratio;
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new QingGenes.NormalizationRatioCalculator());

    public QingGenes(Config config) {
        super(config, MAX);
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
