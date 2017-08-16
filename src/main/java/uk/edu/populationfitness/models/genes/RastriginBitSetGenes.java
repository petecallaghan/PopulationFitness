package uk.edu.populationfitness.models.genes;

import uk.edu.populationfitness.models.Config;

import static java.lang.Math.abs;

public class RastriginBitSetGenes extends BitSetGenes {

    private static final long RastriginTermA = 10;  // The A term in f{x}=An+\sum _{i=1}^{n}[t[x_{i}^{2}-A\cos(2\pi x_{i})]

    private final double  RastriginTermAtimesN;

    private double interpolation_ratio;

    private double remainder_interpolation_ratio;

    private static final long Scale = 100; // Reduce the result by a factor of 100

    private double interpolationRatio(Config config, long max_value){
        return (config.float_upper - config.float_lower) / max_value;
    }

    private long maxForBits(long bitCount){
        return Math.min(Long.MAX_VALUE, (long)Math.pow(2, bitCount)-1);
    }

    public RastriginBitSetGenes(Config config) {
        super(config);
        long max_value = maxForBits(size_of_genes);
        long remainder_max_value = maxForBits(size_of_genes % Long.SIZE);
        interpolation_ratio = interpolationRatio(config, max_value);
        remainder_interpolation_ratio = interpolationRatio(config, remainder_max_value);
        RastriginTermAtimesN = RastriginTermA * config.number_of_genes;
    }

    @Override
    public double fitness(double fitness_factor) {
        if (abs(fitness_factor - stored_fitness_factor) < 0.01) {
            return stored_fitness;
        }
        // We need to calculate the fitness again
        stored_fitness_factor = fitness_factor;

        /**
         * This is a tunable Rastrigin function: https://en.wikipedia.org/wiki/Rastrigin_function
         *
         * f(x)=An +sum{i=1 to n}[x{i}^2-  A cos(2pi x{i})]
         *
         * The '2pi' term is replaced by 'fitness_factor * pi' to make the function tunable
         */
        double fitness = RastriginTermAtimesN;
        long[] integer_values = genes.toLongArray();

        double absolute_fitness_factor = abs(fitness_factor);

        /**
         * Can shift the fitness factor between cos and sin by interpreting the sign of the fitness factor
         */
        if (fitness_factor < 0) {
            fitness = getRastriginFitnessUsingSin(absolute_fitness_factor, fitness, integer_values);
        }
        else{
            fitness = getRastriginFitnessUsingCos(absolute_fitness_factor, fitness, integer_values);
        }

        stored_fitness = fitness / Scale;

        return stored_fitness;
    }

    private double getRastriginFitnessUsingSin(double fitness_factor, double fitness, long[] integer_values) {
        for(int i = 0; i < integer_values.length; i++){
            double ratio = (i == integer_values.length - 1 ? remainder_interpolation_ratio : interpolation_ratio);
            double x = ratio * integer_values[i];
            fitness += Math.pow(x, 2) - RastriginTermA * Math.sin(fitness_factor * Math.PI * x);
        }
        return fitness;
    }

    private double getRastriginFitnessUsingCos(double fitness_factor, double fitness, long[] integer_values) {
        for(int i = 0; i < integer_values.length; i++){
            double ratio = (i == integer_values.length - 1 ? remainder_interpolation_ratio : interpolation_ratio);
            double x = ratio * integer_values[i];
            fitness += Math.pow(x, 2) - RastriginTermA * Math.cos(fitness_factor * Math.PI * x);
        }
        return fitness;
    }
}
