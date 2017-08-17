package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

public class RastriginBitSetGenes extends BitSetGenes {

    private static final long RastriginTermA = 10;  // The A term in f{x}=sum _{i=1}^{n}[t[x_{i}^{2}-A\cos(2\pi x_{i})]

    private static final double TwoPi = 2 * Math.PI;

    private final long Scale;

    private double interpolation_ratio;

    private double remainder_interpolation_ratio;

    private double interpolationRatio(long max_value){
        return 5.12 / max_value;
    }

    private long maxForBits(long bitCount){
        return Math.min(Long.MAX_VALUE, (long)Math.pow(2, bitCount)-1);
    }

    public RastriginBitSetGenes(Config config) {
        super(config);
        long max_value = maxForBits(size_of_genes);
        long remainder_max_value = maxForBits(size_of_genes % Long.SIZE);
        interpolation_ratio = interpolationRatio(max_value);
        remainder_interpolation_ratio = interpolationRatio(remainder_max_value);
        Scale = RastriginTermA * config.number_of_genes;
    }

    @Override
    public double fitness(double fitness_factor) {
        if (abs(fitness_factor - stored_fitness_factor) < 0.000001) {
            return stored_fitness;
        }
        // We need to calculate the fitness again
        stored_fitness_factor = fitness_factor;

        /**
         * This is a tunable Rastrigin function: https://en.wikipedia.org/wiki/Rastrigin_function
         *
         * f(x)=sum{i=1 to n}[x{i}^2-  A cos(2pi x{i})]
         *
         * The '2pi' term is replaced by 'fitness_factor * pi' to make the function tunable
         */
        double fitness = 0;
        long[] integer_values = genes.toLongArray();

        double absolute_fitness_factor = abs(fitness_factor);

        /**
         * Can shift the fitness factor between cos and sin by interpreting the sign of the fitness factor
         */
        if (fitness_factor < 0) {
            fitness = getRastriginFitnessUsingSin(fitness, integer_values);
        }
        else{
            fitness = getRastriginFitnessUsingCos(fitness, integer_values);
        }

        stored_fitness = abs(fitness * absolute_fitness_factor) / Scale;

        return stored_fitness;
    }

    private double interpolate(int i, long[] integer_values){
        double ratio = (i == integer_values.length - 1 ? remainder_interpolation_ratio : interpolation_ratio);
        return ratio * integer_values[i];
    }

    private double getRastriginFitnessUsingSin(double fitness, long[] integer_values) {
        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness += Math.pow(x, 2) - RastriginTermA * Math.sin(TwoPi * x);
        }
        return fitness;
    }

    private double getRastriginFitnessUsingCos(double fitness, long[] integer_values) {
        for(int i = 0; i < integer_values.length; i++){
            double x = interpolate(i, integer_values);
            fitness += Math.pow(x, 2) - RastriginTermA * Math.cos(TwoPi * x);
        }
        return fitness;
    }
}
