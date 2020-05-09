package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class SumOfPowersGenes extends NormalizingBitSetGenes {
    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            double max = 0;

            for(int i = 0; i < n; i++){
                max += FastMaths.pow(0.97, i + 2);
            }
            return max;
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new SumOfPowersGenes.NormalizationRatioCalculator());

    public SumOfPowersGenes(Config config) {
        super(config, 1.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          http://www.sfu.ca/~ssurjano/sumpow.html

          f(x) = sum{i=1 to d}[ abs(x{i}) ^ i+1]

          Dimensions: d

         The function is unimodal.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-1, 1], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (0,...,0)

         */
        double fitness = 0;

        for(int i = 0; i < unknowns.length; i++){
            double x = unknowns[i];
            fitness += FastMaths.pow(Math.abs(x), i + 2);
        }
        return fitness;
    }
}
