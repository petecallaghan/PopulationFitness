package uk.edu.populationfitness.models.genes.sphere;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class SumOfPowersGenes extends InvertedBitSetGenes {

    private double interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 1.0 / max_value;
    }

    public SumOfPowersGenes(Config config) {
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
         * http://www.sfu.ca/~ssurjano/sumpow.html
         *
         * f(x) = sum{i=1 to d}[ abs(x{i}) ^ i+1]
         *
         * Dimensions: d

         The function is unimodal.

         Input Domain:

         The function is usually evaluated on the hypercube xi ∈ [-1, 1], for all i = 1, …, d.

         Global Minimum:

         f(x) = 0, at x = (0,...,0)

         */
        long[] integer_values = genes.toLongArray();
        double fitness = 0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness += Math.pow(Math.abs(x), i + 1);
        }

        return storeScaledInvertedFitness(fitness_factor, fitness);
    }

    private double interpolate(int i, long[] integer_values){
        return interpolation_ratio * integer_values[i];
    }
}
