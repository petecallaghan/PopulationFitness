package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.ExpensiveCalculatedValues;
import uk.edu.populationfitness.models.fastmaths.ValueCalculator;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class Schwefel226Genes extends NormalizingBitSetGenes {

    private static final double SchwefelConstant = 418.982;

    private static final double SchwefelConstant2 = 420.9687;

    private static class NormalizationRatioCalculator implements ValueCalculator<Double> {
        @Override
        public Double calculateValue(long n) {
            return SchwefelConstant * n + SchwefelConstant2 * Math.sin(Math.sqrt(SchwefelConstant2)) * n;
        }
    }

    private static final ExpensiveCalculatedValues<Double> NormalizationRatios = new ExpensiveCalculatedValues(new NormalizationRatioCalculator());

    public Schwefel226Genes(Config config) {
        super(config, SchwefelConstant);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return NormalizationRatios.findOrCalculate(n);
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          http://www.sfu.ca/~ssurjano/schwef.html

          f(x) = 418.9829d - sum{i=1 to d}[x{i} * sin(sqrt(mod(x{i})))]

          Dimensions: d

         The Schwefel226 function is complex, with many local minima. The plot shows the two-dimensional form of the function.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-500, 500], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (420.9687,...,420.9687)

         */
        double fitness = SchwefelConstant * unknowns.length;

        for (double x :unknowns) {
            fitness -= (x * Math.sin(Math.sqrt(Math.abs(x))));
        }
        return fitness;
    }
}
