package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.genes.bitset.*;

public class Schwefel226Genes extends NormalizingBitSetGenes {

    public static final double SchwefelConstant = 418.982;

    public Schwefel226Genes(Config config) {
        super(config, 500.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 2.0 * SchwefelConstant * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.sfu.ca/~ssurjano/schwef.html
         *
         * f(x) = 418.9829d - sum{i=1 to d}[x{i} * sin(sqrt(mod(x{i})))]
         *
         * Dimensions: d

         The Schwefel226 function is complex, with many local minima. The plot shows the two-dimensional form of the function.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-500, 500], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (420.9687,...,420.9687)

         */
        double fitness = SchwefelConstant * integer_values.length;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness -= (x * CosSineCache.sin(Math.sqrt(Math.abs(x))));
        }
        return fitness;
    }
}