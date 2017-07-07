package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Config {
    public static final double ZERO = 0.0;
    public static final double PI = Math.PI;

    // No genes per individual
    public int number_of_genes;

    // Number of codes per gene
    public int size_of_each_gene;

    // Probability that an individual gene code will mutate
    public double mutation_probability;

    public int max_age;

    public int max_breeding_age;

    public int min_breeding_age;

    // The lower bound of the float conversion for genes
    public double float_lower;

    // The upper bound of the float conversion for genes
    public double float_upper;

    public int initial_population_size;

    //  Probability that a pair will produce offspring in a year
    public double probability_of_breeding;

    // Number of years in the simulation
    public int number_of_years;

    // A scaling fitness factor that applies across epochs
    public double fitness_factor;

    public Config(){
        number_of_genes = 4;
        size_of_each_gene = 10;
        mutation_probability = 1.0 / (size_of_each_gene * number_of_genes);
        max_age = 50;
        max_breeding_age = 35;
        min_breeding_age = 16;
        float_lower = ZERO;
        float_upper = PI;
        initial_population_size = 4000;
        probability_of_breeding = 0.6;
        number_of_years = 2150;
        fitness_factor = 1.0;
    }
}