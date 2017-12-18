package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

import static java.lang.Math.abs;

public class RastriginGenes extends NormalizingBitSetGenes {

    private static final long RastriginTermA = 10;  // The A term in f{x}=sum _{i=1}^{n}[t[x_{i}^{2}-A\cos(2\pi x_{i})]

    private static final double TwoPi = 2 * Math.PI;

    public RastriginGenes(Config config) {
        super(config, 5.12);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        return 40.25 * n;
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * This is a tunable Rastrigin function: https://en.wikipedia.org/wiki/Rastrigin_function
         *
         * f(x)=sum{i=1 to n}[x{i}^2-  A cos(2pi x{i})]
         *
         * The '2pi' term is replaced by 'fitness_factor * pi' to make the function tunable
         */
        return getRastriginFitnessUsingCos(RastriginTermA * integer_values.length, integer_values);
    }

    private double getRastriginFitnessUsingCos(double fitness, long[] integer_values) {
        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            fitness += x * x - RastriginTermA * CosSineCache.cos(TwoPi * x);
        }
        return fitness;
    }
}
