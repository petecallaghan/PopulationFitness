package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class SchwefelGenes extends InvertedBitSetGenes {

    private double interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 500.0 / max_value;
    }

    public SchwefelGenes(Config config) {
        super(config);
        long max_value = maxForBits(size_of_genes);
        interpolation_ratio = interpolationRatio(max_value);
    }

    @Override
    public double fitness(double fitness_factor) {
        if (isSameFitnessFactor(fitness_factor)) {
            return storedFitness();
        }
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
        long[] integer_values = genes.toLongArray();
        double fitness = 418.9829 * integer_values.length;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness -= (x * Math.sin(Math.sqrt(Math.abs(x))));
        }

        return storeScaledInvertedFitness(fitness_factor, fitness);
    }

    private double interpolate(int i, long[] integer_values){
        return interpolation_ratio * integer_values[i];
    }
}
