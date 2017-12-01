package uk.edu.populationfitness.models.genes.localmimina;

import uk.edu.populationfitness.models.Config;
import uk.edu.populationfitness.models.fastmaths.CosSineCache;
import uk.edu.populationfitness.models.fastmaths.FastMaths;
import uk.edu.populationfitness.models.genes.bitset.NormalizingBitSetGenes;

public class AckleysGenes extends NormalizingBitSetGenes {

    private static double alpha = 4.5;

    private static double TwentyPlusE = 20.0 + Math.E;

    private static double TwoPi = 2.0 * Math.PI;

    public AckleysGenes(Config config) {
        super(config, 5.0);
    }

    @Override
    protected double calculateNormalizationRatio(int n) {
        /**
         * {f left (x right )} over {20 left (1- {e} ^ {-0.2α} right ) +e- {e} ^ {-1}}
         */
        return 20.0 * (1.0 - FastMaths.exp(-0.2 * alpha)) + Math.E - FastMaths.exp(-1.0);
    }

    @Override
    protected double calculateFitnessFromIntegers(long[] integer_values) {
        /**
         * http://www.cs.unm.edu/~neal.holts/dga/benchmarkFunction/ackley.html
         *
         * f left (x right ) =-20 exp {left (-0.2 sqrt {{1} over {n} sum from {i=1} to {n} {{x} rsub {i} rsup {2}}} right )} - exp {left ({1} over {n} sum from {i=1} to {n} {cos {left ({2πx} rsub {i} right )}} right ) +20+ exp⁡ (1)}
         */
        double firstSum = 0.0;
        double secondSum = 0.0;

        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(integer_values[i]);
            firstSum += FastMaths.pow(x, 2);
            secondSum += CosSineCache.cos(TwoPi * x);
        }

        double n = integer_values.length;

        return -20.0 * FastMaths.exp(-0.2 * Math.sqrt(firstSum / n)) - FastMaths.exp(secondSum / n) + TwentyPlusE;
    }
}
