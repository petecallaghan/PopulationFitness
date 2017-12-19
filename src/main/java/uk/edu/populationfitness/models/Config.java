package uk.edu.populationfitness.models;

import uk.edu.populationfitness.models.genes.GenesFactory;
import uk.edu.populationfitness.models.genes.bitset.BitSetGenesFactory;
import uk.edu.populationfitness.models.genes.fitness.FitnessRange;

import java.time.Instant;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Config {
    public static final double ZERO = 0.0;
    public static final double PI = Math.PI;

    // Unique identifier
    public String id = Instant.now().toString().replaceAll(":", "-");

    // No genes per individual
    public int number_of_genes;

    // Number of codes per gene
    public int size_of_each_gene;

    // The likely number of mutations per gene
    public int mutations_per_gene;

    public int max_age;

    public int max_breeding_age;

    public int min_breeding_age;

    // The lower bound of the float conversion for genes
    public double float_lower;

    // The upper bound of the float conversion for genes
    public double float_upper;

    //  Probability that a pair will produce offspring in a year
    public double probability_of_breeding;

    // Number of years in the simulation
    public int number_of_years;

    // Defines genes for each individual
    public GenesFactory genesFactory = new BitSetGenesFactory();

    public final FitnessRange range = new FitnessRange();

    public int initial_population = 4000;

    public Config(){
        number_of_genes = 4;
        size_of_each_gene = 10;
        mutations_per_gene = 1;
        max_age = 50;
        max_breeding_age = 35;
        min_breeding_age = 16;
        float_lower = ZERO;
        float_upper = PI;
        probability_of_breeding = 0.6;
        number_of_years = 2150;
    }
}