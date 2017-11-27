package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.CachingInterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InterpolatingBitSetGenes;
import uk.edu.populationfitness.models.genes.bitset.InvertedBitSetGenes;

public class StyblinksiTangGenes extends CachingInterpolatingBitSetGenes {

    public StyblinksiTangGenes(Config config) {
        super(config, 5.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
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

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += (Math.pow(x, 4) - 16 * Math.pow(x, 2) + 5 * x);
        }

        return fitness / 2;
    }
}
