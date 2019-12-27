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

    // Defines the fitness adjustment for this epoch
    private double fitness_factor = 1.0;

    // Defines the holding capacity of the environment for this epoch
    public int environment_capacity = UNLIMITED_CAPACITY;

    public int prev_environment_capacity;

    // Use this to turn off fitness. By default fitness is enabled
    public boolean enable_fitness = true;

    // Max population actually expected for this epoch
    public int expected_max_population = 0;

    // Probability of a pair breeding in a given year
    private double probability_of_breeding;

    private boolean isDisease = false;

    private int max_age;

    private int max_breeding_age;

    private double total_capacity_factor = 0;

    private boolean isModern = false;

    public Epoch(Config config, int start_year){
        this.start_year = start_year;
        this.config = config;
        this.probability_of_breeding = config.getProbabilityOfBreeding();
        this.max_breeding_age = config.getMaxBreedingAge();
        this.max_age = config.getMaxAge();
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
        this.probability_of_breeding = source.probability_of_breeding;
        this.total_capacity_factor = source.total_capacity_factor;
        this.prev_environment_capacity = source.prev_environment_capacity;
        this.max_age = source.max_age;
        this.max_breeding_age = source.max_breeding_age;
        this.isModern = source.isModern;
    }

    public boolean isCapacityUnlimited(){
        return environment_capacity == UNLIMITED_CAPACITY;
    }

    public int capacity()
    {
        return isCapacityUnlimited() ? expected_max_population : environment_capacity;
    }

    public int capacityForYear(int year){
        final int capacityRange = capacity() - prev_environment_capacity;
        final int yearRange = end_year - start_year;
        return prev_environment_capacity + ((capacityRange * (year - start_year)) / yearRange);
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

    public Epoch modern(boolean isModern){
        this.isModern = isModern;
        return this;
    }

    public boolean modern(){
        return isModern;
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

    public Epoch max(int expected_max_population){
        this.expected_max_population = expected_max_population;
        return this;
    }

    public Epoch addCapacityFactor(double capacity_factor){
        this.total_capacity_factor += capacity_factor;
        return this;
    }

    public double averageCapacityFactor(){
        if (total_capacity_factor == 0){
            return 1;
        }
        return this.total_capacity_factor / (this.end_year - this.start_year + 1);
    }

    public int maxAge() {
        return max_age;
    }

    public Epoch maxAge(int max_age) {
        this.max_age = max_age;
        return this;
    }

    public int maxBreedingAge() {
        return max_breeding_age;
    }

    public Epoch maxBreedingAge(int max_breeding_age) {
        this.max_breeding_age = max_breeding_age;
        return this;
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
        this.prev_environment_capacity = this.prev_environment_capacity / ratio;
        return this;
    }

    /**
     * Increases the populations by the ratio
     *
     * P' = P * ratio
     *
     * @param ratio
     */
    public Epoch increasePopulation(int ratio){
        this.expected_max_population = this.expected_max_population * ratio;
        this.environment_capacity = this.environment_capacity * ratio;
        this.prev_environment_capacity = this.prev_environment_capacity * ratio;
        return this;
    }

    public Config config(){
        return this.config;
    }
}
