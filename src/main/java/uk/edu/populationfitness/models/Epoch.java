package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class Epoch {

    private static final int UNDEFINED_YEAR = -1;

    // Indicates unlimited capacity
    public static final int UNLIMITED_CAPACITY = 0;

    private final Config config;

    public final int start_year;

    public int end_year = UNDEFINED_YEAR;

    // Defines the kill constant for this epoch
    private double kill_constant = 1.0;

    // Defines the fitness adjustment for this epoch
    private double fitness_factor = 1.0;

    // Defines the holding capacity of the environment for this epoch
    public int environment_capacity = UNLIMITED_CAPACITY;

    // Use this to turn off fitness. By default fitness is enabled
    public boolean enable_fitness = true;

    // Max population actually expected for this epoch
    public int expected_max_population = 0;

    // Probability of a pair breeding in a given year
    private double probability_of_breeding;

    private boolean isDisease = false;

    private double total_capacity_factor = 0;

    public Epoch(Config config, int start_year){
        this.start_year = start_year;
        this.config = config;
        this.probability_of_breeding = config.probability_of_breeding;
    }

    public Epoch(Epoch source){
        this.expected_max_population = source.expected_max_population;
        this.start_year = source.start_year;
        this.end_year = source.end_year;
        this.environment_capacity = source.environment_capacity;
        this.enable_fitness = source.enable_fitness;
        this.config = source.config;
        this.fitness_factor = source.fitness_factor;
        this.isDisease = source.isDisease;
        this.kill_constant = source.kill_constant;
        this.probability_of_breeding = source.probability_of_breeding;
        this.total_capacity_factor = source.total_capacity_factor;
    }

    public boolean isCapacityUnlimited(){
        return environment_capacity == UNLIMITED_CAPACITY;
    }

    public boolean isFitnessEnabled(){
        return enable_fitness;
    }

    public Epoch breedingProbability(double probability){
        this.probability_of_breeding = probability;
        return this;
    }

    public double breedingProbability(){
        return probability_of_breeding;
    }

    public Epoch disease(boolean isDisease){
        this.isDisease = isDisease;
        return this;
    }

    public boolean disease(){
        return isDisease;
    }

    public Epoch kill(double kill_constant){
        this.kill_constant = kill_constant;
        return this;
    }

    public double kill(){
        return this.kill_constant;
    }

    public Epoch fitness(double fitness_factor){
        this.fitness_factor = fitness_factor;
        return this;
    }

    public double fitness(){
        return this.fitness_factor;
    }

    public Epoch capacity(int environment_capacity){
        this.environment_capacity = environment_capacity;
        this.expected_max_population = environment_capacity;
        return this;
    }

    public Epoch disableFitness(){
        this.enable_fitness = false;
        return this;
    }

    public Epoch max(int expected_max_population){
        this.expected_max_population = expected_max_population;
        return this;
    }

    public Epoch addCapacityFactor(double capacity_factor){
        this.total_capacity_factor += capacity_factor;
        return this;
    }

    public double averageCapacityFactor(){
        return this.total_capacity_factor / (this.end_year - this.start_year + 1);
    }

    /**
     * Reduces the populations by the ratio
     *
     * P' = P/ratio
     *
     * @param ratio
     */
    public Epoch reducePopulation(int ratio){
        this.expected_max_population = this.expected_max_population / ratio;
        this.environment_capacity = this.environment_capacity / ratio;
        return this;
    }
}
