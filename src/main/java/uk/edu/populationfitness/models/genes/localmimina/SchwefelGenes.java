package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.CachingInterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class SchwefelGenes extends CachingInterpolatingBitSetGenes {

    public SchwefelGenes(Config config) {
        super(config, 500.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.sfu.ca/~ssurjano/schwef.html
         *
         * f(x) = 418.9829d - sum{i=1 to d}[x{i} * sin(sqrt(mod(x{i})))]
         *
         * Dimensions: d

         The Schwefel function is complex, with many local minima. The plot shows the two-dimensional form of the function.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-500, 500], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (420.9687,...,420.9687)

         */
        double fitness = 418.9829 * integer_values.length;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness -= (x * Math.sin(Math.sqrt(Math.abs(x))));
        }
        return fitness;
    }
}
