package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class StyblinksiTangGenes extends NormalizingBitSetGenes {

    public StyblinksiTangGenes(Config config) {
        super(config, 5.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 85.834 * n;
    }

    @Override
    protected double calculateFitnessFromGenes(double[] unknowns) {
        /*
          http://www.sfu.ca/~ssurjano/stybtang.html

          f(x) = 1/2 * sum{i=1 to n}[x{i}^4 - 16x{i}^2 + 5x{i}]

          Dimensions: d

         The function is usually evaluated on the hypercube xi ∈ [-5, 5], for all i = 1, …, d.

         Global Minimum:

         f(x) = -39.16599d at x = (-2.903534,...-2.903534)
         */
        double fitness = 39.166 * unknowns.length;

        for (double x : unknowns) {
            double xSquared = x * x;
            fitness += (xSquared * xSquared - 16 * xSquared + 5 * x);
        }

        return fitness / 2;
    }
}
