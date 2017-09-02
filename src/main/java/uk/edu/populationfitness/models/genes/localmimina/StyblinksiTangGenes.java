package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class StyblinksiTangGenes extends InvertedBitSetGenes {
    private double interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 5.0 / max_value;
    }

    public StyblinksiTangGenes(Config config) {
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
         * http://www.sfu.ca/~ssurjano/stybtang.html
         *
         * f(x) = 1/2 * sum{i=1 to n}[x{i}^4 - 16x{i}^2 + 5x{i}]
         *
         * Dimensions: d

         The function is usually evaluated on the hypercube xi ∈ [-5, 5], for all i = 1, …, d.

         Global Minimum:

         f(x) = -39.16599d at x = (-2.903534,...-2.903534)
         */
        double fitness = 0;
        long[] integer_values = genes.toLongArray();

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness += (Math.pow(x, 4) - 16 * Math.pow(x, 2) + 5 * x);
        }

        fitness = fitness / 2;

        return storeScaledInvertedFitness(fitness_factor, fitness);
    }

    private double interpolate(int i, long[] integer_values){
        return interpolation_ratio * integer_values[i];
    }
}
